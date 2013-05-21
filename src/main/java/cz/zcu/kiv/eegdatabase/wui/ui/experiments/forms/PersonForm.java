package cz.zcu.kiv.eegdatabase.wui.ui.experiments.forms;

import com.ibm.icu.text.SimpleDateFormat;
import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.StringUtils;
import cz.zcu.kiv.eegdatabase.wui.core.Gender;
import cz.zcu.kiv.eegdatabase.wui.core.Laterality;
import cz.zcu.kiv.eegdatabase.wui.core.educationlevel.EducationLevelFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.AjaxFeedbackUpdatingBehavior;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.GenericModel;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.lang.Classes;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.openjena.atlas.test.Gen;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: Matej Sutr
 * Date: 20.5.13
 * Time: 12:00
 */
public class PersonForm extends Form<Person> {

    @SpringBean
    private EducationLevelFacade educationFacade;

    @SpringBean
    private PersonFacade personFacade;

    private TextField<String> name;
    private TextField<String> surname;
    private DateTextField dateOfBirth;
    private RadioChoice<Character> gender;
    private EmailTextField email;
    private DropDownChoice<Character> laterality;
    private DropDownChoice<EducationLevel> educationLevel;

    private GenericModel<Character> lateralityModel;
    private GenericModel<EducationLevel> educationLevelModel;

    private Character genderForModel = 'M';
    private Character lateralityForModel = 'R';
    private EducationLevel educationLevelForModel = null;

    public PersonForm(String id, final ModalWindow window) {

        super(id, new CompoundPropertyModel<Person>(new Person()));

        add(new Label("addPersonHeader", ResourceUtils.getModel("pageTitle.addPerson")));

        addNameField();
        addSurnameField();
        addDateOfBirthField();

        addGenderRadio();
        addLateralitySelect();
        addEducationSelect();

        addEmailField();
        addPhoneNumber();
        addNoteArea();

        AjaxButton submit = new AjaxButton("submitForm", ResourceUtils.getModel("button.save"), this) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                submitForm(window, target);
            }
        };
        submit.add(new AjaxEventBehavior("onclick") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                submitForm(window, target);
            }
        });
        add(submit);

        add(
                new AjaxButton("closeForm", ResourceUtils.getModel("button.close"), this) {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        window.close(target);
                    }
                }.add(new AjaxEventBehavior("onclick") {
                    @Override
                    protected void onEvent(AjaxRequestTarget target) {
                        window.close(target);
                    }
                })
        );
        setOutputMarkupId(true);
    }

    private void submitForm(ModalWindow window, AjaxRequestTarget target){
        validate();
        validateRequiredEntities();
        if (!hasError()) {
            Person user = PersonForm.this.getModelObject();
            user.setLaterality(lateralityForModel);
            user.setGender(genderForModel);
            user.setEducationLevel(educationLevelForModel);
            user.setAuthority(Util.ROLE_READER);
            personFacade.create(user);
            window.close(target);
        } else
            setResponsePage(getPage()); //refresh
    }

    private void validateRequiredEntities(){

        String nameData = name.getModelObject();
        String surnameData = surname.getModelObject();
        String emailData = email.getModelObject();

        if (nameData == null || nameData.isEmpty())
            name.error(ResourceUtils.getString("required.field"));

        if (surnameData == null || surnameData.isEmpty())
            surname.error(ResourceUtils.getString("required.field"));

        if (dateOfBirth.getModelObject() == null)
            dateOfBirth.error(ResourceUtils.getString("required.date"));

        if (gender.getModelObject() == null)
            gender.error(ResourceUtils.getString("required.field"));

        if(emailData == null || emailData.isEmpty())
            email.error(ResourceUtils.getString("required.field"));
    }


    private void addNameField() {
        name = new TextField<String>("givenname");
        name.add(new PatternValidator(StringUtils.REGEX_ONLY_LETTERS));
        add(name);

        ComponentFeedbackMessageFilter filter = new ComponentFeedbackMessageFilter(name);
        final FeedbackPanel feedback = new FeedbackPanel("nameFeedback", filter);
        feedback.setOutputMarkupId(true);
        name.add(new AjaxFeedbackUpdatingBehavior("onblur", feedback));
        add(feedback);
    }

    private void addSurnameField() {
        surname = new TextField<String>("surname");
        surname.add(new PatternValidator(StringUtils.REGEX_ONLY_LETTERS));
        add(surname);

        ComponentFeedbackMessageFilter filter = new ComponentFeedbackMessageFilter(surname);
        final FeedbackPanel feedback = new FeedbackPanel("surnameFeedback", filter);
        feedback.setOutputMarkupId(true);
        surname.add(new AjaxFeedbackUpdatingBehavior("onblur", feedback));
        add(feedback);
    }

    private void addDateOfBirthField() {
        dateOfBirth = new DateTextField("dateOfBirth", new StyleDateConverter(true)) {
            @Override
            public <C> IConverter<C> getConverter(Class<C> type) {
                return (IConverter<C>) new TimestampConverter();
            }
        };
        add(dateOfBirth);

        ComponentFeedbackMessageFilter filter = new ComponentFeedbackMessageFilter(dateOfBirth);
        final FeedbackPanel feedback = new FeedbackPanel("dateOfBirthFeedback", filter);
        feedback.setOutputMarkupId(true);
        dateOfBirth.add(new AjaxFeedbackUpdatingBehavior("onblur", feedback));
        add(feedback);
    }

    private void addGenderRadio() {
        gender = new RadioChoice<Character>("gender", new Model(genderForModel), Gender.getShortcutList(), new ChoiceRenderer<Character>() {

            private static final long serialVersionUID = 1L;

            @Override
            public Object getDisplayValue(Character object) {
                Gender enumValue = Gender.getGenderByShortcut(object);
                return getString(Classes.simpleName(enumValue.getDeclaringClass()) + '.' + enumValue.name());
            }

        });
        gender.setSuffix("\n");

        gender.add(new AjaxFormChoiceComponentUpdatingBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget ajaxRequestTarget) {
                genderForModel = gender.getModelObject();
            }
        });

        add(gender);

        ComponentFeedbackMessageFilter filter = new ComponentFeedbackMessageFilter(gender);
        final FeedbackPanel feedback = new FeedbackPanel("genderFeedback", filter);
        feedback.setOutputMarkupId(true);
        gender.add(new AjaxFeedbackUpdatingBehavior("onblur", feedback));
        add(feedback);
    }

    private void addLateralitySelect() {
        lateralityModel = new GenericModel<Character>(lateralityForModel);
        laterality = new DropDownChoice<Character>("laterality", lateralityModel, Laterality.getShortcutList(),
                new ChoiceRenderer<Character>() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public Object getDisplayValue(Character object) {
                        Laterality enumValue = Laterality.getLateralityByShortcut(object);
                        return getString(Classes.simpleName(enumValue.getDeclaringClass()) + '.' + enumValue.name());
                    }
                });
        laterality.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget ajaxRequestTarget) {
                lateralityForModel = lateralityModel.getObject();
            }
        });
        add(laterality);
    }

    private void addEducationSelect() {
        educationLevelModel = new GenericModel<EducationLevel>(educationLevelForModel);
        educationLevel = new DropDownChoice<EducationLevel>("educationLevel", educationLevelModel, educationFacade.getAllRecords(),
                new ChoiceRenderer<EducationLevel>("title", "educationLevelId") {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public Object getDisplayValue(EducationLevel object) {
                        return object.getEducationLevelId() + " " + super.getDisplayValue(object);
                    }

                });
        educationLevel.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                educationLevelForModel = educationLevelModel.getObject();
            }
        });
        add(educationLevel);
    }

    private void addEmailField() {
        email = new EmailTextField("username");
        email.add(new UniqueUsernameValidator());

        add(email);

        ComponentFeedbackMessageFilter filter = new ComponentFeedbackMessageFilter(email);
        final FeedbackPanel feedback = new FeedbackPanel("emailFeedback", filter);
        feedback.setOutputMarkupId(true);
        email.add(new AjaxFeedbackUpdatingBehavior("onblur", feedback));
        add(feedback);
    }

    private void addPhoneNumber() {
        TextField<String> phoneNumber = new TextField<String>("phoneNumber");
        phoneNumber.add(new PhoneNumberValidator());
        add(phoneNumber);

        ComponentFeedbackMessageFilter filter = new ComponentFeedbackMessageFilter(phoneNumber);
        final FeedbackPanel feedback = new FeedbackPanel("phoneFeedback", filter);
        feedback.setOutputMarkupId(true);
        phoneNumber.add(new AjaxFeedbackUpdatingBehavior("onblur", feedback));
        add(feedback);
    }

    private void addNoteArea() {
        TextArea<String> note = new TextArea<String>("note");
        note.setLabel(ResourceUtils.getModel("label.note"));
        note.add(StringValidator.maximumLength(255));
        add(note);

        ComponentFeedbackMessageFilter filter = new ComponentFeedbackMessageFilter(note);
        final FeedbackPanel feedback = new FeedbackPanel("noteFeedback", filter);
        feedback.setOutputMarkupId(true);
        note.add(new AjaxFeedbackUpdatingBehavior("onblur", feedback));
        add(feedback);
    }

    private class PhoneNumberValidator implements IValidator<String>{

        @Override
        public void validate(IValidatable<String> validatable) {
            String number = validatable.getValue();
            if(number == null || number.isEmpty())  return;
            try {
                if (number.charAt(0) == '+') {
                    Long.parseLong(number.substring(1));
                } else {
                    Long.parseLong(number);
                }
            } catch (NumberFormatException ex) {
                ValidationError error = new ValidationError();
                error.setMessage(ResourceUtils.getString("invalid.phoneNumber"));
                validatable.error(error);
            }
        }
    }

    private class UniqueUsernameValidator implements IValidator<String>{
        @Override
        public void validate(IValidatable<String> validatable) {
            String userName = validatable.getValue();
            if(userName == null || userName.isEmpty()) return;
            if (personFacade.usernameExists(userName)){
                ValidationError error = new ValidationError();
                error.setMessage(ResourceUtils.getString("inUse.email"));
                validatable.error(error);
            }
        }
    }


    private class TimestampConverter implements IConverter<Timestamp> {

        protected Log log = LogFactory.getLog(getClass());

        private String pattern = "dd/MM/yyyy";
        private String parsePattern = "dd/MM/yyyy";

        public TimestampConverter() { }

        public TimestampConverter(String pattern) {
            this.pattern = pattern;
        }

        @Override
        public Timestamp convertToObject(String value, Locale locale) {
            if (value == null || value.isEmpty()) {
                ValidationError error = new ValidationError();
                error.setMessage(ResourceUtils.getString("required.date"));
                dateOfBirth.error(error);
                error.setMessage(ResourceUtils.getString("form.note.dateFormatDDMMYYYY"));
                dateOfBirth.error(error);
            }
            else {
                try {
                    Timestamp date = new Timestamp(new SimpleDateFormat(parsePattern,
                            EEGDataBaseSession.get().getLocale()).parse(value).getTime());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    cal.add(Calendar.HOUR_OF_DAY, 12);
                    date = new Timestamp((cal.getTime()).getTime());
                    if (date.getTime() <= System.currentTimeMillis())
                        return date;

                    ValidationError error = new ValidationError();
                    error.setMessage(ResourceUtils.getString("invalid.dateOfBirth"));
                    dateOfBirth.error(error);
                    error.setMessage(ResourceUtils.getString("form.note.dateFormatDDMMYYYY"));
                    dateOfBirth.error(error);

                } catch (ParseException e) {

                    ValidationError error = new ValidationError();
                    error.setMessage(ResourceUtils.getString("invalid.dateOfBirth"));
                    dateOfBirth.error(error);
                    error.setMessage(ResourceUtils.getString("form.note.dateFormatDDMMYYYY"));
                    dateOfBirth.error(error);

                    log.error(e.getMessage(), e);
                }
            }
            return null;
        }

        @Override
        public String convertToString(Timestamp value, Locale locale) {
            return new SimpleDateFormat(pattern, EEGDataBaseSession.get().getLocale()).format(value);
        }
    }
}
