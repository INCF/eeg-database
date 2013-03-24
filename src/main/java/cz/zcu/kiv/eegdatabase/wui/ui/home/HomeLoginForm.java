package cz.zcu.kiv.eegdatabase.wui.ui.home;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.security.LoginUserDTO;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.welcome.WelcomePage;

public class HomeLoginForm extends Form<LoginUserDTO> {

    private static final long serialVersionUID = -5196364867691352802L;

    @SpringBean
    SecurityFacade secFacade;

    public HomeLoginForm(String id) {
        super(id, new CompoundPropertyModel<LoginUserDTO>(new LoginUserDTO()));

        TextField<String> userName = new TextField<String>("userName");
        userName.setRequired(true);

        add(userName);

        PasswordTextField password = new PasswordTextField("password");
        password.add(StringValidator.minimumLength(6));
        password.setRequired(true);

        add(password);

        Button submit = new Button("submit", ResourceUtils.getModel("action.login")) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                LoginUserDTO object = HomeLoginForm.this.getModelObject();
                if (secFacade.authorization(object.getUserName().toLowerCase(), object.getPassword())) {
                    continueToOriginalDestination();
                    setResponsePage(WelcomePage.class);

                } else {
                    error("User cannot be log in.");
                }
            }
        };
        add(submit);
    }

}
