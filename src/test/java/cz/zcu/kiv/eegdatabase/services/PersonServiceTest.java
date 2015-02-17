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
 * PersonServiceTest.java, 2014/08/01 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.services;

import java.math.BigDecimal;

import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.dao.LicenseDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonService;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Assert;
import org.testng.annotations.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeMethod;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by stebjan on 1.8.2014.
 */
public class PersonServiceTest extends AbstractServicesTest {

    @Autowired
    private ResearchGroupDao researchGroupDao;

    @Autowired
    private LicenseDao licenseDao;

    @Autowired
    private PersonService personService;

    private ResearchGroup researchGroup;

    private Person person;

    @Autowired
    private PersonDao personDao;

    @BeforeMethod(groups = "unit")
    public void setUp() {

        person = TestUtils.createPersonForTesting("test@test.com", Util.ROLE_READER);
        License license = new License();
        license.setDescription("junit@test.description");
        license.setLicenseId(-231);
        license.setPrice(BigDecimal.valueOf(-1000f));
        license.setTitle("title");
        license.setLicenseType(LicenseType.OPEN_DOMAIN);
        licenseDao.create(license);


    }

    @Test(groups = "unit")
    public void testCreatePerson() {
        int count = personService.getCountRecords();
        personDao.create(person);
        assertEquals(count + 1, personService.getCountRecords());
        assertEquals(count + 1, personService.getCountForList());
        assertNotNull(personService.getPerson("test@test.com"));
    }

    @Test(groups = "unit")
    public void testGetPersonForDetail() {

        int id = personDao.create(person);
        Person fromDB = personService.getPersonForDetail(id);
        assertEquals("test@test.com", fromDB.getUsername());
        assertEquals("test-surname", fromDB.getSurname());
        assertEquals("test-name", fromDB.getGivenname());
        assertEquals('M', fromDB.getGender());

    }

    @Test(groups = "unit")
    public void testChangeUserPassword() {

        personDao.create(person);
        personService.changeUserPassword(person.getUsername(), "newPassword");

        assertTrue(personService.isPasswordEquals(person.getUsername(), "newPassword"));

    }

    @Test(groups = "unit")
    public void testUsernameExists() {

        personDao.create(person);

        assertFalse(personService.usernameExists("xxxt@test.com"));
        assertTrue(personService.usernameExists("test@test.com"));

    }

    @Test(groups = "unit")
    public void testUsernameInGroup() {

        personDao.create(person);
        researchGroup = new ResearchGroup();
        researchGroup.setDescription("test-description");
        researchGroup.setTitle("test-title");
        researchGroup.setPerson(person);

        int groupId = researchGroupDao.create(researchGroup);
        Assert.assertTrue(personService.userNameInGroup(person.getUsername(), groupId));

    }
}
