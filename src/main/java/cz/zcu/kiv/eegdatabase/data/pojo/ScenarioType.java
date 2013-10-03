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
 *   ScenarioType.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.pojo;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Koren
 * Date: 31.5.11
 * Time: 19:46
 * To change this template use File | Settings | File Templates.
 */
@Entity
@javax.persistence.Table(name="SCENARIO_TYPE_PARENT")
public class ScenarioType<T> implements IScenarioType<T>, java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "SCENARIO_ID")
    private int scenarioId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="SCENARIO_ID")
    @PrimaryKeyJoinColumn
    private Scenario scenario;

    public int getScenarioId() {
      return scenarioId;
    }

    public void setScenarioId(int scenarioId) {
      this.scenarioId = scenarioId;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    public T getScenarioXml(){ return null; }

    public void setScenarioXml(T scenarioXml) {}
}
