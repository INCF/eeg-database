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
 *   RegistrationTest.java, 2014/03/31 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.Util;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

/**
 * User: stebjan
 * Date: 31.3.2014
 */
public class RegistrationTest extends AbstractDataAccessTest {

    @Autowired
    private PersonDao personDao;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private Person person;
    private String password;
    private String username;


    @Before
    public void setUp() {

        person = TestUtils.createReaderPersonForTesting();
        username = person.getUsername();
        password = person.getPassword();
       if (personDao.getPerson(username) != null) {
            personDao.delete(personDao.getPerson(username));
       }
    }

    @Test
    public void testCreatePerson() {
        storePerson(person);
        Person tmp = personDao.read(person.getPersonId());
        assertNotNull(tmp);
        assertEquals((username).toLowerCase(), tmp.getUsername());
       }

    @Test
    public void testVerifyPassword() {
        storePerson(person);
        Person tmp = personDao.read(person.getPersonId());
        assertNotNull(tmp);
        assertEquals(password, tmp.getPassword());
    }



    @Test
    @Transactional
    public void testNotNullUsername() {
        try {
            person.setUsername(null);
            personDao.update(person);
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);

        } finally {
            Person tmp = personDao.read(person.getPersonId());
            assertNull(tmp);
        }
    }

    @Test
    public void testUniqueUsername() {
        storePerson(person);
        Person clone = fork(personDao.read(person.getPersonId()));
        try {
            storePerson(clone);
        } catch (Exception e) {
            assertTrue(e instanceof DataIntegrityViolationException);
        } finally {
            assertNotNull(personDao.read(person.getPersonId()));
            assertNull("Second person with the same username cannot be stored ",
                    personDao.read(clone.getPersonId()));

        }
    }

    private Person fork(Person person) {
        Person clone = new Person();
        clone.setUsername(person.getUsername());
        clone.setPassword(person.getPassword());
        clone.setAuthority(Util.ROLE_USER);
        clone.setSurname(person.getSurname() + "_clone");
        clone.setGivenname(person.getGivenname() + "_clone");
        clone.setGender(person.getGender());
        return clone;

    }

    @Transactional
    private void storePerson(Person person) {
       // if (personDao.getPerson(person.getUsername()) == null) {
            personDao.create(person);
      //  }


    }
}
