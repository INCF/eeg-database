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
 *   ExperimentsServicePerformanceTest.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.perf.persistentLayer;

import cz.zcu.kiv.eegdatabase.data.dao.*;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;

import java.sql.Blob;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Kabourek
 * Date: 17.5.11
 * Time: 20:05
 * To change this template use File | Settings | File Templates.
 * Identificator of test /PPT_E_1_WorWitExp_L/. Contains document Testovaci scenare.docx.
 */
public class ExperimentsServicePerformanceTest extends PerformanceTest {

    @Autowired
    ExperimentDao experimentDao;

    @Autowired
    PersonDao personeDao;

    @Autowired
    HardwareDao hardwareDao;

    @Autowired
    HistoryDao historyDao;

    @Autowired
    WeatherDao weatherDao;

    @Autowired
    ScenarioDao scenarioDao;


    private Experiment experiment;
    private Hardware hardware;
    private History history;
    private Person person;
    private Weather weather;
    private Scenario scenario;


    /**
     * Method test create experiment.
     * Identificator of test /PPT_E_2_AddExp_F/. Contains document Testovaci scenare.docx.
     */
    @Test
    public void testCreateExperiment(){
        experiment = new Experiment();
        hardware = new Hardware();
        history = new History();
        person = personeDao.getPerson("kaby");

        hardware.setTitle("hardware");
        hardware.setDescription("testovaci");
        hardware.setType("type");
        hardwareDao.create(hardware);

        ResearchGroup group = new ResearchGroup();
        group.setResearchGroupId(6);


        scenario = new Scenario();
        scenario.setResearchGroup(group);
        scenario.setDescription("description test");
        scenario.setMimetype("test");
        scenario.setPerson(person);
        scenario.setResearchGroup(group);
        scenario.setScenarioName("scenario name test");
        scenario.setScenarioLength(10);
        scenarioDao.create(scenario);

        history.setPerson(personeDao.getPerson("kaby"));
        history.setDateOfDownload(new Timestamp(new Date().getTime()));
        historyDao.create(history);

        weather = new Weather();
        weather.setDescription("test weather");
        weather.setTitle("test weather title");
        weatherDao.create(weather);



        experiment.setPrivateExperiment(true);
        experiment.setWeather(weather);
        experiment.setScenario(scenario);
        experiment.setHardwares(Collections.singleton(hardware));
        experiment.setHistories(Collections.singleton(history));
        experiment.setPersons(Collections.singleton(personeDao.getPerson("kaby")));
        experiment.setPersonBySubjectPersonId(person);
        experiment.setPersonByOwnerId(person);
        experiment.setResearchGroup(group);
        experimentDao.create(experiment);

    }

   /**
     * Method test edit experiment.
     * Identificator of test /PPT_E_3_EdiExp_F/. Contains document Testovaci scenare.docx.
     */
    @Test
    public void testEditExperimetn(){
        testCreateExperiment();
        experimentDao.update(experiment);
    }
    /**
     * Method test insert file to experiment.
     * Identificator of test /PPT_E_4_AddDatFil_F/. Contains document Testovaci scenare.docx.
     */
    //@Test
    public void insertFileToExperimentTest(){

    }
}
