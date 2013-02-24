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
@XmlType(propOrder = { "experimentId", "scenarioId", "scenarioName", "artifactId", "startTime", "endTime", "environmentNote" })
@XmlRootElement(name = "experiment")
public class ExperimentData {

    private int experimentId;

    public ExperimentData(){}

    private int artifactId;
    private Date startTime, endTime;
    private String environmentNote;
    private int scenarioId;
    private String scenarioName;

    public int getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(int artifactId) {
        this.artifactId = artifactId;
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

    public void setExperimentId(int experimentId) {
        this.experimentId = experimentId;
    }

    public int getExperimentId() {
        return experimentId;
    }
}