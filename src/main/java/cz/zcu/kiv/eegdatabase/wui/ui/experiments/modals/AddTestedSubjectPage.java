package cz.zcu.kiv.eegdatabase.wui.ui.experiments.modals;

import com.googlecode.wicket.jquery.ui.form.datepicker.DatePicker;
import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.wui.components.table.TimestampConverter;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.StringUtils;
import cz.zcu.kiv.eegdatabase.wui.core.Gender;
import cz.zcu.kiv.eegdatabase.wui.core.educationlevel.EducationLevelFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.lang.Classes;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 3.4.13
 * Time: 19:33
 */
public class AddTestedSubjectPage extends WebPage {

    @SpringBean
    private EducationLevelFacade educationFacade;

    @SpringBean
    private PersonFacade personFacade;

    public AddTestedSubjectPage(final PageReference modalWindowPage,
                                final ModalWindow window) {

        AddTestedSubjectForm form = new AddTestedSubjectForm("addForm", window);
        add(form);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(CssHeaderItem.forUrl("/files/wizard-style.css"));
        super.renderHead(response);
    }

    private class AddTestedSubjectForm extends Form<Person> {

        public AddTestedSubjectForm(String id, final ModalWindow window) {
            super(id, new CompoundPropertyModel<Person>(new Person()));

            final FeedbackPanel feedback = new FeedbackPanel("feedback");
            feedback.setOutputMarkupId(true);
            add(feedback);

            add(new Label("addPersonHeader", "Add tested subject"));

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

            /* TODO not implemented in prototype - should be discussed
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
            */

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

            AjaxButton submit = new AjaxButton("submitForm", ResourceUtils.getModel("button.save"), this) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedback);
                }

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    Person user = AddTestedSubjectForm.this.getModelObject();

                    if (validation(user, personFacade)) {
//                        user.setAuthority(Util.ROLE_READER);
                        personFacade.create(user);
                        window.close(target);
                    }
                    target.add(feedback);
                }
            };
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
            AjaxFormValidatingBehavior.addToAllFormComponents(this, "focus", Duration.ONE_SECOND);
        }

        private boolean validation(Person user, PersonFacade facade) {
            boolean validate = true;

            if (facade.usernameExists(user.getEmail())) {
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