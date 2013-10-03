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
 *   ChangeUserRolePage.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.administration;

import java.util.Arrays;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.UserRole;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;

/**
 * Page for change user role.
 * 
 * @author Jakub Rinkes
 *
 */
@AuthorizeInstantiation("ROLE_ADMIN")
public class ChangeUserRolePage extends MenuPage {

    private static final long serialVersionUID = -1047855945455763134L;

    @SpringBean
    PersonFacade personFacade;

    public ChangeUserRolePage() {

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
            roles.setRequired(true);
            roles.setOutputMarkupId(true);

            final DropDownChoice<Person> persons = new DropDownChoice<Person>("username", new Model<Person>(), facade.getAllRecords(), new ChoiceRenderer<Person>("username", "personId"));
            persons.setRequired(true);
            persons.setLabel(ResourceUtils.getModel("label.emailLogin"));
            persons.add(new AjaxFormComponentUpdatingBehavior("onChange") {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    Person person = persons.getModelObject();
                    setModelObject(person);
                    roles.setModelObject(UserRole.valueOf(person.getAuthority()));
                    target.add(ChangeUserRoleForm.this);
                    target.add(roles);
                }
            });

            AjaxButton submit = new AjaxButton("submit", ResourceUtils.getModel("button.changeRole")) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedback);
                }

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                    Person person = ChangeUserRoleForm.this.getModelObject();

                    person.setAuthority(roles.getModelObject().name());
                    personFacade.update(person);

                    info(ResourceUtils.getString("pageTitle.roleChanged"));

                    setVisibilityAllowed(false);
                    target.add(feedback);
                    target.add(this);
                }
            };
            submit.setOutputMarkupId(true);
            add(submit, roles, persons);
        }

    }

}
