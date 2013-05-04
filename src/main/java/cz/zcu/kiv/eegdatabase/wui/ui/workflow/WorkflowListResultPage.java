package cz.zcu.kiv.eegdatabase.wui.ui.workflow;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.spring.injection.annot.SpringBean;

import cz.zcu.kiv.eegdatabase.data.dao.ServiceResultDao;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.file.FileFacade;

@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class WorkflowListResultPage extends MenuPage  {

	private static final long serialVersionUID = -1610693580181696939L;

	@SpringBean
	ServiceResultDao serviceResult;
	
	public WorkflowListResultPage() {

        setPageTitle(ResourceUtils.getModel("pageTitle.listOfResults"));
        setupComponents();
    }

	private void setupComponents() {
		add(new ButtonPageMenu("leftMenu", WorkflowPageLeftMenu.values()));

		
	}
}
