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
 *   PersonFormPage.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.people.form;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.lang.Classes;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;

import com.googlecode.wicket.jquery.ui.form.datepicker.DatePicker;

import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.table.TimestampConverter;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.StringUtils;
import cz.zcu.kiv.eegdatabase.wui.core.Gender;
import cz.zcu.kiv.eegdatabase.wui.core.Laterality;
import cz.zcu.kiv.eegdatabase.wui.core.educationlevel.EducationLevelFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.people.ListPersonPage;
import cz.zcu.kiv.eegdatabase.wui.ui.people.PersonDetailPage;
import cz.zcu.kiv.eegdatabase.wui.ui.people.PersonPageLeftMenu;

/**
 * Page add / edit action of person.
 * 
 * @author Jakub Rinkes
 *
 */
@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class PersonFormPage extends MenuPage {

    private static final long serialVersionUID = -7674071655686906138L;

    @SpringBean
    EducationLevelFacade educationFacade;

    @SpringBean
    PersonFacade facade;

    @SpringBean
    SecurityFacade securityFacade;

    public PersonFormPage() {

        setPageTitle(ResourceUtils.getModel("pageTitle.addPerson"));
        add(new Label("title", ResourceUtils.getModel("pageTitle.addPerson")));

        add(new ButtonPageMenu("leftMenu", PersonPageLeftMenu.values()));

        add(new PersonForm("form", new Model<Person>(new Person()), educationFacade, facade, getFeedback()));
    }

    public PersonFormPage(PageParameters parameters) {

        StringValue paramId = parameters.get(DEFAULT_PARAM_ID);

        if (paramId.isNull() || paramId.isEmpty())
            throw new RestartResponseAtInterceptPageException(ListPersonPage.class);

        setPageTitle(ResourceUtils.getModel("pageTitle.editPerson"));
        add(new Label("title", ResourceUtils.getModel("pageTitle.editPerson")));

        add(new ButtonPageMenu("leftMenu", PersonPageLeftMenu.values()));

        Person person = facade.getPersonForDetail(paramId.toInt());

        if (!securityFacade.userCanEditPerson(person.getPersonId()))
            throw new RestartResponseAtInterceptPageException(PersonDetailPage.class, PageParametersUtils.getDefaultPageParameters(person.getPersonId()));

        add(new PersonForm("form", new Model<Person>(person), educationFacade, facade, getFeedback()));
    }
    
    // inner form for add / edit person action.
    private class PersonForm extends Form<Person> {

        private static final long serialVersionUID = 1L;

        public PersonForm(String id, IModel<Person> model, final EducationLevelFacade educationFacade, final PersonFacade personFacade, final FeedbackPanel feedback) {
            super(id, new CompoundPropertyModel<Person>(model));

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

            DatePicker date = new DatePicker("dateOfBirth") {

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

            AjaxButton submit = new AjaxButton("submit", ResourceUtils.getModel("button.save"), this) {

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
                        setResponsePage(ListPersonPage.class);
                    }
                    target.add(feedback);
                }
            };
            add(submit);
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
}
