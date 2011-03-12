package cz.zcu.kiv.eegdatabase.webservices.dataDownload;

/**
 * Author: Petr Miko
 */
public class ExperimentInfo {
    private int experimentID;
    private String scenarioName;

    public ExperimentInfo(){}

    public ExperimentInfo(int experimentID, String scenarioName){
        this.experimentID = experimentID;
        this.scenarioName = scenarioName;
    }

    public int getExperimentID() {
        return experimentID;
    }

    public void setExperimentID(int experimentID) {
        this.experimentID = experimentID;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }
}
