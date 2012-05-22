package cz.zcu.kiv.eegdatabase.webservices.client.wrappers;

import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamValId;

/**
 * @author František Liška
 */
public class PersonOptParamValInfo {
    private PersonOptParamValId id;
    private int personId;
    private int personOptParamDefId;
    private String paramValue;

    public PersonOptParamValId getId() {
        return id;
    }

    public void setId(PersonOptParamValId id) {
        this.id = id;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getPersonOptParamDefId() {
        return personOptParamDefId;
    }

    public void setPersonOptParamDefId(int personOptParamDefId) {
        this.personOptParamDefId = personOptParamDefId;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }
}
