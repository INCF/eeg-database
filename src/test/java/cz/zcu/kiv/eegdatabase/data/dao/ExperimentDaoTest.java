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
* ExperimentDaoTest.java, 2014/04/31 00:01 Jan Stebetak
*****************************************************************************
*/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.ExperimentGenerator;
import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.logic.Util;

import org.testng.annotations.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;

import static org.testng.Assert.*;

/**
* Created by Honza on 1.5.14.
*/
public class ExperimentDaoTest extends AbstractDataAccessTest {

    @Autowired
    private PersonDao personDao;

    @Autowired
    private ExperimentDao experimentDao;

    @Autowired
    private ExperimentGenerator experimentGenerator;

    private Person person;
    private Experiment experiment;

    @BeforeMethod(groups = "unit")
    public void setUp() {

        person = createPerson();
        experiment = experimentGenerator.generateExperiment(person);

    }

    @Test(groups = "unit")
    public void testCreateExperiment() {

        experimentDao.create(experiment);
        assertNotNull(experimentDao.read(experiment.getExperimentId()));
        assertEquals(1, experimentDao.getCountRecords());

    }

    @Test(groups = "unit")
    public void testGetCountExperimentsWhereOwner() {
        int count = experimentDao.getCountForExperimentsWhereOwner(person);
        experimentDao.create(experiment);
        Person person1 = TestUtils.createPersonForTesting("test2@test.com", Util.ROLE_READER);
        personDao.create(person1);
        Experiment exp = experimentGenerator.generateExperiment(person);
        exp.setPersonByOwnerId(person1);
        experimentDao.create(exp);
        assertEquals(count + 1, experimentDao.getCountForExperimentsWhereOwner(person));

    }

    @Test(groups = "unit")
    public void testGetCountExperimentsWhereSubject() {
        int countForOwner = experimentDao.getCountForExperimentsWhereOwner(person);
        int countForSubject = experimentDao.getCountForExperimentsWhereSubject(person);
        experimentDao.create(experiment);
        Person person1 = TestUtils.createPersonForTesting("test2@test.com", Util.ROLE_READER);
        personDao.create(person1);
        Experiment exp = experimentGenerator.generateExperiment(person);
        exp.setPersonBySubjectPersonId(person1);
        experimentDao.create(exp);
        assertEquals(countForOwner + 2, experimentDao.getCountForExperimentsWhereOwner(person));
        assertEquals(countForSubject + 1, experimentDao.getCountForExperimentsWhereSubject(person));

    }

    @Test(groups = "unit")
    public void testGetExperimentForDetail() {

        experimentDao.create(experiment);
        Experiment exp = experimentDao.getExperimentForDetail(experiment.getExperimentId());
        assertEquals("testTitle", exp.getResearchGroup().getTitle());
        assertEquals("test@test.com", exp.getPersonBySubjectPersonId().getUsername());
        assertEquals("testTitleWeather", exp.getWeather().getTitle());
        for (Software sw: exp.getSoftwares()) {
            assertEquals("testTitleSW", sw.getTitle());
        }




    }

    private Person createPerson() {
        Person person = TestUtils.createPersonForTesting("test@test.com", Util.ROLE_READER);
        personDao.create(person);

        return person;
    }


}
