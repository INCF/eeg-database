package cz.zcu.kiv.eegdatabase.wui.ui.home;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.ui.security.ForgottenPasswordPage;
import cz.zcu.kiv.eegdatabase.wui.ui.welcome.WelcomePage;

/**
 * Home page of web portal.
 * 
 * @author Jakub Rinkes
 * 
 */
public class HomePage extends MenuPage {

    private static final long serialVersionUID = -1967810037377960414L;

    public HomePage() {

        // if is user logged in redirect on welcome page.
        if (EEGDataBaseSession.get().isSignedIn())
            throw new RestartResponseAtInterceptPageException(WelcomePage.class);

        /*
         *  if is user not logged in but spring session is authenticated - its used social network 
         *  for login and authorize user in wicket. Redirect on welcome page.
         */
        if (EEGDataBaseSession.get().authenticatedSocial()) {
            throw new RestartResponseAtInterceptPageException(WelcomePage.class);
        }
        
        // or show home page with login form.
        setPageTitle(ResourceUtils.getModel("title.page.home"));
        add(new HomeLoginForm("login"));

        add(new BookmarkablePageLink<Void>("forgottenPass", ForgottenPasswordPage.class));
    }

}
