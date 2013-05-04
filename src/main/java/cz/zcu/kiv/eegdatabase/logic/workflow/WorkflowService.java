package cz.zcu.kiv.eegdatabase.logic.workflow;

import cz.zcu.kiv.eegdatabase.wui.core.file.FileFacade;

public interface WorkflowService {

	/* (non-Javadoc)
	 * @see cz.zcu.kiv.eegdatabase.logic.workflow.WorkflowService#runService(int[], cz.zcu.kiv.eegdatabase.wui.core.file.FileFacade)
	 */
	public abstract void runService(int[] pole, FileFacade fileFacade);

	public abstract void storeWorkflow();

}