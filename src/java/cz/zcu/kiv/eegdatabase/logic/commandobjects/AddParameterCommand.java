package cz.zcu.kiv.eegdatabase.logic.commandobjects;

/**
 *
 * @author Jindra
 */
public class AddParameterCommand {

    private String paramName;
    private String paramDataType;

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
