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
 *   HomePage.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.home;

import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.ui.security.ForgottenPasswordPage;
import cz.zcu.kiv.eegdatabase.wui.ui.security.RegistrationPage;
import cz.zcu.kiv.eegdatabase.wui.ui.welcome.WelcomePage;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;

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
        add(new BookmarkablePageLink<Void>("registerLink", RegistrationPage.class));
//        add(new ExternalLink("licenseLink1", ResourceUtils.getString("homePage.license.link1")));
//        add(new ExternalLink("EEGBaseLink", ResourceUtils.getString("homePage.license.EEGBaseLink"), ResourceUtils.getString("homePage.license.EEGBaseLink")));
//        add(new ExternalLink("licenseLink2", ResourceUtils.getString("homePage.license.link1"), ResourceUtils.getString("homePage.license.linkText2") + "."));
        add(new ExternalLink("githubLink", ResourceUtils.getString("homePage.license.githubLink"), ResourceUtils.getString("homePage.license.githubLink") + "."));
    }
}
