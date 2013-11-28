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
 *   PersonForm.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments.forms;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.lang.Classes;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;

import com.googlecode.wicket.jquery.ui.form.datepicker.DatePicker;

import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.wui.components.table.TimestampConverter;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.StringUtils;
import cz.zcu.kiv.eegdatabase.wui.core.Gender;
import cz.zcu.kiv.eegdatabase.wui.core.Laterality;
import cz.zcu.kiv.eegdatabase.wui.core.educationlevel.EducationLevelFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;

public class PersonForm extends Form<Person> {

    private static final long serialVersionUID = 36670720990541049L;

    @SpringBean
    private EducationLevelFacade educationFacade;

    @SpringBean
    private PersonFacade personFacade;

    private FeedbackPanel feedback;

    public PersonForm(String id, final ModalWindow window) {
        super(id, new CompoundPropertyModel<Person>(new Person()));

        add(new Label("addPersonHeader", ResourceUtils.getModel("pageTitle.addPerson")));

        feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        add(feedback);

        TextField<String> name = new TextField<String>("givenname");
        name.setLabel(ResourceUtils.getModel("label.name"));
        name.setRequired(true);
        name.add(new PatternValidator(StringUtils.REGEX_ONLY_LETTERS));
        add(name);

        TextField<String> surname = new TextField<String>("surname");
        surname.setLabel(ResourceUtils.getModel("label.surname"));
        surname.setRequired(true);
        surname.add(new PatternValidator(StringUtils.REGEX_ONLY_LETTERS));
        add(surname);

        DatePicker date = new DatePicker("dateOfBirth") {

            private static final long serialVersionUID = 1L;

            @Override
            public <C> IConverter<C> getConverter(Class<C> type) {
                return (IConverter<C>) new TimestampConverter();
            }
        };

        date.setLabel(ResourceUtils.getModel("label.dateOfBirth"));
        date.setRequired(true);
        add(date);

        EmailTextField email = new EmailTextField("username");
        email.setLabel(ResourceUtils.getModel("label.email"));
        email.setRequired(true);
        add(email);

        TextField<String> phoneNumber = new TextField<String>("phoneNumber");
        phoneNumber.setLabel(ResourceUtils.getModel("label.phoneNumber"));
        add(phoneNumber);

        RadioChoice<Character> gender = new RadioChoice<Character>("gender", Gender.getShortcutList(), new ChoiceRenderer<Character>() {

            private static final long serialVersionUID = 1L;

            @Override
            public Object getDisplayValue(Character object) {
                Gender enumValue = Gender.getGenderByShortcut(object);
                return getString(Classes.simpleName(enumValue.getDeclaringClass()) + '.' + enumValue.name());
            }

        });
        gender.setSuffix("\n");
        gender.setRequired(true);
        gender.setLabel(ResourceUtils.getModel("label.gender"));
        add(gender);

        TextArea<String> note = new TextArea<String>("note");
        note.setLabel(ResourceUtils.getModel("label.note"));
        note.add(StringValidator.maximumLength(255));
        add(note);

        DropDownChoice<Character> laterality = new DropDownChoice<Character>("laterality", Laterality.getShortcutList(),
                new ChoiceRenderer<Character>() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public Object getDisplayValue(Character object) {
                        Laterality enumValue = Laterality.getLateralityByShortcut(object);
                        return getString(Classes.simpleName(enumValue.getDeclaringClass()) + '.' + enumValue.name());
                    }

                });

        laterality.setLabel(ResourceUtils.getModel("label.laterality"));
        add(laterality);

        DropDownChoice<EducationLevel> educationLevel = new DropDownChoice<EducationLevel>("educationLevel", educationFacade.getAllRecords(),
                new ChoiceRenderer<EducationLevel>("title", "educationLevelId") {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public Object getDisplayValue(EducationLevel object) {
                        return object.getEducationLevelId() + " " + super.getDisplayValue(object);
                    }

                });

        educationLevel.setLabel(ResourceUtils.getModel("label.educationLevel"));
        add(educationLevel);

        AjaxButton submit = new AjaxButton("submitForm", ResourceUtils.getModel("button.save"), this) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedback);
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                Person user = PersonForm.this.getModelObject();

                boolean isEdit = user.getPersonId() > 0;

                if (validation(user, personFacade, isEdit)) {
                    if (isEdit) {
                        personFacade.update(user);
                    } else {
                        user.setAuthority(Util.ROLE_READER);
                        personFacade.create(user);
                    }
                    window.close(target);
                }
                target.add(feedback);
            }
        };
        add(submit);

        add(new AjaxButton("closeForm", ResourceUtils.getModel("button.close"), this) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                window.close(target);
            }
        }.setDefaultFormProcessing(false));

        setOutputMarkupId(true);
    }

    private boolean validation(Person user, PersonFacade facade, boolean editation) {

        boolean validate = true;

        // if its editation we can't check if email exist
        if (!editation && facade.usernameExists(user.getEmail())) {
            error(ResourceUtils.getString("inUse.email"));
            validate = false;
        }

        if (user.getDateOfBirth().getTime() >= System.currentTimeMillis()) {
            error(ResourceUtils.getString("invalid.dateOfBirth"));
            validate = false;
        }

        if (user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty()) {
            try {
                if (user.getPhoneNumber().charAt(0) == '+') {
                    Long.parseLong(user.getPhoneNumber().substring(1));
                } else {
                    Long.parseLong(user.getPhoneNumber());
                }

            } catch (NumberFormatException ex) {
                error(ResourceUtils.getString("invalid.phoneNumber"));
                validate = false;
            }

        }

        return validate;
    }
}
