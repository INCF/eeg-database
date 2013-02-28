package cz.zcu.kiv.eegdatabase.wui.ui.security;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;

public class ForgottenPasswordPage extends MenuPage {

    private static final long serialVersionUID = 8777241488694645558L;

    @SpringBean
    PersonFacade personFacade;

    public ForgottenPasswordPage() {
        setPageTitle(ResourceUtils.getModel("pageTitle.forgottenPassword"));
        add(new ForgottenForm("form", getFeedback()));
    }

    class ForgottenForm extends Form<Person> {

        private static final long serialVersionUID = 1L;

        public ForgottenForm(String id, final FeedbackPanel feedback) {
            super(id, new CompoundPropertyModel<Person>(new Person()));

            final FormComponent<String> username = new EmailTextField("username");
            username.setRequired(true);

            final AjaxButton submit = new AjaxButton("submit", ResourceUtils.getModel("button.send"), this) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                    Person person = ForgottenForm.this.getModelObject();

                    if (personFacade.usernameExists(person.getUsername())) {
                        personFacade.forgottenPassword(person);

                        info(ResourceUtils.getString("pageTitle.forgottenPasswordSuccess"));
                        username.setVisibilityAllowed(false);
                        this.setVisibilityAllowed(false);
                    } else {

                        error(ResourceUtils.getString("pageTitle.forgottenPasswordFailed"));
                    }

                    target.add(this);
                    target.add(username);
                    target.add(feedback);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedback);
                }

            };
            username.setOutputMarkupId(true);
            submit.setOutputMarkupId(true);
            add(username, submit);
        }
    }
}
