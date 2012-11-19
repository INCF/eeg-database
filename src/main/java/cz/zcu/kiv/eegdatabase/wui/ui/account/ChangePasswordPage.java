package cz.zcu.kiv.eegdatabase.wui.ui.account;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;

import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;

@AuthorizeInstantiation("ROLE_USER")
public class ChangePasswordPage extends MenuPage {

    private static final long serialVersionUID = -2098428058200654117L;

    public ChangePasswordPage() {
        setPageTitle(ResourceUtils.getModel("pageTitle.changePassword"));
    }
}
