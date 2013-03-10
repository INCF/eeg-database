package cz.zcu.kiv.eegdatabase.wui.ui.home;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.ui.security.ForgottenPasswordPage;
import cz.zcu.kiv.eegdatabase.wui.ui.welcome.WelcomePage;

public class HomePage extends MenuPage {

    private static final long serialVersionUID = -1967810037377960414L;

    public HomePage() {

        if (EEGDataBaseSession.get().isSignedIn())
            throw new RestartResponseAtInterceptPageException(WelcomePage.class);

        if (EEGDataBaseSession.get().authenticatedSocial()) {
            throw new RestartResponseAtInterceptPageException(WelcomePage.class);
        }

        setPageTitle(ResourceUtils.getModel("title.page.home"));
        add(new HomeLoginForm("login"));

        add(new BookmarkablePageLink<Void>("forgottenPass", ForgottenPasswordPage.class));
    }

}
