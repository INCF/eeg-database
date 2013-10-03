package cz.zcu.kiv.eegdatabase.wui.ui.administration;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import cz.zcu.kiv.eegdatabase.wui.components.menu.button.IButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;

/**
 * Enumeration of left page menu for administration section.
 * @author Jakub Rinkes
 *
 */
public enum AdministrationPageLeftMenu implements IButtonPageMenu {

    CHANGE_USER_ROLE_PAGE(ChangeUserRolePage.class, "menuItem.changeUserRole", null),
	MANAGE_RESEARCH_GROUP_PAGE(ManageResearchGroupPage.class, "menuItem.manageResearchGroup", null)
    ;

    private Class<? extends MenuPage> pageClass;
    private String pageTitleKey;
    private PageParameters parameters;

    private AdministrationPageLeftMenu(Class<? extends MenuPage> pageClass, String pageTitleKey, PageParameters parameters) {
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
