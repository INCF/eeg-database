package cz.zcu.kiv.eegdatabase.data.pojo;

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
public class StimulusType implements Serializable {

    private int StimulusTypeId;
    private String description;
    private Set<StimulusRel> stimulusRels = new HashSet<StimulusRel>(0);

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

    public Set<StimulusRel> getStimulusRels() {
        return stimulusRels;
    }

    public void setStimulusRels(Set<StimulusRel> stimulusRels) {
        this.stimulusRels = stimulusRels;
    }
}
