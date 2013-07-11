package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.service.PersonService;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

/**
 * User: Tomas Pokryvka
 * Date: 26.4.13
 */
public class PersonDaoTest extends AbstractDataAccessTest {

  @Autowired
  protected PersonDao personDao;

  @Autowired
  protected EducationLevelDao educationLevelDao;

  @Autowired
  protected PersonService personService;

  protected BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
  protected Person person;
  protected EducationLevel educationLevel;
  protected String password;


  @Before
  public void init() {
    educationLevel = new EducationLevel();
    educationLevel.setDefaultNumber(0);
    educationLevel.setTitle("test-title");
    educationLevel.setEducationLevelId(0);

    password = ControllerUtils.getRandomPassword();
    person = new Person();
    person.setUsername("ADMIN1@test.test");
    person.setAuthority(Util.ROLE_ADMIN);
    person.setPassword(encoder.encode(password));
    person.setSurname("test-surname");
    person.setGivenname("test-name");
    person.setGender('M');
    person.setLaterality('X');
    person.setEducationLevel(educationLevel);
  }

  @Test
  @Transactional
  public void testCreateEducationLevel() {
    int count = educationLevelDao.getCountRecords();
    educationLevelDao.create(educationLevel);
    assertEquals(count + 1, educationLevelDao.getCountRecords());
  }

  @Test
  public void testVerifyPassword() {
    assertTrue(encoder.matches(password, person.getPassword()));
  }

  @Test
  @Transactional
  public void testCreatePerson() {
    educationLevelDao.create(educationLevel);
    person.setEducationLevel(educationLevel);
    int count = personDao.getCountRecords();
    personDao.create(person);
    assertNotNull(personDao.read(person.getPersonId()));
    assertEquals(count + 1, personDao.getCountRecords());
  }
}
