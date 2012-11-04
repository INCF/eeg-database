package cz.zcu.kiv.eegdatabase.wui.ui.lists;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;

import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;

@AuthorizeInstantiation("ROLE_USER")
public class ListsPage extends MenuPage {
    
    private static final long serialVersionUID = -1967810037377960414L;

    public ListsPage() {
        
        setPageTitle(ResourceUtils.getModel("title.page.lists"));
    }
}
