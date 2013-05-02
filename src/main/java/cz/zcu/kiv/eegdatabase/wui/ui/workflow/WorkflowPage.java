package cz.zcu.kiv.eegdatabase.wui.ui.workflow;

import java.util.List;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.spring.injection.annot.SpringBean;


import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.logic.workflow.WorkflowService;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.page.UnderConstructPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsFacade;
import cz.zcu.kiv.eegdatabase.wui.core.file.FileFacade;

@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER",
		"ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class WorkflowPage extends MenuPage {

	private static final long serialVersionUID = -1967810037377960414L;

	@SpringBean
	ExperimentsFacade facade;

	@SpringBean
	FileFacade fileFacade;
	
	WorkflowService service = new WorkflowService();

	public WorkflowPage() {

		service.storeWorkflow();
		setFiles(134);

		setPageTitle(ResourceUtils.getModel("title.page.workflow"));

		throw new RestartResponseAtInterceptPageException(
				UnderConstructPage.class);
	}

	private void setFiles(int experimentID) {
		List<DataFile> data = facade.getDataFilesWhereExpId(experimentID);
		int[] pole = new int[data.size()];
		for (int a = 0; a < pole.length; a++) {
			if (data.get(a).getFilename().endsWith(".eeg")
					|| data.get(a).getFilename().endsWith(".vhdr")
					|| data.get(a).getFilename().endsWith(".vmrk")){
				pole[a] = data.get(a).getDataFileId();
			}
		}
		service.runService(pole, fileFacade);

	}
}
