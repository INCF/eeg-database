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
 *   AddExperimentCommand.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.experiment;

/**
 * @author JiPER
 */
public class AddExperimentCommand {

    private int measurationId;
    private int researchGroup;
    private String researchGroupTitle;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private int subjectPerson;
    private int[] coExperimenters;
    private int scenario;
    private int[] hardware;
    private int weather;
    private String weatherNote;
    private String temperature;
    private boolean privateNote;

    public int getResearchGroup() {
        return researchGroup;
    }

    public void setResearchGroup(int researchGroup) {
        this.researchGroup = researchGroup;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int[] getCoExperimenters() {
        return coExperimenters;
    }

    public void setCoExperimenters(int[] coExperimenters) {
        this.coExperimenters = coExperimenters;
    }

    public int[] getHardware() {
        return hardware;
    }

    public void setHardware(int[] hardware) {
        this.hardware = hardware;
    }

    public int getMeasurationId() {
        return measurationId;
    }

    public void setMeasurationId(int measurationId) {
        this.measurationId = measurationId;
    }

    public int getSubjectPerson() {
        return subjectPerson;
    }

    public void setSubjectPerson(int subjectPerson) {
        this.subjectPerson = subjectPerson;
    }

    public int getScenario() {
        return scenario;
    }

    public void setScenario(int scenario) {
        this.scenario = scenario;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setPrivateNote(boolean privateNote) {
        this.privateNote = privateNote;
    }

    public boolean isPrivateNote() {
        return privateNote;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public int getWeather() {
        return weather;
    }

    public void setWeather(int weather) {
        this.weather = weather;
    }

    public String getWeatherNote() {
        return weatherNote;
    }

    public void setWeatherNote(String weatherNote) {
        this.weatherNote = weatherNote;
    }

    public String getResearchGroupTitle() {
        return researchGroupTitle;
    }

    public void setResearchGroupTitle(String researchGroupTitle) {
        this.researchGroupTitle = researchGroupTitle;
    }
}
