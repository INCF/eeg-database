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
 *   TemplateSharePage.java, 2015/03/24 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments.metadata.template;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Template;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.metadata.TemplateFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsPageLeftMenu;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AbstractAutoCompleteTextRenderer;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteSettings;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.Strings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class TemplateSharePage extends MenuPage {

    private static final long serialVersionUID = 5800903656913762022L;

    @SpringBean
    private TemplateFacade templateFacade;

    @SpringBean
    private PersonFacade personFacade;

    private Model<Template> templateModel;

    private Model<Person> personModel;

    public TemplateSharePage() {

        setPageTitle(ResourceUtils.getModel("pageTitle.template.share"));
        add(new ButtonPageMenu("leftMenu", ExperimentsPageLeftMenu.values()));

        setupComponents();
    }

    private void setupComponents() {
        int personId = EEGDataBaseSession.get().getLoggedUser().getPersonId();
        List<Template> templatesByPerson = templateFacade.getTemplatesByPerson(personId);
        templateModel = new Model<Template>();
        personModel = new Model<Person>();

        ChoiceRenderer<Template> templateChoiceRenderer = new ChoiceRenderer<Template>("name", "templateId");

        DropDownChoice<Template> templateSelection = new DropDownChoice<Template>("template-choice", templateModel, templatesByPerson, templateChoiceRenderer);

        // added autocomplete textfield for person
        AutoCompleteSettings settings = prepareAutoCompleteSettings();

        AbstractAutoCompleteTextRenderer<Person> renderer = new AbstractAutoCompleteTextRenderer<Person>() {

            private static final long serialVersionUID = 1L;

            @Override
            protected String getTextValue(Person object) {
                return object.getAutoCompleteData();
            }
        };

        final AutoCompleteTextField<Person> personSelection = new AutoCompleteTextField<Person>("person-choice", personModel,
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

        personSelection.setLabel(ResourceUtils.getModel("label.subjectPerson"));
        Form<Void> form = new Form<Void>("form");
        add(form);
        form.add(personSelection);
        form.add(templateSelection);
        form.add(new AjaxButton("share-button", new Model<String>("Share")) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                Template template = templateModel.getObject();
                Person person = personModel.getObject();

                Template newTemplate = new Template();
                newTemplate.setName(template.getName());
                newTemplate.setPersonByPersonId(person);
                newTemplate.setTemplate(template.getTemplate());

                templateFacade.create(newTemplate);
                templateModel.setObject(null);
                personModel.setObject(null);

                info(ResourceUtils.getString("text.template.shared", template.getName(), person.getUsername()));
                target.add(getFeedback());
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(getFeedback());
            }

        });
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
