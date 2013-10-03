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
 *   PersonDaoTest.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
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
