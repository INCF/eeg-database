package cz.zcu.kiv.eegdatabase.data.pojo;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 6.3.12
 * Time: 13:46
 * To change this template use File | Settings | File Templates.
 */
public class Analysis implements Serializable {

    private int analysisId;
    private int epochNumber;
    private int prestimulusTime;
    private int poststimulusTime;
    private String description;

    public Analysis() {
    }

    public Analysis(int analysisId, int epochNumber, int prestimulusTime, int poststimulusTime, String description) {
        this.analysisId = analysisId;
        this.epochNumber = epochNumber;
        this.prestimulusTime = prestimulusTime;
        this.poststimulusTime = poststimulusTime;
        this.description = description;
    }

    public int getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(int analysisId) {
        this.analysisId = analysisId;
    }

    public int getEpochNumber() {
        return epochNumber;
    }

    public void setEpochNumber(int epochNumber) {
        this.epochNumber = epochNumber;
    }

    public int getPrestimulusTime() {
        return prestimulusTime;
    }

    public void setPrestimulusTime(int prestimulusTime) {
        this.prestimulusTime = prestimulusTime;
    }

    public int getPoststimulusTime() {
        return poststimulusTime;
    }

    public void setPoststimulusTime(int poststimulusTime) {
        this.poststimulusTime = poststimulusTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}


