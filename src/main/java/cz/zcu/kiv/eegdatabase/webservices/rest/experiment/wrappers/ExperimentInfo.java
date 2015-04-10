package cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers;


import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"experimentId", "scenarioName", "researchGroupName", "startTime", "endTime", "subjectName", "subjectSurname"})
@XmlRootElement(name = "experiment")
public class ExperimentInfo {

    private String experimentId;
    private String scenarioName;
    private String researchGroupName;
    private String startTime;
    private String endTime;
    private String subjectName;
    private String subjectSurname;


    public ExperimentInfo() { }

    public ExperimentInfo(String experimentId, String scenarioName, String researchGroupName,
                      String startTime, String endTime, String subjectName, String subjectSurname) {
        this.experimentId = experimentId;
        this.scenarioName = scenarioName;
        this.researchGroupName = researchGroupName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.subjectName = subjectName;
        this.subjectSurname = subjectSurname;
    }


    public String getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(String experimentId) {
        this.experimentId = experimentId;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    public String getResearchGroupName() {
        return researchGroupName;
    }

    public void setResearchGroupName(String researchGroupName) {
        this.researchGroupName = researchGroupName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectSurname() {
        return subjectSurname;
    }

    public void setSubjectSurname(String subjectSurname) {
        this.subjectSurname = subjectSurname;
    }
}