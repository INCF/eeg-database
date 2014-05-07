/**
 * *****************************************************************************
 * This file is part of the EEG-database project
 *
 * ==========================================
 *
 * Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 *  ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 *  ***********************************************************************************************************************
 *
 * ScenariosServiceImpl.java, 2014/04/31 00:01 Jan Stebetak
 *****************************************************************************
 */
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.logic.Util;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Honza on 1.5.14.
 */
public class ExperimentDaoTest extends AbstractDataAccessTest {

    @Autowired
    private PersonDao personDao;
    @Autowired
    private DigitizationDao digitizationDao;
    @Autowired
    private ExperimentDao experimentDao;

    @Autowired
    private ResearchGroupDao groupDao;
    @Autowired
    private HardwareDao hardwareDao;
    @Autowired
    private SimpleSoftwareDao softwareDao;
    @Autowired
    private WeatherDao weatherDao;

    @Autowired
    private ScenarioDao scenarioDao;

    private Person person;
    private ResearchGroup group;
    private Hardware hw;
    private Software sw;
    private Weather weather;
    private Scenario scenario;
    private Digitization digitization;
    private Experiment experiment;

    @Before
    public void setUp() {
        person = createPerson();
        group = createGroup();
        hw = createHardware();
        sw = createSoftware();
        weather = createWeather();
        scenario = createScenario();

        digitization = new Digitization();
        digitization.setFilter("testFilter");
        digitization.setGain(1f);
        digitization.setSamplingRate(1000f);
        digitizationDao.create(digitization);
        experiment = setUpExperiment();

    }

    @Test
    @Transactional
    public void testCreateExperiment() {

//        experimentDao.create(experiment);
//        assertNotNull(experimentDao.read(experiment.getExperimentId()));

    }

    @After
    public void clean() {
        if (person.getUsername() != null) {
            personDao.delete(person);
        }
    }

    private Person createPerson() {
        Person person = TestUtils.createPersonForTesting("test@test.com", Util.ROLE_READER);
        personDao.create(person);

        return person;
    }

    private ResearchGroup createGroup() {
        ResearchGroup group = new ResearchGroup();
        group.setTitle("testTitle");
        group.setDescription("testDesc");
        group.setPerson(person);
        groupDao.create(group);
        return group;
    }
    private Hardware createHardware() {
        Hardware hardware = new Hardware();
        hardware.setTitle("testTitle");
        hardware.setDescription("testDesc");
        hardware.setType("testType");
        hardwareDao.create(hardware);
        return hardware;
    }

    private Software createSoftware() {
        Software software = new Software();
        software.setDescription("testDesc");
        software.setTitle("testTitle");
        softwareDao.create(software);
        return software;
    }

    private Weather createWeather() {
        Weather weather = new Weather();
        weather.setTitle("testTitle");
        weather.setDescription("testDesc");
        weatherDao.create(weather);
        return weather;
    }


    private Scenario createScenario() {
        Scenario scenario = new Scenario();
        scenario.setDescription("testDesc");
        scenario.setTitle("testTitle");
        scenario.setScenarioLength(10);
        scenario.setPerson(person);
        scenario.setResearchGroup(group);
        scenarioDao.create(scenario);
        return scenario;
    }

    private Experiment setUpExperiment() {
        Experiment exp = new Experiment();
        exp.setScenario(scenario);
        exp.setPersonByOwnerId(person);
        exp.setResearchGroup(group);
        exp.getHardwares().add(hw);
        exp.getSoftwares().add(sw);
        exp.setPersonBySubjectPersonId(person);
        exp.setWeather(weather);
        exp.setStartDate(new Date(10));
        exp.setStartTime(new Timestamp(11));
        exp.setEndTime(new Timestamp(13));
        exp.setFinishDate(new Date(12));
        exp.setDigitization(digitization);
        return exp;
    }
}
