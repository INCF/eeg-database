package cz.zcu.kiv.eegdatabase.logic.controller.experiment;

/**
 *
 * @author Jindra
 */
public class AddMetadataCommand {

    private int dataId;
    private int paramId;
    private String paramValue;

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

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
}
