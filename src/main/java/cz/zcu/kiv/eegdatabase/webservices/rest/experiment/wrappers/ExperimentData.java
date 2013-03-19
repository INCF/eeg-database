package cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers;

import cz.zcu.kiv.eegdatabase.webservices.rest.common.utils.DateAdapter;
import cz.zcu.kiv.eegdatabase.webservices.rest.groups.wrappers.ResearchGroupData;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

/**
 * Wrapper for experiment information.
 *
 * @author Petr Miko
 */
@XmlType(propOrder = {"experimentId", "scenario", "researchGroup","artifact", "startTime", "endTime", "owner", "subject",
        "diseases", "hardwareList", "digitization", "environmentNote", "weather", "electrodeConf", "pharmaceuticals",
        "softwareList"})
@XmlRootElement(name = "experiment")
public class ExperimentData {

    private int experimentId;
    private HardwareDataList hardwareList;
    private ArtifactData artifact;
    private Date startTime, endTime;
    private String environmentNote;
    private ScenarioSimpleData scenario;
    private WeatherData weather;
    private OwnerData owner;
    private SubjectData subject;
    private DiseaseDataList diseases;
    private DigitizationData digitization;
    private ElectrodeConfData electrodeConf;
    private PharmaceuticalDataList pharmaceuticals;
    private SoftwareDataList softwareList;
    private ResearchGroupData researchGroup;


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

    public OwnerData getOwner() {
        return owner;
    }

    public void setOwner(OwnerData owner) {
        this.owner = owner;
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

    public ElectrodeConfData getElectrodeConf() {
        return electrodeConf;
    }

    public void setElectrodeConf(ElectrodeConfData electrodeConf) {
        this.electrodeConf = electrodeConf;
    }

    public PharmaceuticalDataList getPharmaceuticals() {
        return pharmaceuticals;
    }

    public void setPharmaceuticals(PharmaceuticalDataList pharmaceuticals) {
        this.pharmaceuticals = pharmaceuticals;
    }

    public SoftwareDataList getSoftwareList() {
        return softwareList;
    }

    public void setSoftwareList(SoftwareDataList softwareList) {
        this.softwareList = softwareList;
    }

    public ResearchGroupData getResearchGroup() {
        return researchGroup;
    }

    public void setResearchGroup(ResearchGroupData researchGroup) {
        this.researchGroup = researchGroup;
    }
}