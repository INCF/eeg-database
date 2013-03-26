package cz.zcu.kiv.eegdatabase.data.pojo;

import cz.zcu.kiv.eegdatabase.data.annotation.SolrField;
import cz.zcu.kiv.eegdatabase.data.annotation.SolrId;
import cz.zcu.kiv.eegdatabase.data.indexing.IndexField;

import javax.persistence.*;
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
@Entity
@javax.persistence.Table(name="PHARMACEUTICAL")
public class Pharmaceutical implements Serializable {
    @SolrId
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PHARMACEUTICAL_ID")
    private int pharmaceuticalId;
    @SolrField(name = IndexField.TITLE)
    @Column(name = "TITLE")
    private String title;
    @SolrField(name = IndexField.TEXT)
    @Column(name = "DESCRIPTION")
    private String description;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Experiment> experiments = new HashSet<Experiment>(0);
    private Set<ResearchGroup> researchGroups = new HashSet<ResearchGroup>(0);

    public Pharmaceutical() {
    }

    public Pharmaceutical(int pharmaceuticalId, String title, String description) {
        this.pharmaceuticalId = pharmaceuticalId;
        this.title = title;
        this.description = description;
    }

    public int getPharmaceuticalId() {
        return pharmaceuticalId;
    }

    public void setPharmaceuticalId(int pharmaceuticalId) {
        this.pharmaceuticalId = pharmaceuticalId;
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

    public Set<ResearchGroup> getResearchGroups() {
        return researchGroups;
    }

    public void setResearchGroups(Set<ResearchGroup> researchGroups) {
        this.researchGroups = researchGroups;
    }
}
