package cz.zcu.kiv.eegdatabase.data.pojo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 1.3.12
 * Time: 11:41
 * To change this template use File | Settings | File Templates.
 */
public class ProjectType implements Serializable {

    private int projectTypeId;
    private String title;
    private String description;
    private Set<Experiment> experiments = new HashSet<Experiment>(0);

    public ProjectType() {

    }

    public ProjectType(int projectTypeId, String title, String description, Set<Experiment> experiments) {
        this.projectTypeId = projectTypeId;
        this.title = title;
        this.description = description;
        this.experiments = experiments;
    }

    public int getProjectTypeId() {
        return projectTypeId;
    }

    public void setProjectTypeId(int projectTypeId) {
        this.projectTypeId = projectTypeId;
    }

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

    public Set<Experiment> getExperiments() {
        return experiments;
    }

    public void setExperiments(Set<Experiment> experiments) {
        this.experiments = experiments;
    }
}
