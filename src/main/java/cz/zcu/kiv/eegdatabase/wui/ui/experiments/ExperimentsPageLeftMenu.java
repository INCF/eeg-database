package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import cz.zcu.kiv.eegdatabase.wui.components.menu.button.IButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;

public enum ExperimentsPageLeftMenu implements IButtonPageMenu {

    LIST_OF_EXPERIMENTS(ListExperimentsPage.class, "menuItem.experiments.allExperiments", null),
    LIST_OF_EXPERIMENTS_AS_OWNER(ListExperimentsPage.class, "menuItem.experiments.myExperiments", PageParametersUtils.getDefaultPageParameters(ListExperimentsPage.PARAM_OWNER)),
    LIST_OF_EXPERIMENTS_AS_SUBJECT(ListExperimentsPage.class, "menuItem.experiments.meAsSubject", PageParametersUtils.getDefaultPageParameters(ListExperimentsPage.PARAM_SUBJECT)),
    SEARCH(ListExperimentsPage.class, "menuItem.searchMeasuration", null),
    // ADD_EXPERIMENTS(ListExperimentsPage.class,
    // "menuItem.experiments.meAsSubject", null),
    RESULT(ListExperimentsPage.class, "menuItem.serviceResult", null),

    ;

    private Class<? extends MenuPage> pageClass;
    private String pageTitleKey;
    private PageParameters parameters;

    private ExperimentsPageLeftMenu(Class<? extends MenuPage> pageClass, String pageTitleKey, PageParameters parameters) {
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
