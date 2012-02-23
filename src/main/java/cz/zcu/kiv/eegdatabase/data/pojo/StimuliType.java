package cz.zcu.kiv.eegdatabase.data.pojo;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 20.2.12
 * Time: 14:31
 * To change this template use File | Settings | File Templates.
 */
public class StimuliType implements Serializable {

    private int StimuliTypeId;
    private String description;

    public StimuliType() {
    }

    public StimuliType(int stimuliTypeId, String description) {
        StimuliTypeId = stimuliTypeId;
        this.description = description;
    }

    public int getStimuliTypeId() {
        return StimuliTypeId;
    }

    public void setStimuliTypeId(int stimuliTypeId) {
        StimuliTypeId = stimuliTypeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
