package cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers;

import cz.zcu.kiv.eegdatabase.webservices.rest.common.utils.DateAdapter;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

/**
 * Wrapper for experiment information.
 *
 * @author Petr Miko
 */
@XmlType(propOrder = {"experimentId", "scenario", "artifact", "startTime", "endTime", "subject", "diseases", "hardwareList", "digitization", "environmentNote", "weather"})
@XmlRootElement(name = "experiment")
public class ExperimentData {

    private int experimentId;
    private HardwareDataList hardwareList;
    private ArtifactData artifact;
    private Date startTime, endTime;
    private String environmentNote;
    private ScenarioSimpleData scenario;
    private WeatherData weather;
    private SubjectData subject;
    private DiseaseDataList diseases;
    private DigitizationData digitization;


    public ExperimentData() {
    }

    public ArtifactData getArtifact() {
        return artifact;
    }

    public void setArtifact(ArtifactData artifact) {
        this.artifact = artifact;
    }

    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getEnvironmentNote() {
        return environmentNote;
    }

    public void setEnvironmentNote(String environmentNote) {
        this.environmentNote = environmentNote;
    }

    public ScenarioSimpleData getScenario() {
        return scenario;
    }

    public void setScenario(ScenarioSimpleData scenario) {
        this.scenario = scenario;
    }

    public int getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(int experimentId) {
        this.experimentId = experimentId;
    }

    public SubjectData getSubject() {
        return subject;
    }

    public void setSubject(SubjectData subject) {
        this.subject = subject;
    }

    public WeatherData getWeather() {
        return weather;
    }

    public void setWeather(WeatherData weatherData) {
        this.weather = weatherData;
    }

    public DiseaseDataList getDiseases() {
        return diseases;
    }

    public void setDiseases(DiseaseDataList diseases) {
        this.diseases = diseases;
    }

    public DigitizationData getDigitization() {
        return digitization;
    }

    public void setDigitization(DigitizationData digitization) {
        this.digitization = digitization;
    }

    public HardwareDataList getHardwareList() {
        return hardwareList;
    }

    public void setHardwareList(HardwareDataList hardwareList) {
        this.hardwareList = hardwareList;
    }
}