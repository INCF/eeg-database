package cz.zcu.kiv.eegdatabase.logic.controller.list.experimentoptparamdef;

/**
 * @author Jindra
 */
public class AddExperimentOptParamDefCommand {

    private int id;
    private String paramName;
    private String paramDataType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParamDataType() {
        return paramDataType;
    }

    public void setParamDataType(String paramDataType) {
        this.paramDataType = paramDataType;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }
}
