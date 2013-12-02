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
 *   ScenarioDaoTest.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.data.pojo.ScenarioType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

/**
 * User: Tomas Pokryvka
 * Date: 26.4.13
 */
public class ScenarioDaoTest extends AbstractDataAccessTest {

  @Autowired
  protected ScenarioDao scenarioDao;
  @Autowired
  protected ScenarioTypeDao scenarioTypeDao;
  @Autowired
  protected PersonDao personDao;
  @Autowired
  protected ScenarioTypeDao getScenarioTypeDao;
  @Autowired
  protected ResearchGroupDao researchGroupDao;

  protected Scenario scenario;
  protected ScenarioType scenarioType;
  protected Person person;
  protected ReservationDao reservationDao;

  @Before
  public void setUp() throws Exception {
    person = personDao.getPerson("testaccountforeeg2@seznam.cz");

    scenario = new Scenario();
    scenario.setTitle("test-title-title");
    scenario.setDescription("test-description-test");
    scenario.setScenarioName("test-scenarioName-test");
    scenario.setAvailableFile(false);
    scenario.setScenarioLength(100);
    scenario.setPrivateScenario(false);
    scenario.setUserMemberOfGroup(false);
    scenario.setMimetype("test-Mimetype");
    scenario.setPerson(person);

    scenarioType = new ScenarioType();
    scenarioType.setScenarioId(0);
    scenarioType.setScenario(scenario);

    scenario.setResearchGroup(researchGroupDao.getAllRecords().get(0));
  }

  @Test
  @Transactional
  public void testCreateScenario(){
    int count = scenarioDao.getCountRecords();
    scenarioDao.create(scenario);
    assertEquals(count + 1, scenarioDao.getCountRecords());
  }

  @Test
  @Transactional
  public void testGetScenariosWhereOwner(){
    int count = scenarioDao.getScenariosWhereOwner(person).size();
    scenarioDao.create(scenario);
    assertEquals(count + 1, scenarioDao.getScenariosWhereOwner(person).size());
  }

  @Test
  @Transactional
  public void testGetScenarioCountForList(){
    int count = scenarioDao.getScenarioCountForList(person);
    scenarioDao.create(scenario);
    assertEquals(count + 1, scenarioDao.getScenarioCountForList(person));
  }

  @Test
  @Transactional
  public void testGetScenariosForList(){
    int count = scenarioDao.getScenariosForList(person, 0, 200).size();
    scenarioDao.create(scenario);
    assertEquals(count + 1, scenarioDao.getScenariosForList(person, 0, 200).size());

    assertEquals(10, scenarioDao.getScenariosForList(person, 0, 10).size());
  }
}
