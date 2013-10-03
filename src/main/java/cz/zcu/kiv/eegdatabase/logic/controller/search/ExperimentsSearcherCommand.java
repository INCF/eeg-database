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
 *   ExperimentsSearcherCommand.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.logic.controller.search;

/**
 * @author pbruha
 */
public class ExperimentsSearcherCommand {

    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private int scenario;
    private int[] hardware;
    private String gender;
    private int dateOption;
    private int genderOption;
    private int scenarioOption;
    private int hardwareOption;
    private int ageOption;
    private String fromDate;
    private String toDate;
    private int scenarioLenghtFrom;
    private int scenarioLenghtTo;


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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int[] getHardware() {
        return hardware;
    }

    public void setHardware(int[] hardware) {
        this.hardware = hardware;
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

    public int getDateOption() {
        return dateOption;
    }

    public void setDateOption(int dateOption) {
        this.dateOption = dateOption;
    }

    public int getGenderOption() {
        return genderOption;
    }

    public void setGenderOption(int genderOption) {
        this.genderOption = genderOption;
    }

    public int getScenarioOption() {
        return scenarioOption;
    }

    public void setScenarioOption(int scenarioOption) {
        this.scenarioOption = scenarioOption;
    }

    public int getHardwareOption() {
        return hardwareOption;
    }

    public void setHardwareOption(int hardwareOption) {
        this.hardwareOption = hardwareOption;
    }

    public int getAgeOption() {
        return ageOption;
    }

    public void setAgeOption(int ageOption) {
        this.ageOption = ageOption;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
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

}
