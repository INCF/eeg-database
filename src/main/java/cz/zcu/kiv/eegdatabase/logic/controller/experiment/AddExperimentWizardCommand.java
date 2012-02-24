package cz.zcu.kiv.eegdatabase.logic.controller.experiment;

import org.springframework.web.multipart.MultipartFile;

/**
 * Object command for saving experiment from wizard
 * User: pbruha
 * Date: 10.3.11
 * Time: 19:18
 * To change this template use File | Settings | File Templates.
 */
public class AddExperimentWizardCommand {
    private int measurationId;
    private int researchGroup;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private int subjectPerson;
    private int[] coExperimenters;
    private int scenario;
    private int[] hardware;
    private int weather;
    private String samplingRate;
    private String weatherNote;
    private String temperature;
    private boolean privateNote;

    private String fileDescription;
    private MultipartFile dataFile;

    private String title;
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getResearchGroup() {
        return researchGroup;
    }

    public void setResearchGroup(int researchGroup) {
        this.researchGroup = researchGroup;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int[] getCoExperimenters() {
        return coExperimenters;
    }

    public void setCoExperimenters(int[] coExperimenters) {
        this.coExperimenters = coExperimenters;
    }

    public int[] getHardware() {
        return hardware;
    }

    public void setHardware(int[] hardware) {
        this.hardware = hardware;
    }

    public int getSubjectPerson() {
        return subjectPerson;
    }

    public void setSubjectPerson(int subjectPerson) {
        this.subjectPerson = subjectPerson;
    }

    public int getScenario() {
        return scenario;
    }

    public void setScenario(int scenario) {
        this.scenario = scenario;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setPrivateNote(boolean privateNote) {
        this.privateNote = privateNote;
    }

    public boolean isPrivateNote() {
        return privateNote;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public int getWeather() {
        return weather;
    }

    public void setWeather(int weather) {
        this.weather = weather;
    }

    public String getWeatherNote() {
        return weatherNote;
    }

    public void setWeatherNote(String weatherNote) {
        this.weatherNote = weatherNote;
    }

     public int getMeasurationId() {
        return measurationId;
    }

    public void setMeasurationId(int measurationId) {
        this.measurationId = measurationId;
    }

    public MultipartFile getDataFile() {
        return dataFile;
    }

    public void setDataFile(MultipartFile dataFile) {
        this.dataFile = dataFile;
    }

    public String getFileDescription() {
        return fileDescription;
    }

    public void setFileDescription(String fileDescription) {
        this.fileDescription = fileDescription;
    }

    public String getSamplingRate() {
        return samplingRate;
    }

    public void setSamplingRate(String samplingRate) {
        this.samplingRate = samplingRate;
    }
}
