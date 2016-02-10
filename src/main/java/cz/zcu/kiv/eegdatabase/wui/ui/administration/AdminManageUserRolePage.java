/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   AdminManageUserRolePage.java, 2014/05/13 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.administration;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroupMembership;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.GroupRole;
import cz.zcu.kiv.eegdatabase.wui.core.UserRole;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AbstractAutoCompleteTextRenderer;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteSettings;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.Strings;

import java.util.*;

/**
 * Page for change user role.
 * 
 * @author Jakub Rinkes
 * 
 */
@AuthorizeInstantiation("ROLE_ADMIN")
public class AdminManageUserRolePage extends MenuPage {

    private static final long serialVersionUID = -1047855945455763134L;

    @SpringBean
    PersonFacade personFacade;

    @SpringBean
    ResearchGroupFacade groupFacade;

    public AdminManageUserRolePage() {

        setPageTitle(ResourceUtils.getModel("pageTitle.changeUserRole"));

        add(new ButtonPageMenu("leftMenu", AdministrationPageLeftMenu.values()));

        add(new ChangeUserRoleForm("form", new Model<Person>(), personFacade, getFeedback()));
    }

    // inner for used for change user role
    private class ChangeUserRoleForm extends Form<Person> {

        private static final long serialVersionUID = 1L;

        public ChangeUserRoleForm(String id, IModel<Person> model, final PersonFacade facade, final FeedbackPanel feedback) {
            super(id, new CompoundPropertyModel<Person>(model));

            final DropDownChoice<UserRole> roles = new DropDownChoice<UserRole>("authority", new Model<UserRole>(), Arrays.asList(UserRole.values()),
                    new EnumChoiceRenderer<UserRole>());
            roles.setLabel(ResourceUtils.getModel("label.userRole"));
            roles.setOutputMarkupId(true);
            roles.add(new AjaxFormComponentUpdatingBehavior("onChange") {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onUpdate(AjaxRequestTarget target) {

                    UserRole role = roles.getModelObject();
                    Person person = ChangeUserRoleForm.this.getModelObject();
                    if (person != null && role != null) {
                        person.setAuthority(role.name());
                        personFacade.update(person);

                        String localizedValue = ResourceUtils.getString(roles.getModelObject().getClass().getSimpleName() + "." + roles.getModelObject());
                        info(ResourceUtils.getString("text.administration.role.user.changed", ChangeUserRoleForm.this.getModelObject().getUsername(), localizedValue));
                        target.add(feedback);
                    }
                }
            });

            final PropertyListView<ResearchGroupMembership> groupRoles = new PropertyListView<ResearchGroupMembership>("groupRoles", Collections.EMPTY_LIST) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void populateItem(ListItem<ResearchGroupMembership> item) {
                    item.add(new Label("authority"));
                    item.add(new Label("person.username"));
                    item.add(new Label("researchGroup.title"));

                    final ResearchGroupMembership membership = item.getModelObject();
                    final DropDownChoice<GroupRole> groupRoles = new DropDownChoice<GroupRole>("groupRoles",
                            new Model<GroupRole>(GroupRole.valueOf(membership.getAuthority())),
                            Arrays.asList(GroupRole.values()),
                            new EnumChoiceRenderer<GroupRole>());

                    groupRoles.add(new AjaxFormComponentUpdatingBehavior("onChange") {

                        private static final long serialVersionUID = 1L;

                        @Override
                        protected void onUpdate(AjaxRequestTarget target) {

                            GroupRole groupRole = groupRoles.getModelObject();
                            membership.setAuthority(groupRole.name());
                            groupFacade.updateMemberhip(membership);

                            String localizedValue = ResourceUtils.getString(groupRole.getClass().getSimpleName() + "." + groupRole);
                            info(ResourceUtils.getString("text.administration.role.group.changed", membership.getPerson().getUsername(),
                                    membership.getResearchGroup().getTitle(), localizedValue));
                            target.add(feedback);
                        }

                    });

                    item.add(groupRoles);

                }

                @Override
                public boolean isVisible() {
                    return getModelObject() != null && getModelObject().size() != 0;
                }

            };

            WebMarkupContainer noGroups = new WebMarkupContainer("noGroups") {

                private static final long serialVersionUID = 1L;

                @Override
                public boolean isVisible() {
                    return !groupRoles.isVisible();
                }
            };

            // added autocomplete textfield for subject
            AutoCompleteSettings settings = prepareAutoCompleteSettings();

            AbstractAutoCompleteTextRenderer<Person> renderer = new AbstractAutoCompleteTextRenderer<Person>() {

                private static final long serialVersionUID = 1L;

                @Override
                protected String getTextValue(Person object) {
                    return object.getAutoCompleteData();
                }
            };

            final AutoCompleteTextField<Person> personField = new AutoCompleteTextField<Person>("personField", new Model<Person>(),
                    Person.class, renderer, settings) {

                private static final long serialVersionUID = 1L;

                @Override
                protected Iterator<Person> getChoices(String input) {
                    List<Person> choices;
                    List<Person> allChoices = personFacade.getAllRecords();

                    if (Strings.isEmpty(input)) {
                        choices = allChoices;
                    } else {

                        choices = new ArrayList<Person>(10);
                        for (Person t : allChoices) {
                            if ((t.getAutoCompleteData() != null) &&
                                    t.getAutoCompleteData().toLowerCase().contains(input.toLowerCase())
                                    || t.getAutoCompleteData().toLowerCase().startsWith(input.toLowerCase())) {
                                choices.add(t);
                            }
                        }
                    }
                    Collections.sort(choices);
                    return choices.iterator();
                }
            };

            personField.setLabel(ResourceUtils.getModel("label.subjectPerson"));
            personField.add(new AjaxFormComponentUpdatingBehavior("onChange") {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onUpdate(AjaxRequestTarget target) {

                    Person person = personField.getModelObject();
                    if (person != null && person.getPersonId() != 0) {
                        ChangeUserRoleForm.this.setModelObject(person);
                        groupRoles.setModelObject(groupFacade.readMemberhipByParameter("person.id", person.getPersonId()));
                        roles.setModelObject(UserRole.valueOf(person.getAuthority()));
                    }

                    target.add(getFeedback(), ChangeUserRoleForm.this);
                }
            });

            add(roles, noGroups, groupRoles, personField);
        }

    }

    private AutoCompleteSettings prepareAutoCompleteSettings() {

        AutoCompleteSettings settings = new AutoCompleteSettings();
        settings.setShowListOnEmptyInput(true);
        settings.setShowCompleteListOnFocusGain(true);
        settings.setMaxHeightInPx(200);
        settings.setAdjustInputWidth(false);
        return settings;
    }

}
