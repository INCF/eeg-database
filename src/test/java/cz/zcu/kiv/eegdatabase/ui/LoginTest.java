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
 * LoginTest.java, 2014/09/10 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.ui;


import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.Util;
import net.sourceforge.jwebunit.junit.WebTester;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

/**
 * Created by stebjan on 10.9.2014.
 */
public class LoginTest extends AbstractUITest {

    private Person person;

    @Autowired
    private PersonDao personDao;

    private WebTester tester;

    @Before
    @Transactional
    public void createPerson() {
//        person = TestUtils.createPersonForTesting("test@test.com", Util.ROLE_USER);
//        person.setConfirmed(true);
//        personDao.create(person);

        tester = new WebTester();
        tester.setBaseUrl("http://www.google.com");
    }

    @Test
    public void testLogin() {


        tester.beginAt("/");
        tester.assertTitleEquals("Google");

    }

}
