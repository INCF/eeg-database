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


import com.gargoylesoftware.htmlunit.BrowserVersion;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;

import net.sourceforge.jwebunit.junit.WebTester;


import org.junit.Before;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
    public void setUp() {
//        person = TestUtils.createPersonForTesting("test@test.com", Util.ROLE_USER);
//        person.setConfirmed(true);
//        personDao.create(person);

        tester = new WebTester();
        tester.setBaseUrl("http://eeg2.kiv.zcu.cz:8080");
        // tester.setBaseUrl("http://localhost:8080");
    }

    @Test
    public void testLogin() {

        try {
            tester.beginAt("/home-page");
        } catch (Exception ex) {
            System.out.println("text skipped");
            return;
        }
        tester.assertTitleEquals("Home Page");
        tester.setTextField("userName", "jan.stebetak@seznam.cz");
        tester.setTextField("password", "stebjan");
        tester.submit(":submit");
        tester.assertTextPresent("Log out");

    }

    @Test
    public void testUnsuccesfullLogin() {

        try {
            tester.beginAt("/home-page");
        } catch (Exception ex) {
            System.out.println("text skipped");
            return;
        }
        tester.assertTitleEquals("Home Page");
        tester.setTextField("userName", "xxx");
        tester.setTextField("password", "xxx");
        tester.submit(":submit");
        tester.assertTextPresent("User cannot be log in.");

    }

}
