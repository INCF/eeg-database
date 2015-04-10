package cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlType(propOrder = {"parameterAttributes"})
@XmlRootElement(name = "parameterAttributes")
public class ParameterAttributeDataList {
    private List<ParameterAttributeData> parameterAttributes;

    public ParameterAttributeDataList() {
        this(new ArrayList<ParameterAttributeData>());
    }

    public ParameterAttributeDataList(List<ParameterAttributeData> parameterAttributes) {
        this.parameterAttributes = parameterAttributes;
    }

    @XmlElement(name = "parameterAttribute")
    public List<ParameterAttributeData> getParameterAttributes() {
        return parameterAttributes;
    }

    public void setParameterAttributes(List<ParameterAttributeData> parameterAttributes) {
        this.parameterAttributes = parameterAttributes;
    }
}
