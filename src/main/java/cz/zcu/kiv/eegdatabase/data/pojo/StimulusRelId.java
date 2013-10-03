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
 *   StimulusRelId.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.pojo;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 5.3.12
 * Time: 14:05
 * To change this template use File | Settings | File Templates.
 */
@Embeddable
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

