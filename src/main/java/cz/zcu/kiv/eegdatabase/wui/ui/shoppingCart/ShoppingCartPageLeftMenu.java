package cz.zcu.kiv.eegdatabase.wui.ui.shoppingCart;

import cz.zcu.kiv.eegdatabase.wui.components.menu.button.IButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Created with IntelliJ IDEA.
 * User: jfronek
 * Date: 21.3.13
 * Time: 18:21
 * To change this template use File | Settings | File Templates.
 */
public enum ShoppingCartPageLeftMenu implements IButtonPageMenu {

    MY_CART(ShoppingCartPage.class, "pageTitle.myCart", null),
    MY_DOWNLOADS(MyDownloadsPage.class, "pageTitle.myDownloads", null),
    ;

    private Class<? extends MenuPage> pageClass;
    private String pageTitleKey;
    private PageParameters parameters;

    private ShoppingCartPageLeftMenu(Class<? extends MenuPage> pageClass, String pageTitleKey, PageParameters parameters) {
        this.pageClass = pageClass;
        this.pageTitleKey = pageTitleKey;
        this.parameters = parameters;
    }

    @Override
    public Class<? extends MenuPage> getPageClass() {
        return pageClass;
    }

    @Override
    public String getPageTitleKey() {
        return pageTitleKey;
    }

    @Override
    public PageParameters getPageParameters() {
        return parameters;
    }
}
