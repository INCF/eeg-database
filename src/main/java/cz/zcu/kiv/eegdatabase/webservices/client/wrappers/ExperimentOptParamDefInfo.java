package cz.zcu.kiv.eegdatabase.webservices.client.wrappers;

import java.util.LinkedList;
import java.util.List;

/**
 * Class for gathering important information about optional parameter definition
 * for experiment. Meant to be sent between eegclient and portal's client
 * service.
 * 
 * @author František Liška
 */
public class ExperimentOptParamDefInfo {
	private int experimentOptParamDefId;
	private String paramName;
	private String paramDataType;
	private int defaultNumber;
	private List<Integer> researchGroupIdList = new LinkedList<Integer>();

	public int getExperimentOptParamDefId() {
		return experimentOptParamDefId;
	}

	public void setExperimentOptParamDefId(int experimentOptParamDefId) {
		this.experimentOptParamDefId = experimentOptParamDefId;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamDataType() {
		return paramDataType;
	}

	public void setParamDataType(String paramDataType) {
		this.paramDataType = paramDataType;
	}

	public int getDefaultNumber() {
		return defaultNumber;
	}

	public void setDefaultNumber(int defaultNumber) {
		this.defaultNumber = defaultNumber;
	}

	public List<Integer> getResearchGroupIdList() {
		return researchGroupIdList;
	}

	public void setResearchGroupIdList(List<Integer> researchGroupIdList) {
		this.researchGroupIdList = researchGroupIdList;
	}
}
