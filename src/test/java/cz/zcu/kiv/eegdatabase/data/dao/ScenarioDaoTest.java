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

    scenario.setScenarioType(scenarioType);
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