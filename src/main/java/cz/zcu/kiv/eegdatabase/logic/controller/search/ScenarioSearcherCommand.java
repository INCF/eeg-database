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
 *   ScenarioSearcherCommand.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.controller.search;

/**
 * @author pbruha
 */
public class ScenarioSearcherCommand {

    private int scenarioOption;
    private int scenario;
    private int lenghtOption;
    private int scenarioLenghtFrom;
    private int scenarioLenghtTo;
    private int authorScenarioOption;
    private int authorsScenario;

    public int getAuthorScenarioOption() {
        return authorScenarioOption;
    }

    public void setAuthorScenarioOption(int authorScenarioOption) {
        this.authorScenarioOption = authorScenarioOption;
    }

    public int getAuthorsScenario() {
        return authorsScenario;
    }

    public void setAuthorsScenario(int authorsScenario) {
        this.authorsScenario = authorsScenario;
    }

    public int getLenghtOption() {
        return lenghtOption;
    }

    public void setLenghtOption(int lenghtOption) {
        this.lenghtOption = lenghtOption;
    }

    public int getScenario() {
        return scenario;
    }

    public void setScenario(int scenario) {
        this.scenario = scenario;
    }

    public int getScenarioLenghtFrom() {
        return scenarioLenghtFrom;
    }

    public void setScenarioLenghtFrom(int scenarioLenghtFrom) {
        this.scenarioLenghtFrom = scenarioLenghtFrom;
    }

    public int getScenarioLenghtTo() {
        return scenarioLenghtTo;
    }

    public void setScenarioLenghtTo(int scenarioLenghtTo) {
        this.scenarioLenghtTo = scenarioLenghtTo;
    }

    public int getScenarioOption() {
        return scenarioOption;
    }

    public void setScenarioOption(int scenarioOption) {
        this.scenarioOption = scenarioOption;
    }
}
