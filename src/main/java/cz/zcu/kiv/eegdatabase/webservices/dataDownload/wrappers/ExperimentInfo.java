package cz.zcu.kiv.eegdatabase.webservices.dataDownload.wrappers;

import java.util.List;

/**
 * Class for gathering few important information about experiment.
 * Meant to be sent to user.
 *
 * @author: Petr Miko (miko.petr at gmail.com)
 */
public class ExperimentInfo {

    private int experimentId;
    private int ownerId;
    private int subjectPersonId;
    private int scenarioId;
    private int weatherId;
    private int researchGroupId;
    private long startTimeInMillis;
    private long endTimeInMillis;
    private int temperature;
    private String weatherNote;
    private int privateFlag;
    private String title;
    private List<Integer> hwIds;
    private long scn;
    private boolean added;
    private boolean changed;

    /**
     * Getter of experiment's identifier.
     *
     * @return experiment's identifier
     */
    public int getExperimentId() {
        return experimentId;
    }

    /**
     * Setter of experiment's identifier.
     *
     * @param experimentId experiment's identifier
     */
    public void setExperimentId(int experimentId) {
        this.experimentId = experimentId;
    }

    /**
     * Getter of owner person identifier.
     *
     * @return person identifier
     */
    public int getOwnerId() {
        return ownerId;
    }

    /**
     * Setter of owner person identifier.
     *
     * @param ownerId owner person identifier
     */
    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * Getter of subject person identifier.
     *
     * @return person identifier
     */
    public int getSubjectPersonId() {
        return subjectPersonId;
    }

    /**
     * Setter of subject person identifier.
     *
     * @param subjectPersonId person identifier
     */
    public void setSubjectPersonId(int subjectPersonId) {
        this.subjectPersonId = subjectPersonId;
    }

    /**
     * Getter of scenario's identifier.
     *
     * @return scenario's identifier
     */
    public int getScenarioId() {
        return scenarioId;
    }

    /**
     * Setter of scenario's identifier.
     *
     * @param scenarioId scenario's identifier
     */
    public void setScenarioId(int scenarioId) {
        this.scenarioId = scenarioId;
    }

    /**
     * Getter of weather identifier.
     *
     * @return weather identifier
     */
    public int getWeatherId() {
        return weatherId;
    }

    /**
     * Setter of weather identifier.
     *
     * @param weatherId weather identifier
     */
    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    /**
     * Getter of research groups identifier.
     *
     * @return research group identifier
     */
    public int getResearchGroupId() {
        return researchGroupId;
    }

    /**
     * Setter of research group identifier.
     *
     * @param researchGroupId research group identifier
     */
    public void setResearchGroupId(int researchGroupId) {
        this.researchGroupId = researchGroupId;
    }

    /**
     * Getter of experiment's start time in milliseconds.
     *
     * @return milliseconds of experiment's start time
     */
    public long getStartTimeInMillis() {
        return startTimeInMillis;
    }

    /**
     * Setter of experiment's start time in milliseconds.
     *
     * @param startTimeInMillis milliseconds of experiment's start time
     */
    public void setStartTimeInMillis(long startTimeInMillis) {
        this.startTimeInMillis = startTimeInMillis;
    }

    /**
     * Getter of experiment's end time in milliseconds.
     *
     * @return milliseconds of experiment's end time
     */
    public long getEndTimeInMillis() {
        return endTimeInMillis;
    }

    /**
     * Setter of experiment's end time in milliseconds.
     *
     * @param endTimeInMillis milliseconds of experiment's end time
     */
    public void setEndTimeInMillis(long endTimeInMillis) {
        this.endTimeInMillis = endTimeInMillis;
    }

    /**
     * Getter of temperature measured during experiment measurement.
     *
     * @return degrees of temperature (Celsius, presumed)
     */
    public int getTemperature() {
        return temperature;
    }

    /**
     * Setter of temperature measured during experiment measurement.
     *
     * @param temperature degrees of temperature (Celsius, presumed)
     */
    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    /**
     * Getter of weather note.
     *
     * @return weather note
     */
    public String getWeatherNote() {
        return weatherNote;
    }

    /**
     * Setter of weather note.
     *
     * @param weatherNote weather note
     */
    public void setWeatherNote(String weatherNote) {
        this.weatherNote = weatherNote;
    }

    /**
     * Getter of flag, whether the experiment is private.
     *
     * @return flag private/non private
     */
    public int getPrivateFlag() {
        return privateFlag;
    }

    /**
     * Setter of flag, whether the experiment is private or not.
     *
     * @param privateFlag flag private/non private
     */
    public void setPrivateFlag(int privateFlag) {
        this.privateFlag = privateFlag;
    }

    /**
     * Getter of a scenario name in accordance to experiment identifier.
     * @return scenario name
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter of "experiment" (scenario) name.
     * @param title scenario name
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter of revision number (oracle scn).
     * @return revision number
     */
    public long getScn() {
        return scn;
    }

    /**
     * Setter of revision number (oracle scn).
     *
     * @param scn revision number
     */
    public void setScn(long scn) {
        this.scn = scn;
    }

    /**
     * Getter of used HW identifiers.
     * @return list of HW identifiers
     */
    public List<Integer> getHwIds() {
        return hwIds;
    }

    /**
     * Setter of used HW identifiers.
     * @param hwIds list of HW identifiers
     */
    public void setHwIds(List<Integer> hwIds) {
        this.hwIds = hwIds;
    }

    /**
     * Object is meant to create new record.
     * @return new record
     */
    public boolean isAdded() {
        return added;
    }

    /**
     * Mark object to create new record.
     * @param added new record
     */
    public void setAdded(boolean added) {
        this.added = added;
    }

    /**
     * Object is meant to update existing record.
     * @return updated record
     */
    public boolean isChanged() {
        return changed;
    }

    /**
     * Mark object to update an existing object.
     * @param changed updated record
     */
    public void setChanged(boolean changed) {
        this.changed = changed;
    }
}
