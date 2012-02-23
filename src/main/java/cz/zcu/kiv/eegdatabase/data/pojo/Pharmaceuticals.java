package cz.zcu.kiv.eegdatabase.data.pojo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 8.2.12
 * Time: 13:23
 * To change this template use File | Settings | File Templates.
 */
public class Pharmaceuticals implements Serializable {

    private int pharmaceuticalsId;
    private String name;
    private String description;
    private Set<Experiment> experiments = new HashSet<Experiment>(0);

    public Pharmaceuticals() {
    }

    public Pharmaceuticals(int pharmaceuticalsId, String name, String description) {
        this.pharmaceuticalsId = pharmaceuticalsId;
        this.name = name;
        this.description = description;
    }

    public int getPharmaceuticalsId() {
        return pharmaceuticalsId;
    }

    public void setPharmaceuticalsId(int pharmaceuticalsId) {
        this.pharmaceuticalsId = pharmaceuticalsId;
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
