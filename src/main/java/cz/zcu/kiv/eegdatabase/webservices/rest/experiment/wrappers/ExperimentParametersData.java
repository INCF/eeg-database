package cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"append", "genericParameters"})
@XmlRootElement(name = "experimentParameters")
public class ExperimentParametersData {
    private boolean append = false;
    private GenericParameterDataList genericParameters;

    public ExperimentParametersData() {}

    public ExperimentParametersData(boolean append, GenericParameterDataList genericParameters) {
        this.append = append;
        this.genericParameters = genericParameters;
    }

    public boolean isAppend() {
        return append;
    }

    public void setAppend(boolean append) {
        this.append = append;
    }

    public GenericParameterDataList getGenericParameters() {
        return genericParameters;
    }

    public void setGenericParameters(GenericParameterDataList genericParameters) {
        this.genericParameters = genericParameters;
    }
}
