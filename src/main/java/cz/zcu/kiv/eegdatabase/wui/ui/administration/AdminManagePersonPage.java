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
 *   AdminManagePersonPage.java, 2014/05/13 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.administration;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.wui.components.form.PersonForm;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.educationlevel.EducationLevelFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AbstractAutoCompleteTextRenderer;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteSettings;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.Strings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@AuthorizeInstantiation("ROLE_ADMIN")
public class AdminManagePersonPage extends MenuPage {

    private static final long serialVersionUID = -353795863744483030L;

    @SpringBean
    private PersonFacade personFacade;

    @SpringBean
    private EducationLevelFacade educationFacade;

    public AdminManagePersonPage() {

        setPageTitle(ResourceUtils.getModel("menuItem.managePerson"));
        add(new ButtonPageMenu("leftMenu", AdministrationPageLeftMenu.values()));

        Model<Person> person = new Model<Person>();
        PersonForm personForm = new PersonForm("form", person, educationFacade, personFacade, getFeedback());
        personForm.setOutputMarkupPlaceholderTag(true);
        personForm.setVisible(false);
        addPersonField(personForm);
        add(personForm);
    }

    private void addPersonField(final PersonForm form) {

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
                if (person.getPersonId() != 0) {
                    form.setModelObject(person);
                    form.setVisible(true);
                }

                target.add(getFeedback(), form);
            }
        });
        Form<Void> selectForm = new Form<Void>("selectForm");
        add(selectForm.add(personField));
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
