package cz.zcu.kiv.eegdatabase.webservices.dataDownload;

/**
 * Class for gathering few important information about experiment.
 * Meant to be sent to user.
 *
 * Author: Petr Miko
 */
public class ExperimentInfo {
    private int experimentId;
    private int scenarioId;
    private String scenarioName;

    public ExperimentInfo(){}

    public ExperimentInfo(int experimentId, int scenarioId, String scenarioName){
        this.experimentId = experimentId;
        this.scenarioId = scenarioId;
        this.scenarioName = scenarioName;
    }

    public int getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(int experimentId) {
        this.experimentId = experimentId;
    }

    public int getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(int scenarioId) {
        this.scenarioId = scenarioId;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }
}
