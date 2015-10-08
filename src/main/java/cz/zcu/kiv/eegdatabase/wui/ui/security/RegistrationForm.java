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
 *   RegistrationForm.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.security;

import com.octo.captcha.service.image.ImageCaptchaService;
import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.wui.components.form.PersonFormPanel;
import cz.zcu.kiv.eegdatabase.wui.components.form.input.DateTimeFieldPicker;
import cz.zcu.kiv.eegdatabase.wui.components.table.TimestampConverter;
import cz.zcu.kiv.eegdatabase.wui.components.utils.FileUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.StringUtils;
import cz.zcu.kiv.eegdatabase.wui.core.dto.FullPersonDTO;
import cz.zcu.kiv.eegdatabase.wui.core.educationlevel.EducationLevelFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonMapper;
import cz.zcu.kiv.eegdatabase.wui.ui.security.components.RegistrationObject;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.image.resource.BufferedDynamicImageResource;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.joda.time.DateTime;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Form for registration new user.
 *
 * @author Jakub Rinkes
 *
 */
public class RegistrationForm extends Form<RegistrationObject> {

    private static final long serialVersionUID = 4973918066620014022L;

    @SpringBean
    EducationLevelFacade educationLevelFacade;

    @SpringBean
    PersonFacade personFacade;

    @SpringBean
    ImageCaptchaService captchaService;

    private NonCachingImage captchaImage;

    private PersonFormPanel<FullPersonDTO> panelPerson;

    public RegistrationForm(String id, final FeedbackPanel feedback) throws IOException {
        super(id, new CompoundPropertyModel<RegistrationObject>(new RegistrationObject()));

        EmailTextField email = new EmailTextField("email");
        email.setLabel(ResourceUtils.getModel("general.email"));
        email.setRequired(true);
        add(email);

        PasswordTextField password = new PasswordTextField("password");
        password.setLabel(ResourceUtils.getModel("general.password"));
        password.setRequired(true);
        password.add(StringValidator.minimumLength(6));
        add(password);

        PasswordTextField passwordVerify = new PasswordTextField("passwordVerify");
        passwordVerify.setLabel(ResourceUtils.getModel("general.password.verify"));
        passwordVerify.setRequired(true);
        passwordVerify.add(StringValidator.minimumLength(6));
        add(passwordVerify);

        add(panelPerson = new PersonFormPanel<FullPersonDTO>("panelPerson",new CompoundPropertyModel<FullPersonDTO>(new FullPersonDTO()), educationLevelFacade));

//        TextField<String> name = new TextField<String>("name");
//        name.setLabel(ResourceUtils.getModel("general.name"));
//        name.setRequired(true);
//        name.add(new PatternValidator(StringUtils.REGEX_ONLY_LETTERS));
//        add(name);
//
//        TextField<String> surname = new TextField<String>("surname");
//        surname.setLabel(ResourceUtils.getModel("general.surname"));
//        surname.setRequired(true);
//        surname.add(new PatternValidator(StringUtils.REGEX_ONLY_LETTERS));
//        add(surname);
//
//        DateTimeFieldPicker date = new DateTimeFieldPicker("dateOfBirth") {
//
//            private static final long serialVersionUID = 1L;
//
//            @Override
//            public <C> IConverter<C> getConverter(Class<C> type) {
//                return (IConverter<C>) new TimestampConverter();
//            }
//        };
//        date.setLabel(ResourceUtils.getModel("general.dateofbirth"));
//        //date.setRequired(true);
//        add(date);
//
//
//        TextField<String> address = new TextField<String>("address");
//        address.setLabel(ResourceUtils.getModel("label.address"));
//        add(address);
//
//        TextField<String> city = new TextField<String>("city");
//        city.setLabel(ResourceUtils.getModel("label.city"));
//        add(city);
//
//        TextField<String> state = new TextField<String>("state");
//        state.setLabel(ResourceUtils.getModel("label.state"));
//        add(state);
//
//        TextField<String> zipCode = new TextField<String>("zipCode");
//        zipCode.setLabel(ResourceUtils.getModel("label.zipCode"));
//        add(zipCode);
//
//        TextField<String> url = new TextField<String>("url");
//        url.setLabel(ResourceUtils.getModel("label.url"));
//        add(url);
//
//        TextField<String> phone = new TextField<String>("phone");
//        phone.setLabel(ResourceUtils.getModel("label.phoneNumber"));
//        add(phone);
//
//        TextField<String> organization = new TextField<String>("organization");
//        organization.setLabel(ResourceUtils.getModel("label.organization"));
//        add(organization);
//
//        TextField<String> jobTitle = new TextField<String>("jobTitle");
//        jobTitle.setLabel(ResourceUtils.getModel("label.jobTitle"));
//        add(jobTitle);
//
//        TextField<String> orgAddress = new TextField<String>("orgAddress");
//        orgAddress.setLabel(ResourceUtils.getModel("label.address"));
//        add(orgAddress);
//
//        TextField<String> orgCity = new TextField<String>("orgCity");
//        orgCity.setLabel(ResourceUtils.getModel("label.city"));
//        add(orgCity);
//
//        TextField<String> orgState = new TextField<String>("orgState");
//        orgState.setLabel(ResourceUtils.getModel("label.state"));
//        add(orgState);
//
//        TextField<String> orgZipCode = new TextField<String>("orgZipCode");
//        orgZipCode.setLabel(ResourceUtils.getModel("label.zipCode"));
//        add(orgZipCode);
//
//        TextField<String> orgUrl = new TextField<String>("orgUrl");
//        orgUrl.setLabel(ResourceUtils.getModel("label.url"));
//        add(orgUrl);
//
//        TextField<String> orgPhone = new TextField<String>("orgPhone");
//        orgPhone.setLabel(ResourceUtils.getModel("label.phoneNumber"));
//        add(orgPhone);
//
//        TextField<String> VAT = new TextField<String>("VAT");
//        VAT.setLabel(ResourceUtils.getModel("label.VAT"));
//        add(VAT);

        generateCaptchaImageAndPrepareValidation();
        add(captchaImage);

        TextField<String> controlText = new TextField<String>("controlText");
        controlText.setLabel(ResourceUtils.getModel("general.controlText"));
        controlText.setRequired(true);
        add(controlText);

//        RadioChoice<Gender> gender = new RadioChoice<Gender>("gender", Arrays.asList(Gender.values()), new EnumChoiceRenderer<Gender>());
//        gender.setSuffix("\n");
//        gender.setRequired(true);
//        gender.setLabel(ResourceUtils.getModel("general.gender"));
//        add(gender);

//        List<String> listOfTitles = new ArrayList<String>();
//        listOfTitles.add("Mr.");
//        listOfTitles.add("Mrs.");
//        listOfTitles.add("Ms.");
//
//        DropDownChoice<String> title = new DropDownChoice<String>("title", listOfTitles,
//                new ChoiceRenderer<String>() {
//
//                    private static final long serialVersionUID = 1L;
//
//                    @Override
//                    public Object getDisplayValue(String object) {
//                        return object;
//                    }
//
//                });
//
//        title.setRequired(true);
//        title.setLabel(ResourceUtils.getModel("label.title"));
//        add(title);
//
//        File file = ResourceUtils.getFile("countries.txt");
//        List<String> countries = FileUtils.getFileLines(file);
//
//        DropDownChoice<String> country = new DropDownChoice<String>("country", countries,
//                new ChoiceRenderer<String>("country") {
//
//                    private static final long serialVersionUID = 1L;
//
//                    @Override
//                    public Object getDisplayValue(String object) {
//                        return object;
//                    }
//
//                });
//
//        country.setRequired(true);
//        country.setLabel(ResourceUtils.getModel("label.country"));
//        add(country);
//
//        DropDownChoice<String> orgCountry = new DropDownChoice<String>("orgCountry", countries,
//                new ChoiceRenderer<String>("orgCountry") {
//
//                    private static final long serialVersionUID = 1L;
//
//                    @Override
//                    public Object getDisplayValue(String object) {
//                        return object;
//                    }
//
//                });
//
//        //orgCountry.setRequired(true);
//        orgCountry.setLabel(ResourceUtils.getModel("label.country"));
//        add(orgCountry);
//
//        DropDownChoice<EducationLevel> educationLevel = new DropDownChoice<EducationLevel>("educationLevel", educationLevelFacade.getAllRecords(),
//                new ChoiceRenderer<EducationLevel>("title", "educationLevelId") {
//
//                    private static final long serialVersionUID = 1L;
//
//                    @Override
//                    public Object getDisplayValue(EducationLevel object) {
//                        return object.getEducationLevelId() + " " + super.getDisplayValue(object);
//                    }
//
//                });
//
//        educationLevel.setLabel(ResourceUtils.getModel("general.educationlevel"));
//        add(educationLevel);
//
//        List<String> listOfOrgTypes = new ArrayList<String>();
//        listOfOrgTypes.add("Commercial");
//        listOfOrgTypes.add("Non-Commercial");
//
//        DropDownChoice<String> organizationType = new DropDownChoice<String>("organizationType", listOfOrgTypes,
//                new ChoiceRenderer<String>("organizationType") {
//
//                    private static final long serialVersionUID = 1L;
//
//                    @Override
//                    public Object getDisplayValue(String object) {
//                        return object;
//                    }
//
//                });
//
//        organizationType.setRequired(true);
//        organizationType.setLabel(ResourceUtils.getModel("label.organizationType"));
//        add(organizationType);


        SubmitLink submit = new SubmitLink("submit", ResourceUtils.getModel("action.create.account")) {

            private static final long serialVersionUID = 1L;

//            @Override
//            protected void onError(AjaxRequestTarget target, Form<?> form) {
//                target.add(feedback);
//            }

            @Override
            public void onSubmit() {

                RegistrationObject user = RegistrationForm.this.getModelObject();
                user.setPanelPerson(panelPerson.getModelObject());
                // validate captcha via service
                if (captchaService.validateResponseForID(user.getCaptcha(), user.getControlText())) {

                    user.getPanelPerson().setRegistrationDate(new DateTime());
                    if (validation(user)) {
                        personFacade.create(new PersonMapper().convertToEntity(user, new Person()));
                        setResponsePage(ConfirmPage.class, PageParametersUtils.getPageParameters(ConfirmPage.EMAIL, user.getEmail()));
                    }
                    // if captcha is valid but other validation fail - generate new captcha
                    generateCaptchaImageAndPrepareValidation();
                } else {
                    error(ResourceUtils.getString("general.error.registration.captchaInvalid"));
                    generateCaptchaImageAndPrepareValidation();
                }
                //target.add(captchaImage);
                //target.add(feedback);
            }
        };
        add(submit);
    }

    private void generateCaptchaImageAndPrepareValidation() {

        // TODO create own captcha component with using this captcha service.
        String captcha = StringUtils.getCaptchaString();
        getModelObject().setCaptcha(captcha);

        BufferedImage image = captchaService.getImageChallengeForID(captcha, getLocale());
        BufferedDynamicImageResource res = new BufferedDynamicImageResource();
        res.setImage(image);

        if(captchaImage == null)
            captchaImage = new NonCachingImage("captchaImage", res);
        else
            captchaImage.setImageResource(res);

        captchaImage.setOutputMarkupId(true);

    }

    private boolean validation(RegistrationObject user) {

        boolean validate = true;

        if (personFacade.usernameExists(user.getEmail())) {
            error(ResourceUtils.getString("inUse.email"));
            validate = false;
        }

        if (user.getPanelPerson().getDateOfBirth().getTime() >= new Date().getTime()) {
            error(ResourceUtils.getString("invalid.dateOfBirth"));
            validate = false;
        }

        if (!user.isPasswordValid()) {
            error(ResourceUtils.getString("invalid.passwordMatch"));
            validate = false;
        }

        return validate;
    }

}
