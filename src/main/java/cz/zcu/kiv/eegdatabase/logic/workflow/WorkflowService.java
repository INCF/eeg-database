package cz.zcu.kiv.eegdatabase.logic.workflow;

import java.util.List;

public interface WorkflowService {

	public abstract void runService();

	public abstract void addToWorkflow(String name, String format, String store, String method, String params, List<String> fileNames);

	public abstract void addFileIds(int[] files);
	
	public abstract void beginExperiment(String name);
	
	public abstract void endExperiment();
}