package cz.zcu.kiv.eegdatabase.webservices.dataDownload;

import java.sql.Timestamp;

/**
 * Class for gathering few important information about experiment.
 * Meant to be sent to user.
 * <p/>
 * Author: Petr Miko
 */
public class ExperimentInfo {

    private int experimentId;
    private int ownerId;
    private int subjectPersonId;
    private int scenarioId;
    private int weatherId;
    private int researchGroupId;
    private long startTimeInMilis;
    private long endTimeInMilis;
    private int temperature;
    private String weatherNote;
    private int privateFlag;

    public ExperimentInfo() {}

    public int getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(int experimentId) {
        this.experimentId = experimentId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getSubjectPersonId() {
        return subjectPersonId;
    }

    public void setSubjectPersonId(int subjectPersonId) {
        this.subjectPersonId = subjectPersonId;
    }

    public int getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(int scenarioId) {
        this.scenarioId = scenarioId;
    }

    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    public int getResearchGroupId() {
        return researchGroupId;
    }

    public void setResearchGroupId(int researchGroupId) {
        this.researchGroupId = researchGroupId;
    }

    public long getStartTimeInMilis() {
        return startTimeInMilis;
    }

    public void setStartTimeInMilis(long startTimeInMilis) {
        this.startTimeInMilis = startTimeInMilis;
    }

    public long getEndTimeInMilis() {
        return endTimeInMilis;
    }

    public void setEndTimeInMilis(long endTimeInMilis) {
        this.endTimeInMilis = endTimeInMilis;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public String getWeatherNote() {
        return weatherNote;
    }

    public void setWeatherNote(String weatherNote) {
        this.weatherNote = weatherNote;
    }

    public int getPrivateFlag() {
        return privateFlag;
    }

    public void setPrivateFlag(int privateFlag) {
        this.privateFlag = privateFlag;
    }
}
