package cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlType(propOrder = {"name", "valueString", "valueInteger", "parameterAttributes"})
@XmlRootElement(name = "genericParameter")
public class GenericParameterData {

    private String name;
    private String valueString;
    private Double valueInteger;
    private ParameterAttributeDataList parameterAttributes;

    public GenericParameterData() {
    }

    public GenericParameterData(String name, Double valueInteger) {
        this.name = name;
        this.valueInteger = valueInteger;
    }

    public GenericParameterData(String name, String valueString) {
        this.name = name;
        this.valueString = valueString;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValueString() {
        return valueString;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
    }

    public Double getValueInteger() {
        return valueInteger;
    }

    public void setValueInteger(Double valueInteger) {
        this.valueInteger = valueInteger;
    }

    public ParameterAttributeDataList getParameterAttributes() {
        return parameterAttributes;
    }

    public void setParameterAttributes(ParameterAttributeDataList parameterAttributes) {
        this.parameterAttributes = parameterAttributes;
    }
}
