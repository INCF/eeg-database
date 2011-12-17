package cz.zcu.kiv.eegdatabase.logic.controller.experiment;

/**
 * @author Jindra
 */
public class AddExperimentOptParamValCommand {

    private int measurationFormId;
    private int paramId;
    private String paramValue;
    private String researchGroupTitle;

    public int getMeasurationFormId() {
        return measurationFormId;
    }

    public void setMeasurationFormId(int measurationFormId) {
        this.measurationFormId = measurationFormId;
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

    public String getResearchGroupTitle() {
        return researchGroupTitle;
    }

    public void setResearchGroupTitle(String researchGroupTitle) {
        this.researchGroupTitle = researchGroupTitle;
    }
}
