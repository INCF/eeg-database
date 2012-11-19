package cz.zcu.kiv.eegdatabase.wui.ui.account;

import cz.zcu.kiv.eegdatabase.wui.components.menu.button.IButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;

public enum MyAccountPageLeftMenu implements IButtonPageMenu {

    ACCOUNT_OVERVIEW(AccountOverViewPage.class, "menuItem.myAccount.overview"),
    CHANGE_PASSWORD(ChangePasswordPage.class, "menuItem.myAccount.changePassword"),
    SOCIAL(SocialNetworksPage.class, "menuItem.myAccount.social"), ;

    private Class<? extends MenuPage> pageClass;
    private String pageTitleKey;

    private MyAccountPageLeftMenu(Class<? extends MenuPage> pageClass, String pageTitleKey) {
        this.pageClass = pageClass;
        this.pageTitleKey = pageTitleKey;
    }

    @Override
    public Class<? extends MenuPage> getPageClass() {
        return pageClass;
    }

    @Override
    public String getPageTitleKey() {
        return pageTitleKey;
    }

}
