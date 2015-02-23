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
 *   ScenarioDaoTest.java, 2014/05/06 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.logic.Util;
import org.testng.annotations.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;

import static org.testng.Assert.*;

public class ScenarioDaoTest extends AbstractDataAccessTest {

    @Autowired
    private ScenarioDao scenarioDao;
    @Autowired
    private PersonDao personDao;
    @Autowired
    private ResearchGroupDao researchGroupDao;

    private Scenario scenario;
    private Person person;
    private ResearchGroup group;

    @BeforeMethod(groups = "unit")
    public void setUp() throws Exception {
        person = TestUtils.createPersonForTesting("test@test.com", Util.ROLE_READER);

        personDao.create(person);


        scenario = new Scenario();
        scenario.setTitle("test-title");
        scenario.setDescription("test-description-test");
        scenario.setScenarioName("test-scenarioName-test");
        scenario.setAvailableFile(false);
        scenario.setScenarioLength(100);
        scenario.setPrivateScenario(false);
        scenario.setUserMemberOfGroup(false);
        scenario.setMimetype("test-mimetype");
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
    }

    @Test(groups = "unit")
    public void testCreateScenario() {
        int count = scenarioDao.getCountRecords();
        scenarioDao.create(scenario);
        assertEquals(count + 1, scenarioDao.getCountRecords());
        assertEquals("test@test.com", scenarioDao.read(scenario.getScenarioId()).getPerson().getUsername());
    }

    @Test(groups = "unit")
    public void testGetScenariosWhereOwner() {
        int count = scenarioDao.getCountRecords();
        int countOfOwner = scenarioDao.getScenariosWhereOwner(person).size();
        scenarioDao.create(scenario);
        Person person1 = TestUtils.createPersonForTesting("test.test2@test.com", Util.ROLE_ADMIN);
        personDao.create(person1);
        Scenario scen = fork("title");
        scen.setPerson(person1);
        scenarioDao.create(scen);
        assertEquals(countOfOwner + 1, scenarioDao.getScenariosWhereOwner(person).size());
        assertEquals(count + 2, scenarioDao.getCountRecords());
    }

//    @Test(groups = "unit")
//    public void testUniqueScenarioTitle() {
//        scenarioDao.create(scenario);
//        Scenario clone = fork(scenario.getTitle());
//        try {
//            scenarioDao.create(clone);
//        } catch (Exception e) {
//            assertTrue(e instanceof DataIntegrityViolationException);
//        } finally {
//            assertNotNull(scenarioDao.read(scenario.getScenarioId()));
//            System.out.println("comparison " + scenario.getTitle() + " " + clone.getTitle());
//            assertNotNull("Second scenario with the same title cannot be stored ",
//                    scenarioDao.read(clone.getScenarioId()));
//
//
//        }
//    }

    @Test(groups = "unit")
    public void testGetScenariosForList() {
        int count = scenarioDao.getCountRecords();
        scenarioDao.create(scenario);
        Scenario clone = fork("title");
        scenarioDao.create(clone);
        assertEquals(count + 2, scenarioDao.getScenariosForList(person, 0, 200).size());

        assertEquals(1, scenarioDao.getScenariosForList(person, 0, 1).size());
        //scenarioDao.delete(clone);
    }
    @Test(groups = "unit")
    public void testEditScenario() {
        int count = scenarioDao.getCountRecords();
        int id = scenarioDao.create(scenario);
        assertEquals(count + 1, scenarioDao.getCountRecords());
        scenario.setDescription("new desc");
        scenarioDao.update(scenario);
        assertEquals(count + 1, scenarioDao.getCountRecords());

        assertEquals("new desc" , scenarioDao.read(id).getDescription());
    }

    private Scenario fork(String title) {
        Scenario scenario = new Scenario();
        scenario.setTitle(title);
        scenario.setDescription("test-description-test_clone");
        scenario.setScenarioName("test-scenarioName-test_clone");
        scenario.setAvailableFile(false);
        scenario.setScenarioLength(100);
        scenario.setPrivateScenario(false);
        scenario.setUserMemberOfGroup(false);
        scenario.setMimetype("test-mimetype_clone");
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
