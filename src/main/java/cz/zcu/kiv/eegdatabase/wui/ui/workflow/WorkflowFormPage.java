package cz.zcu.kiv.eegdatabase.wui.ui.workflow;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.spring.injection.annot.SpringBean;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.logic.workflow.WorkflowService;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsFacade;
import cz.zcu.kiv.eegdatabase.wui.core.file.FileFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.ExperimentsPageLeftMenu;

@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER",
		"ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class WorkflowFormPage extends MenuPage {

	private static final long serialVersionUID = -1967810037377960414L;

	@SpringBean
	ExperimentsFacade facade;

	@SpringBean
	FileFacade fileFacade;
	
	@SpringBean
	WorkflowService workflowService;
	
	private List<String> fileNames = new ArrayList<String>();

	public WorkflowFormPage() {
		setPageTitle(ResourceUtils.getModel("pageTitle.workflowForm"));
		setupComponent();
		
//		workflowService.beginExperiment("Experiment1");
//		
//		String stepName = "SimpleFile";
//		String format = "KIV_FORMAT";
//		String store = "false";
//		
//		String method = "DWTPlugin-1.0.0";
//		String params = "01,100,Cz,FAST_DAUBECHIES_2";
//		
//		setFiles(134);
//		workflowService.addToWorkflow(stepName, format, store, method, params, fileNames);
//		workflowService.addFileIds(setFiles(134));
//		fileNames.clear();
//		
//		stepName = "SimpleDouble";
//		format = "DOUBLE_FORMAT";
//		store = "true";
//		
//		fileNames.add("Experiment1_SimpleFile");
//			
//		method = "CWTPlugin-1.0.0";
//		params = "01,1000,Cz,COMPLEX_GAUSSIAN,1,1,1,14000,14000";
//		
//		workflowService.addToWorkflow(stepName, format, store, method, params, fileNames);
//		fileNames.clear();
//		
//		stepName = "SimpleFile2";
//		format = "KIV_FORMAT";
//		store = "true";
//		
//		method = "DWTPlugin-1.0.0";
//		params = "01,100,Cz,FAST_DAUBECHIES_2";
//		
//		setFiles(134);
//		workflowService.addToWorkflow(stepName, format, store, method, params, fileNames);
//		workflowService.addFileIds(setFiles(134));
//		
//		workflowService.endExperiment();
		
//		workflowService.runService();
		
	}
	
	
	private void setupComponent() {
		add(new ButtonPageMenu("leftMenu", WorkflowPageLeftMenu.values()));
	}


	/**
	 * Method taking fileID for experiment files
	 * Checking only for eeg whdr and wmrk files
	 * @param experimentID
	 * @return
	 */

	private int[] setFiles(int experimentID) {
		List<DataFile> data = facade.getDataFilesWhereExpId(experimentID);
		int[] pole = new int[data.size()];
		for (int a = 0; a < pole.length; a++) {
			if (data.get(a).getFilename().endsWith(".eeg")
					|| data.get(a).getFilename().endsWith(".vhdr")
					|| data.get(a).getFilename().endsWith(".vmrk")){
				pole[a] = data.get(a).getDataFileId();
				fileNames.add(data.get(a).getFilename());
			}
		}
		return pole;
	}
}
