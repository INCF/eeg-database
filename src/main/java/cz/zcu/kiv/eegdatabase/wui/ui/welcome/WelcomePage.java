package cz.zcu.kiv.eegdatabase.wui.ui.welcome;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;

import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;

@AuthorizeInstantiation("ROLE_USER")
public class WelcomePage extends MenuPage {
    
    private static final long serialVersionUID = -1967810037377960414L;

    public WelcomePage() {
        
        
    }
}
