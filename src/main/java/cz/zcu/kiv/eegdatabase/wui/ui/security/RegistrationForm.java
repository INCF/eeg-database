package cz.zcu.kiv.eegdatabase.wui.ui.security;

import java.util.Arrays;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.captcha.CaptchaImageResource;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.joda.time.DateTime;

import com.googlecode.wicket.jquery.ui.form.datepicker.DatePicker;

import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.StringUtils;
import cz.zcu.kiv.eegdatabase.wui.core.Gender;
import cz.zcu.kiv.eegdatabase.wui.core.dto.FullPersonDTO;
import cz.zcu.kiv.eegdatabase.wui.core.educationlevel.EducationLevelDTO;
import cz.zcu.kiv.eegdatabase.wui.core.educationlevel.EducationLevelFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonMapper;

public class RegistrationForm extends Form<FullPersonDTO> {

    private static final long serialVersionUID = 4973918066620014022L;

    @SpringBean
    EducationLevelFacade educationLevelFacade;

    @SpringBean
    PersonFacade personFacade;

    private String captchaComponentPath;

    public RegistrationForm(String id, final FeedbackPanel feedback) {
        super(id, new CompoundPropertyModel<FullPersonDTO>(new FullPersonDTO()));

        TextField<String> name = new TextField<String>("name");
        name.setLabel(ResourceUtils.getModel("general.name"));
        name.setRequired(true);
        name.add(new PatternValidator(StringUtils.REGEX_ONLY_LETTERS));
        FormComponentLabel nameLabel = new FormComponentLabel("nameLb", name);
        add(name, nameLabel);

        TextField<String> surname = new TextField<String>("surname");
        surname.setLabel(ResourceUtils.getModel("general.surname"));
        surname.setRequired(true);
        surname.add(new PatternValidator(StringUtils.REGEX_ONLY_LETTERS));
        FormComponentLabel surnameLabel = new FormComponentLabel("surnameLb", surname);
        add(surname, surnameLabel);

        DatePicker date = new DatePicker("dateOfBirth");
        date.setLabel(ResourceUtils.getModel("general.dateofbirth"));
        date.setRequired(true);
        FormComponentLabel dateLabel = new FormComponentLabel("dateLb", date);
        add(date, dateLabel);

        EmailTextField email = new EmailTextField("email");
        email.setLabel(ResourceUtils.getModel("general.email"));
        email.setRequired(true);
        FormComponentLabel emailLabel = new FormComponentLabel("emailLb", email);
        add(email, emailLabel);

        PasswordTextField password = new PasswordTextField("password");
        password.setLabel(ResourceUtils.getModel("general.password"));
        password.setRequired(true);
        password.add(StringValidator.minimumLength(6));
        FormComponentLabel passLabel = new FormComponentLabel("passLb", password);
        add(password, passLabel);

        PasswordTextField passwordVerify = new PasswordTextField("passwordVerify");
        passwordVerify.setLabel(ResourceUtils.getModel("general.password.verify"));
        passwordVerify.setRequired(true);
        passwordVerify.add(StringValidator.minimumLength(6));
        FormComponentLabel pass2Label = new FormComponentLabel("passVerLb", passwordVerify);
        add(passwordVerify, pass2Label);

        String captcha = StringUtils.getCaptchaString();
        getModelObject().setCaptcha(captcha);

        CaptchaImageResource imageResource = new CaptchaImageResource(captcha);
        Image captchaImage = new Image("captchaImage", imageResource);
        captchaComponentPath = captchaImage.getPath();
        add(captchaImage);

        TextField<String> controlText = new TextField<String>("controlText");
        surname.setLabel(ResourceUtils.getModel("general.controlText"));
        surname.setRequired(true);
        FormComponentLabel controlTextLabel = new FormComponentLabel("controlTextLb", surname);
        add(controlText, controlTextLabel);

        RadioChoice<Gender> gender = new RadioChoice<Gender>("gender", Arrays.asList(Gender.values()), new EnumChoiceRenderer<Gender>());
        gender.setSuffix("\n");
        gender.setRequired(true);
        gender.setLabel(ResourceUtils.getModel("general.gender"));
        FormComponentLabel genderLabel = new FormComponentLabel("genderLb", gender);
        add(gender, genderLabel);

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
        FormComponentLabel educationLevelLabel = new FormComponentLabel("educationLevelLb", educationLevel);
        add(educationLevel, educationLevelLabel);


        AjaxButton submit = new AjaxButton("submit", ResourceUtils.getModel("action.create.account"), this) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedback);
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                FullPersonDTO user = RegistrationForm.this.getModelObject();
                if (user.isCaptchaValid()) {

                    user.setRegistrationDate(new DateTime());
                    if (validation(user)) {
                        personFacade.create(new PersonMapper().convertToEntity(user, new Person()));
                        setResponsePage(ConfirmPage.class, PageParametersUtils.getPageParameters(ConfirmPage.EMAIL, user.getEmail()));
                    }
                } else {
                    error(ResourceUtils.getString("general.error.registration.captchaInvalid"));
                }
                target.add(feedback);
            }
        };
        add(submit);
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        if (captchaComponentPath != null) {
            String captcha = StringUtils.getCaptchaString();
            getModelObject().setCaptcha(captcha);

            Image captchaImage = (Image) get(captchaComponentPath);
            captchaImage.setImageResource(new CaptchaImageResource(captcha));
        }
    }

    private boolean validation(FullPersonDTO user) {

        boolean validate = true;

        if (personFacade.usernameExists(user.getEmail())) {
            error(ResourceUtils.getString("inUse.email"));
            validate = false;
        }

        if (user.getDateOfBirth().getTime() >= System.currentTimeMillis()) {
            error(ResourceUtils.getString("invalid.dateOfBirth"));
            validate = false;
        }

        if (user.isPasswordValid()) {
            error(ResourceUtils.getString("invalid.passwordMatch"));
        }

        return validate;
    }

}
