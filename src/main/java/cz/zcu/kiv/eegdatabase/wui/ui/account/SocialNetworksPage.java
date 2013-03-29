package cz.zcu.kiv.eegdatabase.wui.ui.account;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;

import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.page.UnderConstructPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;

@AuthorizeInstantiation(value = { "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class SocialNetworksPage extends MenuPage {

    private static final long serialVersionUID = -5514036024012232250L;

    public SocialNetworksPage() {
        setPageTitle(ResourceUtils.getModel("pageTitle.socialOverview"));

        throw new RestartResponseAtInterceptPageException(UnderConstructPage.class);
    }
}
