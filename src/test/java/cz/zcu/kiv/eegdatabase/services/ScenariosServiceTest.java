/*******************************************************************************
 * This file is part of the EEG-database project
 *
 * ==========================================
 *
 * Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 * ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * ***********************************************************************************************************************
 *
 * ScenarioServicesTest.java, 2014/07/29 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.services;

import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.ScenariosService;
import org.testng.annotations.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;

import static org.testng.Assert.*;

/**
 * Created by stebjan on 29.7.2014.
 */
public class ScenariosServiceTest extends AbstractServicesTest {

    @Autowired
    private ScenariosService scenariosService;
    @Autowired
    private PersonDao personDao;
    @Autowired
    private ResearchGroupDao researchGroupDao;

    private Scenario scenario;
    private Person person;


    @BeforeMethod(groups = "unit")
    public void setUp() throws Exception {
        person = TestUtils.createPersonForTesting("test@test.com", Util.ROLE_READER);

        personDao.create(person);

        scenario = createScenario("test_title");
    }

    @Test(groups = "unit")
    public void testCreateScenario() {
        int count = scenariosService.getCountRecords();
        scenariosService.create(scenario);
        assertEquals(count + 1, scenariosService.getCountRecords());
        assertNotNull(scenariosService.getScenarioByTitle("test_title"));
    }

    @Test(groups = "unit")
    public void testGetScenariosWhereOwner() {
        int count = scenariosService.getCountRecords();
        int countOfOwner = scenariosService.getScenariosWhereOwner(person).size();
        scenariosService.create(scenario);
        Person person1 = TestUtils.createPersonForTesting("test.test2@test.com", Util.ROLE_ADMIN);
        personDao.create(person1);
        Scenario scen = createScenario("title");
        scen.setPerson(person1);
        scenariosService.create(scen);
        assertEquals(countOfOwner + 1, scenariosService.getScenariosWhereOwner(person).size());
        assertEquals(count + 2, scenariosService.getCountRecords());
    }

    @Test(groups = "unit")
    public void testCanSaveTitle() {

        scenariosService.create(scenario);
        assertTrue(scenariosService.canSaveTitle(scenario.getTitle(), scenario.getScenarioId()));
        assertFalse(scenariosService.canSaveTitle(scenario.getTitle(), -1));

    }

    private Scenario createScenario(String title) {
        Scenario scenario = new Scenario();
        scenario.setTitle(title);
        scenario.setDescription("test-description-test");
        scenario.setScenarioName("test-scenarioName-test");
        scenario.setAvailableFile(false);
        scenario.setScenarioLength(100);
        scenario.setPrivateScenario(false);
        scenario.setUserMemberOfGroup(false);
        scenario.setMimetype("test-mimetype_clone");
        ResearchGroup group;
        scenario.setPerson(person);
        if (researchGroupDao.getAllRecords().size() == 0) {
            group = new ResearchGroup();
            group.setTitle("title");
            group.setDescription("description");
            group.setPerson(person);
            researchGroupDao.create(group);
        } else {
            group = researchGroupDao.getAllRecords().get(0);
        }
        scenario.setResearchGroup(group);
        return scenario;
    }
}
