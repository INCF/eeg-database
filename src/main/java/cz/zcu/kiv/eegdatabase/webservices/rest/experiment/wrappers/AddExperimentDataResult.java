package cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"success"})
@XmlRootElement(name = "addExperimentDataResult")
public class AddExperimentDataResult {
    private boolean success;

    public AddExperimentDataResult() {}

    public AddExperimentDataResult(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
