package cz.zcu.kiv.eegdatabase.logic.controller.person;

/**
 * @author Jindra
 */
public class AddPersonAdditionalParamValueCommand {

    private int personFormId;
    private int paramId;
    private String paramValue;

    public int getParamId() {
        return paramId;
    }

    public void setParamId(int paramId) {
        this.paramId = paramId;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public int getPersonFormId() {
        return personFormId;
    }

    public void setPersonFormId(int personFormId) {
        this.personFormId = personFormId;
    }
}
