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
 *   PersonForm.java, 2013/05/13 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.components.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.lang.Classes;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.form.input.DateTimeFieldPicker;
import cz.zcu.kiv.eegdatabase.wui.components.table.TimestampConverter;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.StringUtils;
import cz.zcu.kiv.eegdatabase.wui.core.Gender;
import cz.zcu.kiv.eegdatabase.wui.core.Laterality;
import cz.zcu.kiv.eegdatabase.wui.core.UserRole;
import cz.zcu.kiv.eegdatabase.wui.core.educationlevel.EducationLevelFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;

public class PersonForm extends Form<Person> {

    private static final long serialVersionUID = 1L;

    public PersonForm(String id, IModel<Person> model, final EducationLevelFacade educationFacade, final PersonFacade personFacade, final FeedbackPanel feedback) {
        super(id, new CompoundPropertyModel<Person>(model));

        final boolean isUserAdmin = EEGDataBaseSession.get().hasRole(UserRole.ROLE_ADMIN.name());

        TextField<String> name = new TextField<String>("givenname");
        name.setLabel(ResourceUtils.getModel("label.name"));
        name.setRequired(true);
        name.add(new PatternValidator(StringUtils.REGEX_ONLY_LETTERS));
        FormComponentLabel nameLabel = new FormComponentLabel("nameLb", name);
        add(name, nameLabel);

        TextField<String> surname = new TextField<String>("surname");
        surname.setLabel(ResourceUtils.getModel("label.surname"));
        surname.setRequired(true);
        surname.add(new PatternValidator(StringUtils.REGEX_ONLY_LETTERS));
        FormComponentLabel surnameLabel = new FormComponentLabel("surnameLb", surname);
        add(surname, surnameLabel);

        DateTimeFieldPicker date = new DateTimeFieldPicker("dateOfBirth") {

            private static final long serialVersionUID = 1L;

            @Override
            public <C> IConverter<C> getConverter(Class<C> type) {
                return (IConverter<C>) new TimestampConverter();
            }
        };

        date.setLabel(ResourceUtils.getModel("label.dateOfBirth"));
        date.setRequired(true);
        FormComponentLabel dateLabel = new FormComponentLabel("dateLb", date);
        add(date, dateLabel);

        EmailTextField email = new EmailTextField("username");
        email.setLabel(ResourceUtils.getModel("label.email"));
        email.setRequired(true);
        FormComponentLabel emailLabel = new FormComponentLabel("emailLb", email);
        add(email, emailLabel);

        // only for admins

        final PasswordTextField password = new PasswordTextField("password", new Model<String>(""));
        password.setLabel(ResourceUtils.getModel("general.password"));
        password.add(StringValidator.minimumLength(6));
        password.setRequired(false);
        password.setVisibilityAllowed(isUserAdmin);
        password.setVisible(false);
        add(password);

        final PasswordTextField passwordVerify = new PasswordTextField("passwordVerify", new Model<String>(""));
        passwordVerify.setLabel(ResourceUtils.getModel("general.password.verify"));
        passwordVerify.add(StringValidator.minimumLength(6));
        passwordVerify.setRequired(false);
        passwordVerify.setVisibilityAllowed(isUserAdmin);
        passwordVerify.setVisible(false);
        add(passwordVerify);

        final AjaxCheckBox changePasswordBox = new AjaxCheckBox("changePassword", new Model<Boolean>(Boolean.FALSE)) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {

                Boolean visible = getModelObject();
                password.setVisible(visible);
                password.setRequired(visible);
                passwordVerify.setVisible(visible);
                passwordVerify.setRequired(visible);

                target.add(PersonForm.this);
            }
        };
        changePasswordBox.setVisibilityAllowed(isUserAdmin);
        add(changePasswordBox);
        // end only for admins

        TextField<String> phoneNumber = new TextField<String>("phoneNumber");
        phoneNumber.setLabel(ResourceUtils.getModel("label.phoneNumber"));
        FormComponentLabel phoneNumberLabel = new FormComponentLabel("phoneNumberLb", phoneNumber);
        add(phoneNumber, phoneNumberLabel);

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
        FormComponentLabel genderLabel = new FormComponentLabel("genderLb", gender);
        add(gender, genderLabel);

        TextArea<String> note = new TextArea<String>("note");
        note.setLabel(ResourceUtils.getModel("label.note"));
        note.add(StringValidator.maximumLength(255));
        FormComponentLabel noteLabel = new FormComponentLabel("noteLb", note);
        add(note, noteLabel);

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
        FormComponentLabel lateralityLabel = new FormComponentLabel("lateralityLb", laterality);
        add(laterality, lateralityLabel);

        DropDownChoice<EducationLevel> educationLevel = new DropDownChoice<EducationLevel>("educationLevel", educationFacade.getAllRecords(),
                new ChoiceRenderer<EducationLevel>("title", "educationLevelId") {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public Object getDisplayValue(EducationLevel object) {
                        return object.getEducationLevelId() + " " + super.getDisplayValue(object);
                    }

                });

        educationLevel.setLabel(ResourceUtils.getModel("label.educationLevel"));
        FormComponentLabel educationLevelLabel = new FormComponentLabel("educationLevelLb", educationLevel);
        add(educationLevel, educationLevelLabel);

        CheckBox lockCheckBox = new CheckBox("lock");
        add(lockCheckBox);
        
        CheckBox confirmCheckBox = new CheckBox("confirmed");
        add(confirmCheckBox);

        AjaxButton submit = new AjaxButton("submit", ResourceUtils.getModel("button.save"), this) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedback);
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                Person user = PersonForm.this.getModelObject();
                user.setEmail(user.getUsername().toLowerCase());
                boolean isEdit = user.getPersonId() > 0;

                String planPassword = password.getModelObject();
                String plainPasswordVerify = passwordVerify.getModelObject();
                Boolean isPasswordChanged = changePasswordBox.getModelObject();

                if (validation(user, personFacade, isEdit, isUserAdmin, isPasswordChanged, planPassword, plainPasswordVerify)) {
                    if (isEdit) {

                        if (isPasswordChanged)
                            user.setPassword(encodePassword(planPassword));

                        personFacade.update(user);
                    } else {
                        user.setAuthority(Util.ROLE_READER);
                        personFacade.create(user);
                    }
                    setResponsePage(getPage().getClass());
                }

                target.add(feedback);
            }
        };
        add(submit);
    }

    private boolean validation(Person user, PersonFacade facade, boolean editation, boolean isUserAdmin, boolean isPasswordChanged, String password, String passwordVerify) {

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

        if (isUserAdmin && isPasswordChanged && password != null && passwordVerify != null
                && password.isEmpty() && passwordVerify.isEmpty()
                && password.equals(passwordVerify)) {
            error(ResourceUtils.getString("invalid.passwordMatch"));
            validate = false;
        }

        return validate;
    }

    private String encodePassword(String plaintextPassword) {

        return new BCryptPasswordEncoder().encode(plaintextPassword);
    }

}
