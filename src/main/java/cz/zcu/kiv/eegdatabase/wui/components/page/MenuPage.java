package cz.zcu.kiv.eegdatabase.wui.components.page;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;

import cz.zcu.kiv.eegdatabase.wui.app.EEGDataBaseApplication;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.feedback.BaseFeedbackMessagePanel;
import cz.zcu.kiv.eegdatabase.wui.components.menu.ddm.MainMenu;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.account.MyAccountPage;
import cz.zcu.kiv.eegdatabase.wui.ui.security.RegistrationPage;

public class MenuPage extends BasePage {

    private static final long serialVersionUID = -9208758802775141621L;

    private BaseFeedbackMessagePanel feedback;

    public MenuPage() {

        feedback = new BaseFeedbackMessagePanel("base_feedback");
        add(feedback);

        boolean signedIn = EEGDataBaseSession.get().isSignedIn();

        String labelMessage;
        Class<?> pageClass;
        String labelLink;

        if (signedIn) {
            labelMessage = ResourceUtils.getString("general.header.logged");
            labelMessage += EEGDataBaseSession.get().getUserName();
            labelLink = ResourceUtils.getString("general.page.myaccount.link");
            pageClass = MyAccountPage.class;
        } else {
            labelMessage = ResourceUtils.getString("general.header.notlogged");
            labelLink = ResourceUtils.getString("action.register");
            pageClass = RegistrationPage.class;
        }

        BookmarkablePageLink headerLink = new BookmarkablePageLink("userHeaderLink", pageClass);
        headerLink.add(new Label("linkLabel", labelLink));
        add(headerLink);

        Link<Void> link = new Link<Void>("logout") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                EEGDataBaseSession.get().invalidate();
                setResponsePage(EEGDataBaseApplication.get().getHomePage());
            }
        };
        link.setVisibilityAllowed(signedIn);
        add(link);

        add(new Label("userLogStatusLabel", labelMessage));

        add(new MainMenu("mainMenu"));

        add(new ExternalLink("footerLink", ResourceUtils.getString("general.footer.link"), ResourceUtils.getString("general.footer.link.title")));
    }

    public BaseFeedbackMessagePanel getFeedback() {
        return feedback;
    }
}
