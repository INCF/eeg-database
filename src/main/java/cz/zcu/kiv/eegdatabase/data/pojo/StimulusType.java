package cz.zcu.kiv.eegdatabase.data.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 20.2.12
 * Time: 14:31
 * To change this template use File | Settings | File Templates.
 */
@Entity
@javax.persistence.Table(name="STIMULUS_TYPE")
public class StimulusType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "STIMULUS_TYPE_ID")
    private int StimulusTypeId;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "IS_DEFAULT")
    private int defaultNumber;
    @OneToMany(mappedBy = "stimulusType")
    private Set<StimulusRel> stimulusRels = new HashSet<StimulusRel>(0);
    private Set<ResearchGroup> researchGroups = new HashSet<ResearchGroup>(0);


    public StimulusType() {
    }

    public StimulusType(int stimulusTypeId, String description) {
        StimulusTypeId = stimulusTypeId;
        this.description = description;
    }

    public int getStimulusTypeId() {
        return StimulusTypeId;
    }

    public void setStimulusTypeId(int stimulusTypeId) {
        StimulusTypeId = stimulusTypeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDefaultNumber() {
        return defaultNumber;
    }

    public void setDefaultNumber(int defaultNumber) {
        this.defaultNumber = defaultNumber;
    }

    public Set<StimulusRel> getStimulusRels() {
        return stimulusRels;
    }

    public void setStimulusRels(Set<StimulusRel> stimulusRels) {
        this.stimulusRels = stimulusRels;
    }

    public Set<ResearchGroup> getResearchGroups() {
        return researchGroups;
    }

    public void setResearchGroups(Set<ResearchGroup> researchGroups) {
        this.researchGroups = researchGroups;
    }
}
