package cz.zcu.kiv.eegdatabase.logic.controller.list.filemetadata;

/**
 * @author Jindra
 */
public class AddFileMetadataParamDefCommand {

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
