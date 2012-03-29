package cz.zcu.kiv.eegdatabase.data.pojo;

import javax.persistence.*;
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
@Entity
@javax.persistence.Table(name="SOFTWARE")
public class Software implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "SOFTWARE_ID")
    private int softwareId;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "IS_DEFAULT")
    private int defaultNumber;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Experiment> experiments = new HashSet<Experiment>(0);

    public Software() {
    }

    public Software(int softwareId, String title, String description, Set<Experiment> experiments) {
        this.softwareId = softwareId;
        this.title = title;
        this.description = description;
        this.experiments = experiments;
    }

    public int getSoftwareId() {
        return softwareId;
    }

    public void setSoftwareId(int softwareId) {
        this.softwareId = softwareId;
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

    public int getDefaultNumber() {
        return defaultNumber;
    }

    public void setDefaultNumber(int defaultNumber) {
        this.defaultNumber = defaultNumber;
    }
}
