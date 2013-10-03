/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   StimulusRel.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
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

