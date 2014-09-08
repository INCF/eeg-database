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
 * ExperimentServiceTest.java, 2014/09/01 00:01 Jan Stebetak
 *****************************************************************************
 */
package cz.zcu.kiv.eegdatabase.services;

import cz.zcu.kiv.eegdatabase.data.ExperimentGenerator;
import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Software;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by stebjan on 1.9.2014.
 */
@Transactional
public class ExperimentServiceTest extends AbstractServicesTest {

    @Autowired
    private ExperimentsService experimentsService;

    @Autowired
    private PersonDao personDao;
    @Autowired
    private ExperimentGenerator experimentGenerator;

    private Person person;
    private Experiment experiment;

    @Before
    public void setUp() {

        person = TestUtils.createPersonForTesting("test@test.com", Util.ROLE_READER);
        personDao.create(person);
        experiment = experimentGenerator.generateExperiment(person);

    }

    @Test
    public void testCreateExperiment() {

        experimentsService.create(experiment);
        assertNotNull(experimentsService.read(experiment.getExperimentId()));
        assertEquals(1, experimentsService.getCountRecords());

    }
    @Test
    public void testGetCountExperimentsWhereOwner() {
        int count = experimentsService.getCountForExperimentsWhereOwner(person);
        experimentsService.create(experiment);
        Person person1 = TestUtils.createPersonForTesting("test2@test.com", Util.ROLE_READER);
        personDao.create(person1);
        Experiment exp = experimentGenerator.generateExperiment(person);
        exp.setPersonByOwnerId(person1);
        experimentsService.create(exp);
        assertEquals(count + 1, experimentsService.getCountForExperimentsWhereOwner(person));

    }

    @Test
    public void testGetCountExperimentsWhereSubject() {
        int countForOwner = experimentsService.getCountForExperimentsWhereOwner(person);
        int countForSubject = experimentsService.getCountForExperimentsWhereSubject(person);
        experimentsService.create(experiment);
        Person person1 = TestUtils.createPersonForTesting("test2@test.com", Util.ROLE_READER);
        personDao.create(person1);
        Experiment exp = experimentGenerator.generateExperiment(person);
        exp.setPersonBySubjectPersonId(person1);
        experimentsService.create(exp);
        assertEquals(countForOwner + 2, experimentsService.getCountForExperimentsWhereOwner(person));
        assertEquals(countForSubject + 1, experimentsService.getCountForExperimentsWhereSubject(person));

    }

    @Test
    public void testGetExperimentForDetail() {

        experimentsService.create(experiment);
        Experiment exp = experimentsService.getExperimentForDetail(experiment.getExperimentId());
        assertEquals("testTitle", exp.getResearchGroup().getTitle());
        assertEquals("test@test.com", exp.getPersonBySubjectPersonId().getUsername());
        assertEquals("testTitleWeather", exp.getWeather().getTitle());
        for (Software sw : exp.getSoftwares()) {
            assertEquals("testTitleSW", sw.getTitle());
        }
    }


}
