package cz.zcu.kiv.eegdatabase.data.pojo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 20.2.12
 * Time: 14:36
 * To change this template use File | Settings | File Templates.
 */
public class Software implements Serializable {

    private int softwareId;
    private String name;
    private String description;
    private Set<Experiment> experiments = new HashSet<Experiment>(0);

    public Software() {
    }

    public Software(int softwareId, String name, String description, Set<Experiment> experiments) {
        this.softwareId = softwareId;
        this.name = name;
        this.description = description;
        this.experiments = experiments;
    }

    public int getSoftwareId() {
        return softwareId;
    }

    public void setSoftwareId(int softwareId) {
        this.softwareId = softwareId;
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
