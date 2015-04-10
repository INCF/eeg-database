package cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlType(propOrder = {"genericParameters"})
@XmlRootElement(name = "genericParameters")
public class GenericParameterDataList {
    private List<GenericParameterData> genericParameters;

    public GenericParameterDataList() {
        this(new ArrayList<GenericParameterData>());
    }

    public GenericParameterDataList(List<GenericParameterData> genericParameters) {
        this.genericParameters = genericParameters;
    }

    @XmlElement(name = "genericParameter")
    public List<GenericParameterData> getGenericParameters() {
        return genericParameters;
    }

    public void setGenericParameters(List<GenericParameterData> genericParameters) {
        this.genericParameters = genericParameters;
    }
}
