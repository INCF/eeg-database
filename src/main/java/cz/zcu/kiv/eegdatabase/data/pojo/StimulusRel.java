package cz.zcu.kiv.eegdatabase.data.pojo;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 5.3.12
 * Time: 14:14
 * To change this template use File | Settings | File Templates.
 */
@Entity
@javax.persistence.Table(name="STIMULUS_REL")
public class StimulusRel implements Serializable {
    @EmbeddedId
    private StimulusRelId stimulusRelId;
    @ManyToOne
    @JoinColumn(name = "SCENARIO_ID")
    private Scenario scenario;
    @ManyToOne
    @JoinColumn(name = "STIMULUS_ID")
    private Stimulus stimulus;
    @ManyToOne
    @JoinColumn(name = "STIMULUS_TYPE_ID")
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

