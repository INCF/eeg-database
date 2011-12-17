package cz.zcu.kiv.eegdatabase.logic.controller.experiment;

/**
 * @author JiPER
 */
public class AddExperimentCommand {

    private int measurationId;
    private int researchGroup;
    private String researchGroupTitle;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private int subjectPerson;
    private int[] coExperimenters;
    private int scenario;
    private int[] hardware;
    private int weather;
    private String weatherNote;
    private String temperature;
    private boolean privateNote;

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

    public int getMeasurationId() {
        return measurationId;
    }

    public void setMeasurationId(int measurationId) {
        this.measurationId = measurationId;
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

    public String getResearchGroupTitle() {
        return researchGroupTitle;
    }

    public void setResearchGroupTitle(String researchGroupTitle) {
        this.researchGroupTitle = researchGroupTitle;
    }
}
