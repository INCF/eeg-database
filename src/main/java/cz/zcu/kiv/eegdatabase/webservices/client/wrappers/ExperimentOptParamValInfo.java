package cz.zcu.kiv.eegdatabase.webservices.client.wrappers;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamValId;

/**
 * Class for gathering important information about optional parameter value for
 * experiment. Meant to be sent between eegclient and portal's client service.
 * 
 * @author František Liška
 */
public class ExperimentOptParamValInfo {
	private ExperimentOptParamValId id;
	private String paramValue;

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public ExperimentOptParamValId getId() {
		return id;
	}

	public void setId(ExperimentOptParamValId id) {
		this.id = id;
	}
}
