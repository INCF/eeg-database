package cz.zcu.kiv.eegdatabase.data.pojo;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 5.3.12
 * Time: 14:05
 * To change this template use File | Settings | File Templates.
 */
public class StimulusRelId implements Serializable {

    private int scenarioId;
    private int stimulusId;

    public StimulusRelId() {
    }

    public StimulusRelId(int scenarioId, int stimulusId) {
        this.scenarioId = scenarioId;
        this.stimulusId = stimulusId;
    }

    public int getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(int scenarioId) {
        this.scenarioId = scenarioId;
    }

    public int getStimulusId() {
        return stimulusId;
    }

    public void setStimulusId(int stimulusId) {
        this.stimulusId = stimulusId;
    }
}

