package cz.zcu.kiv.eegdatabase.data.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 20.2.12
 * Time: 14:41
 * To change this template use File | Settings | File Templates.
 */
@Entity
@javax.persistence.Table(name="ARTEFACT_REMOVING_METHOD")
public class ArtifactRemoveMethod implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ARTEFACT_REMOVING_METHOD_ID")
    private int artifactRemoveMethodId;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "DESCRIPTION")
    private String description;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Experiment> experiments = new HashSet<Experiment>(0);
    private Set<ResearchGroup> researchGroups = new HashSet<ResearchGroup>(0);
    @Column(name = "IS_DEFAULT")
    private int defaultNumber;

    public ArtifactRemoveMethod() {
    }

    public ArtifactRemoveMethod(int artifactRemoveMethodId, String title, String description, Set<Experiment> experiments) {
        this.artifactRemoveMethodId = artifactRemoveMethodId;
        this.title = title;
        this.description = description;
        this.experiments = experiments;
    }

    public int getArtifactRemoveMethodId() {
        return artifactRemoveMethodId;
    }

    public void setArtifactRemoveMethodId(int artifactRemoveMethodId) {
        this.artifactRemoveMethodId = artifactRemoveMethodId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDefaultNumber() {
        return defaultNumber;
    }

    public void setDefaultNumber(int defaultNumber) {
        this.defaultNumber = defaultNumber;
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
