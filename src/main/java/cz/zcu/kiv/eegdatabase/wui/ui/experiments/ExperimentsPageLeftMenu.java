package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import cz.zcu.kiv.eegdatabase.wui.components.menu.button.IButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.page.UnderConstructPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.ui.licenses.GrantedLicensesPage;
import cz.zcu.kiv.eegdatabase.wui.ui.licenses.ManageLicenseRequestsPage;

/**
 * Enumeration of left menu page for experiment section.
 * 
 * @author Jakub Rinkes
 *
 */
public enum ExperimentsPageLeftMenu implements IButtonPageMenu {

    LIST_OF_EXPERIMENTS(ListExperimentsByPackagePage.class, "menuItem.experiments.allExperiments", null),
    LIST_OF_EXPERIMENTS_AS_OWNER(ListExperimentsPage.class, "menuItem.experiments.myExperiments", PageParametersUtils.getDefaultPageParameters(ListExperimentsPage.PARAM_OWNER)),
    LIST_OF_EXPERIMENTS_AS_SUBJECT(ListExperimentsPage.class, "menuItem.experiments.meAsSubject", PageParametersUtils.getDefaultPageParameters(ListExperimentsPage.PARAM_SUBJECT)),
    MANAGE_EXPERIMENT_PACKAGES(ManageExperimentPackagesPage.class, "menuItem.experiments.packages", null),
	MANAGE_LICENSE_REQUESTS(ManageLicenseRequestsPage.class, "menuItem.experiments.licenses.requests", null),
	LIST_GRANTED_LICENSES(GrantedLicensesPage.class, "menuItem.experiments.licenses.granted", null),
    SEARCH(UnderConstructPage.class, "menuItem.searchMeasuration", null),
    ADD_EXPERIMENTS(WizardTabbedPanelPage.class, "menuItem.experiments.addExperiment", null),
    RESULT(UnderConstructPage.class, "menuItem.serviceResult", null),

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
