package cz.zcu.kiv.eegdatabase.data.pojo;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 5.3.12
 * Time: 14:14
 * To change this template use File | Settings | File Templates.
 */
public class StimulusRel implements Serializable {

    private StimulusRelId stimulusRelId;
    private Scenario scenario;
    private Stimulus stimulus;
    private StimulusType stimulusType;

    public StimulusRel() {
    }

    public StimulusRelId getStimulusRelId() {
        return stimulusRelId;
    }

    public void setStimulusRelId(StimulusRelId stimulusRelId) {
        this.stimulusRelId = stimulusRelId;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    public Stimulus getStimulus() {
        return stimulus;
    }

    public void setStimulus(Stimulus stimulus) {
        this.stimulus = stimulus;
    }

    public StimulusType getStimulusType() {
        return stimulusType;
    }

    public void setStimulusType(StimulusType stimulusType) {
        this.stimulusType = stimulusType;
    }

    public StimulusRel(StimulusRelId stimulusRelId, Scenario scenario, Stimulus stimulus, StimulusType stimulusType) {
        this.stimulusRelId = stimulusRelId;
        this.scenario = scenario;
        this.stimulus = stimulus;
        this.stimulusType = stimulusType;


    }
}

