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
 *   CSVUtils.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.csv;

import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: pbruha
 * Date: 13.12.11
 * Time: 13:11
 * To change this template use File | Settings | File Templates.
 */
public class CSVUtils {
    public static final String SCENARIO_URL = "/scenarios/detail.html?scenarioId=";
    public static final String EXPERIMENT_URL = "/experiments/detail.html?experimentId=";
    public static final String SCENARIO_TITLE = "Scenario title";
    public static final String SCENARIO_LENGTH = "Length";
    public static final String SCENARIO_DESCRIPTION = "Description";
    public static final String SCENARIO_DETAILS = "Scenario details";
    public static final String SEMICOLON = ";";
    public static final String EXPERIMENT_SUBJECT = "Subject";
    public static final String EXPERIMENT_GENDER = "Gender";
    public static final String EXPERIMENT_YEAR_OF_BIRTH = "Year of birth";
    public static final String EXPERIMENT_USED_HARDWARE = "Used hardware";
    public static final String EXPERIMENT_DETAILS = "Experiment details";
    public static final String EEGBASE_SUBJECT_PERSON = "EEGbase_subject_person_";
    public static final String PROTOCOL_HTTP="http://";

}
