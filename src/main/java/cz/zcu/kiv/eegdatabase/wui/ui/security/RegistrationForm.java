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

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Date;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.image.resource.BufferedDynamicImageResource;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.joda.time.DateTime;

import com.octo.captcha.service.image.ImageCaptchaService;

import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.wui.components.form.input.DateTimeFieldPicker;
import cz.zcu.kiv.eegdatabase.wui.components.table.TimestampConverter;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.StringUtils;
import cz.zcu.kiv.eegdatabase.wui.core.Gender;
import cz.zcu.kiv.eegdatabase.wui.core.dto.FullPersonDTO;
import cz.zcu.kiv.eegdatabase.wui.core.educationlevel.EducationLevelFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonMapper;

/**
 * Form for registration new user.
 * 
 * @author Jakub Rinkes
 *
 */
public class RegistrationForm extends Form<FullPersonDTO> {

    private static final long serialVersionUID = 4973918066620014022L;
    
    @SpringBean
    EducationLevelFacade educationLevelFacade;

    @SpringBean
    PersonFacade personFacade;
    
    @SpringBean
    ImageCaptchaService captchaService;

    private NonCachingImage captchaImage;

    public RegistrationForm(String id, final FeedbackPanel feedback) {
        super(id, new CompoundPropertyModel<FullPersonDTO>(new FullPersonDTO()));

        TextField<String> name = new TextField<String>("name");
        name.setLabel(ResourceUtils.getModel("general.name"));
        name.setRequired(true);
        name.add(new PatternValidator(StringUtils.REGEX_ONLY_LETTERS));
        add(name);

        TextField<String> surname = new TextField<String>("surname");
        surname.setLabel(ResourceUtils.getModel("general.surname"));
        surname.setRequired(true);
        surname.add(new PatternValidator(StringUtils.REGEX_ONLY_LETTERS));
        add(surname);

        DateTimeFieldPicker date = new DateTimeFieldPicker("dateOfBirth") {

            private static final long serialVersionUID = 1L;

            @Override
            public <C> IConverter<C> getConverter(Class<C> type) {
                return (IConverter<C>) new TimestampConverter();
            }
        };
        date.setLabel(ResourceUtils.getModel("general.dateofbirth"));
        date.setRequired(true);
        add(date);

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

        generateCaptchaImageAndPrepareValidation();
        add(captchaImage);

        TextField<String> controlText = new TextField<String>("controlText");
        controlText.setLabel(ResourceUtils.getModel("general.controlText"));
        controlText.setRequired(true);
        add(controlText);

        RadioChoice<Gender> gender = new RadioChoice<Gender>("gender", Arrays.asList(Gender.values()), new EnumChoiceRenderer<Gender>());
        gender.setSuffix("\n");
        gender.setRequired(true);
        gender.setLabel(ResourceUtils.getModel("general.gender"));
        add(gender);

        DropDownChoice<EducationLevel> educationLevel = new DropDownChoice<EducationLevel>("educationLevel", educationLevelFacade.getAllRecords(),
                new ChoiceRenderer<EducationLevel>("title", "educationLevelId") {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public Object getDisplayValue(EducationLevel object) {
                        return object.getEducationLevelId() + " " + super.getDisplayValue(object);
                    }

                });

        educationLevel.setRequired(true);
        educationLevel.setLabel(ResourceUtils.getModel("general.educationlevel"));
        add(educationLevel);


        AjaxButton submit = new AjaxButton("submit", ResourceUtils.getModel("action.create.account"), this) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedback);
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                
                FullPersonDTO user = RegistrationForm.this.getModelObject();
                // validate captcha via service
                if (captchaService.validateResponseForID(user.getCaptcha(), user.getControlText())) {

                    user.setRegistrationDate(new DateTime());
                    if (validation(user)) {
                        personFacade.create(new PersonMapper().convertToEntity(user, new Person()));
                        setResponsePage(ConfirmPage.class, PageParametersUtils.getPageParameters(ConfirmPage.EMAIL, user.getEmail()));
                    }
                    // if captcha is valid but other validation fail - generate new captcha
                    generateCaptchaImageAndPrepareValidation();
                } else {
                    error(ResourceUtils.getString("general.error.registration.captchaInvalid"));
                }
                target.add(captchaImage);
                target.add(feedback);
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
    
    private boolean validation(FullPersonDTO user) {

        boolean validate = true;

        if (personFacade.usernameExists(user.getEmail())) {
            error(ResourceUtils.getString("inUse.email"));
            validate = false;
        }

        if (user.getDateOfBirth().getTime() >= new Date().getTime()) {
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
