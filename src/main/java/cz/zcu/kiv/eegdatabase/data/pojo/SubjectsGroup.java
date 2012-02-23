package cz.zcu.kiv.eegdatabase.data.pojo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 8.2.12
 * Time: 13:30
 * To change this template use File | Settings | File Templates.
 */
public class SubjectsGroup implements Serializable {

    private int subjectsGroupId;
    private String name;
    private String description;
    private Set<Experiment> experiments = new HashSet<Experiment>(0);

    public SubjectsGroup() {
    }

    public SubjectsGroup(int subjectsGroupId, String description, String name) {
        this.subjectsGroupId = subjectsGroupId;
        this.description = description;
        this.name = name;
    }

    public int getSubjectsGroupId() {
        return subjectsGroupId;
    }

    public void setSubjectsGroupId(int subjectsGroupId) {
        this.subjectsGroupId = subjectsGroupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
