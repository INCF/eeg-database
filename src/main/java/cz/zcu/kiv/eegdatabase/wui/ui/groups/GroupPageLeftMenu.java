package cz.zcu.kiv.eegdatabase.wui.ui.groups;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import cz.zcu.kiv.eegdatabase.wui.components.menu.button.IButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.page.UnderConstructPage;
import cz.zcu.kiv.eegdatabase.wui.ui.groups.form.ResearchGroupFormPage;
import cz.zcu.kiv.eegdatabase.wui.ui.groups.role.GroupRoleRequestPage;

public enum GroupPageLeftMenu implements IButtonPageMenu {

    LIST_OF_GROUPS(ListResearchGroupsPage.class, "menuItem.listOfGroups", null),
    MY_GROUPS(MyGroupsPage.class, "menuItem.myGroups", null),
    REQUEST_FOR_GROUP_ROLE(GroupRoleRequestPage.class, "menuItem.requestForGroupRole", null),
    BOOKING_ROOM(UnderConstructPage.class, "menuItem.bookingRoom", null),
    CREATE_GROUP(ResearchGroupFormPage.class, "menuItem.createGroup", null), ;

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
