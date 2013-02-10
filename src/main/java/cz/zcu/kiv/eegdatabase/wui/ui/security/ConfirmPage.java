package cz.zcu.kiv.eegdatabase.wui.ui.security;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.home.HomePage;

public class ConfirmPage extends MenuPage {

    private static final long serialVersionUID = 3478535046128166026L;

    public static final String CONFIRM_ACTIVATION = "activation";
    public static final String EMAIL = "email";

    @SpringBean
    PersonFacade personFacade;

    private final String TITLE_ID = "title";
    private final String MESSAGE_ID = "message";
    private final String MESSAGE_2_ID = "message2";

    public ConfirmPage() {

        setPageTitle();
        setupConfirmation(PageParametersUtils.getPageParameters(EMAIL, "email@sezn.cz"));
    }

    public ConfirmPage(PageParameters parameters) {
        setPageTitle();
        setupConfirmation(parameters);
    }

    private void setPageTitle() {
        setPageTitle(ResourceUtils.getModel("title.page.confirm"));
    }

    private void setupConfirmation(PageParameters parameters) {

        boolean confirm = false;
        IModel<String> titleText;
        IModel<String> messageText;
        IModel<String> message2Text;

        if (!parameters.get(EMAIL).isNull()) {
            titleText = ResourceUtils.getModel("pageTitle.registrationSuccessfull");
            messageText = new Model<String>(ResourceUtils.getString("text.registrationSuccessfull.youNeedConfirm") + parameters.get(EMAIL).toString());
            message2Text = messageText;

        } else {

            if (parameters.get(CONFIRM_ACTIVATION).isNull() || parameters.get(CONFIRM_ACTIVATION).isEmpty()) {
                throw new RestartResponseAtInterceptPageException(HomePage.class);
            }

            String activationHashCode = parameters.get(CONFIRM_ACTIVATION).toString();

            Person person = personFacade.getPersonByHash(activationHashCode);
            if (person == null) {
                titleText = ResourceUtils.getModel("pageTitle.registrationFalse");
                messageText = ResourceUtils.getModel("text.registrationExpired");
                message2Text = messageText;
            } else if (person.isConfirmed()) {
                titleText = ResourceUtils.getModel("pageTitle.confirmationRepeated");
                messageText = ResourceUtils.getModel("text.registrationConfirmedOnce");
                message2Text = messageText;
            } else if (confirmedInTime(System.currentTimeMillis(), person)) {
                confirm = true;
                person.setConfirmed(true);
                personFacade.update(person);
                titleText = ResourceUtils.getModel("pageTitle.confirmationSuccessfull");
                messageText = ResourceUtils.getModel("text.registrationSuccessfull.youCanLogIn.part1");
                message2Text = ResourceUtils.getModel("text.registrationSuccessfull.youCanLogIn.part2");
            } else {
                personFacade.delete(person);
                titleText = ResourceUtils.getModel("pageTitle.registrationFalse");
                messageText = ResourceUtils.getModel("text.registrationExpired");
                message2Text = messageText;
            }
        }

        Label title = new Label(TITLE_ID, titleText);
        Label message = new Label(MESSAGE_ID, messageText);
        Label message2 = new Label(MESSAGE_2_ID, message2Text);
        Link<Void> link = new Link<Void>("link") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                setResponsePage(HomePage.class);
            }
        };
        add(title, message, message2, link);
        link.setVisibilityAllowed(confirm);
        message2.setVisibilityAllowed(confirm);

    }

    private boolean confirmedInTime(long clickTime, Person person) {
        long requestTime = person.getRegistrationDate().getTime();
        // 8 days in ms
        long maximumDelay = 691200000;
        return (clickTime - requestTime < maximumDelay);
    }
}
