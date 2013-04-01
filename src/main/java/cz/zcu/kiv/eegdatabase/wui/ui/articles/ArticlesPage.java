package cz.zcu.kiv.eegdatabase.wui.ui.articles;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;

import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.page.UnderConstructPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;

@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ArticlesPage extends MenuPage {

    private static final long serialVersionUID = -1967810037377960414L;

    public ArticlesPage() {
        
        setPageTitle(ResourceUtils.getModel("title.page.articles"));
        
        throw new RestartResponseAtInterceptPageException(UnderConstructPage.class);
    }
}
