package cz.zcu.kiv.eegdatabase.wui.ui.groups;

import cz.zcu.kiv.eegdatabase.wui.components.menu.button.IButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.ui.welcome.WelcomePage;

public enum GroupPageLeftMenu implements IButtonPageMenu {

    LIST_OF_GROUPS(WelcomePage.class, "menuItem.listOfGroups"),
    MY_GROUPS(WelcomePage.class, "menuItem.myGroups"), 
    REQUEST_FOR_GROUP_ROLE(WelcomePage.class, "menuItem.requestForGroupRole"),
    BOOKING_ROOM(WelcomePage.class, "menuItem.bookingRoom"), // TODO check  role - only user and admin
    CREATE_GROUP(WelcomePage.class, "menuItem.createGroup"),
    ;

    private Class<? extends MenuPage> pageClass;
    private String pageTitleKey;

    private GroupPageLeftMenu(Class<? extends MenuPage> pageClass, String pageTitleKey) {
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
