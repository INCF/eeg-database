package cz.zcu.kiv.eegdatabase.wui.ui.lists;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;

import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;

/**
 * Page with link on lists with parameters and definitions.
 * 
 * @author Jakub Rinkes
 *
 */
@AuthorizeInstantiation(value = { "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ListListsPage extends MenuPage {
    
    private static final long serialVersionUID = -1967810037377960414L;

    public ListListsPage() {
        
        setPageTitle(ResourceUtils.getModel("title.page.lists"));
        add(new ButtonPageMenu("leftMenu", ListsLeftPageMenu.values()));
    }
}
