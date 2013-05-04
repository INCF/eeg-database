package cz.zcu.kiv.eegdatabase.wui.ui.workflow;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import cz.zcu.kiv.eegdatabase.wui.components.menu.button.IButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;


public enum WorkflowPageLeftMenu implements IButtonPageMenu {

	LIST_OF_WORKFLOW_RESULTS(WorkflowListResultPage.class, "menuItem.workflow.result.list", null),
	ADD_NEW_WORKFLOW(WorkflowFormPage.class, "menuItem.workflow.add.new", null),
	
    ;

    private Class<? extends MenuPage> pageClass;
    private String pageTitleKey;
    private PageParameters parameters;

    private WorkflowPageLeftMenu(Class<? extends MenuPage> pageClass, String pageTitleKey, PageParameters parameters) {
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
