package cz.zcu.kiv.eegdatabase.wui.ui.welcome;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;

@AuthorizeInstantiation(value = { "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class WelcomePage extends MenuPage {

    private static final long serialVersionUID = -1967810037377960414L;

    public WelcomePage() {
    }

    public WelcomePage(PageParameters parameters) {
        
    }
}
