package cz.zcu.kiv.eegdatabase.wui.ui.groups;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import cz.zcu.kiv.eegdatabase.wui.components.menu.button.IButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.ui.articles.ArticlesPage;
import cz.zcu.kiv.eegdatabase.wui.ui.welcome.WelcomePage;

public enum GroupPageLeftMenu implements IButtonPageMenu {

    LIST_OF_GROUPS(ArticlesPage.class, "menuItem.listOfGroups", null),
    MY_GROUPS(WelcomePage.class, "menuItem.myGroups", null),
    REQUEST_FOR_GROUP_ROLE(WelcomePage.class, "menuItem.requestForGroupRole", null),
    BOOKING_ROOM(WelcomePage.class, "menuItem.bookingRoom", null),
    CREATE_GROUP(WelcomePage.class, "menuItem.createGroup", null), ;

    private Class<? extends MenuPage> pageClass;
    private String pageTitleKey;
    private PageParameters parameters;

    private GroupPageLeftMenu(Class<? extends MenuPage> pageClass, String pageTitleKey, PageParameters parameters) {
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
