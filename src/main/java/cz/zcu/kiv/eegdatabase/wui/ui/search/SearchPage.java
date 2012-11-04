package cz.zcu.kiv.eegdatabase.wui.ui.search;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;

import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;

@AuthorizeInstantiation("ROLE_USER")
public class SearchPage extends MenuPage {
    
    private static final long serialVersionUID = -1967810037377960414L;

    public SearchPage() {
        
        setPageTitle(ResourceUtils.getModel("title.page.search"));
    }
}
