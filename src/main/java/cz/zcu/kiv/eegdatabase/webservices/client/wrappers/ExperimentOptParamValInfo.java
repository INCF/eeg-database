package cz.zcu.kiv.eegdatabase.webservices.client.wrappers;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamValId;

/**
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
