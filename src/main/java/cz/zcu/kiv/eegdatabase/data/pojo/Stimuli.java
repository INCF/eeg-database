package cz.zcu.kiv.eegdatabase.data.pojo;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 20.2.12
 * Time: 14:27
 * To change this template use File | Settings | File Templates.
 */
public class Stimuli implements Serializable {

    private int stimuliId;
    private String description;

    public Stimuli() {

    }

    public Stimuli(int stimuliId, String description) {
        this.stimuliId = stimuliId;
        this.description = description;
    }

    public int getStimuliId() {
        return stimuliId;
    }

    public void setStimuliId(int stimuliId) {
        this.stimuliId = stimuliId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
