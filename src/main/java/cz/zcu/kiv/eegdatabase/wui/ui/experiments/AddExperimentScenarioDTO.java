package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import cz.zcu.kiv.eegdatabase.wui.core.dto.IdentifiDTO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Matheo
 * Date: 26.3.13
 * Time: 17:45
 * To change this template use File | Settings | File Templates.
 */
public class AddExperimentScenarioDTO extends IdentifiDTO implements Serializable {

    private String scenario;
    private String group;
    private boolean isDefaultGroup;
    private String project;
    private String startDate;
    private String startTime;
    private String finishDate;
    private String finishTime;
    private String subjects;
    private String stimulus;
    private List<String> coExperimenters;
    private Date start;
    private Date finish;

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public String getStimulus() {
        return stimulus;
    }

    public void setStimulus(String stimulus) {
        this.stimulus = stimulus;
    }

    public List<String> getCoExperimenters() {
        return coExperimenters;
    }

    public void setCoExperimenters(List<String> coExperimenters) {
        this.coExperimenters = coExperimenters;
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

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public boolean isDefaultGroup() {
        return isDefaultGroup;
    }

    public void setDefaultGroup(boolean defaultGroup) {
        isDefaultGroup = defaultGroup;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getFinish() {
        return finish;
    }

    public void setFinish(Date finish) {
        this.finish = finish;
    }
}
