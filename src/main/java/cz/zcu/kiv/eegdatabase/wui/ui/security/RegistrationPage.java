package cz.zcu.kiv.eegdatabase.wui.ui.security;

import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;

/**
 * Page for registration new user.
 * 
 * @author Jakub Rinkes
 *
 */
public class RegistrationPage extends MenuPage {

    private static final long serialVersionUID = -2862552856622208298L;

    public RegistrationPage() {

        add(new RegistrationForm("registration", getFeedback()));

        setPageTitle(ResourceUtils.getModel("title.page.registration"));
    }
}
