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
 *   SimpleCSVFactory.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.csv;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import cz.zcu.kiv.eegdatabase.data.dao.ExperimentDao;
import cz.zcu.kiv.eegdatabase.data.dao.ScenarioDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;

/**
 * Generating csv from experiments or scenarios
 * User: pbruha
 * Date: 13.12.11
 * Time: 6:56
 * To change this template use File | Settings | File Templates.
 */
public class SimpleCSVFactory implements CSVFactory {
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private ScenarioDao scenarioDao;
    @Autowired
    private ExperimentDao experimentDao;

    private String domain;

    /**
     * Generating csv file from scenarios
     *
     * @return csv file with scenarios
     * @throws IOException - error writing to stream
     */
    @Transactional(readOnly = true)
    public OutputStream generateScenariosCsvFile() throws IOException {

        log.debug("Creating output stream");
        OutputStream out = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(out);
        List<Scenario> scenarioList = scenarioDao.getAllRecords();
        log.debug("Creating table header");
        printStream.println(CSVUtils.SCENARIO_TITLE + CSVUtils.SEMICOLON + CSVUtils.SCENARIO_LENGTH + CSVUtils.SEMICOLON + CSVUtils.SCENARIO_DESCRIPTION + CSVUtils.SEMICOLON + CSVUtils.SCENARIO_DETAILS);
        log.debug("Printing experiments to outputstream");
        for (int i = 0; i < scenarioList.size(); i++) {
            printStream.println(scenarioList.get(i).getTitle() + CSVUtils.SEMICOLON + scenarioList.get(i).getScenarioLength() + CSVUtils.SEMICOLON + scenarioList.get(i).getDescription() + CSVUtils.SEMICOLON + CSVUtils.PROTOCOL_HTTP + domain + CSVUtils.SCENARIO_URL + scenarioList.get(i).getScenarioId());
        }
        printStream.close();

        return out;
    }

    /**
     * Generating csv file from experiments
     *
     * @return csv file with experiments
     * @throws IOException - error writing to stream
     */
    @Transactional(readOnly = true)
    public OutputStream generateExperimentsCsvFile() throws IOException {
        log.debug("Generating csv file from experiments");
        log.debug("Creating output stream");
        String usedHardware = "";
        OutputStream out = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(out);
        log.debug("Loading all experiments");
        List<Experiment> experimentList = experimentDao.getAllRecords();
        log.debug("Creating table header");
        printStream.println(CSVUtils.EXPERIMENT_SUBJECT + CSVUtils.SEMICOLON + CSVUtils.EXPERIMENT_GENDER + CSVUtils.SEMICOLON + CSVUtils.EXPERIMENT_YEAR_OF_BIRTH + CSVUtils.SEMICOLON + CSVUtils.SCENARIO_TITLE + CSVUtils.SEMICOLON + CSVUtils.EXPERIMENT_USED_HARDWARE + CSVUtils.SEMICOLON + CSVUtils.EXPERIMENT_DETAILS);
        log.debug("Printing experiments to outputstream");
        for (int i = 0; i < experimentList.size(); i++) {
            usedHardware = getHardware(experimentList.get(i).getHardwares());
            printStream.println(CSVUtils.EEGBASE_SUBJECT_PERSON + experimentList.get(i).getPersonBySubjectPersonId().getPersonId() + CSVUtils.SEMICOLON + experimentList.get(i).getPersonBySubjectPersonId().getGender() + CSVUtils.SEMICOLON + getDateOfBirth(experimentList.get(i).getPersonBySubjectPersonId().getDateOfBirth()) + CSVUtils.SEMICOLON + experimentList.get(i).getScenario().getTitle() + CSVUtils.SEMICOLON + usedHardware + CSVUtils.SEMICOLON + CSVUtils.PROTOCOL_HTTP + domain + CSVUtils.EXPERIMENT_URL + experimentList.get(i).getExperimentId());
        }
        log.debug("Close printing stream");
        printStream.close();

        return out;
    }

    /**
     * Returns used hardware(String)
     *
     * @param hardwareSet - used hardware
     * @return used hardware(String)
     */
    private String getHardware(Set<Hardware> hardwareSet) {
        String usedHardware = "";
        int countOfHardware = 0;
        for (Hardware hardware : hardwareSet) {
            if (countOfHardware > 0) {
                usedHardware = usedHardware + ","+hardware.getTitle();
            } else {
            usedHardware = usedHardware + hardware.getTitle();
            }
            countOfHardware++;
        }
        return usedHardware;
    }

    /**
     * Get date of birth
     *
     * @param date - date of birth
     * @return only date without time
     */
    private String getDateOfBirth(Timestamp date) {
        String[] dateOfBirth = null;
        String noDate="-";
        if (date != null) {
            dateOfBirth = date.toString().split(" ");
            return dateOfBirth[0];
        }
        return noDate;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
