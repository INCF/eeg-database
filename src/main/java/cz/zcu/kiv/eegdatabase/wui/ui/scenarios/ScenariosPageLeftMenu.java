package cz.zcu.kiv.eegdatabase.wui.ui.scenarios;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import cz.zcu.kiv.eegdatabase.wui.components.menu.button.IButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.ui.welcome.WelcomePage;

public enum ScenariosPageLeftMenu implements IButtonPageMenu {

    LIST_OF_SCENARIOS(WelcomePage.class, "menuItem.listOfScenarios", null),
    MY_SCENARIOS(WelcomePage.class, "menuItem.myScenarios", null),
    SEARCH_SCENARIOS(WelcomePage.class, "menuItem.searchScenario", null),
    ADD_SCENARIOS(WelcomePage.class, "menuItem.addScenario", null),
    ADD_SCENARIOS_SCHEMA(WelcomePage.class, "menuItem.addScenarioSchema", null), ;

    private Class<? extends MenuPage> pageClass;
    private String pageTitleKey;
    private PageParameters parameters;

    private ScenariosPageLeftMenu(Class<? extends MenuPage> pageClass, String pageTitleKey, PageParameters parameters) {
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
