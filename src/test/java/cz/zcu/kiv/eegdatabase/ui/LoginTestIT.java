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
import org.testng.annotations.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;

/**
 * Created by stebjan on 10.9.2014.
 */
public class LoginTestIT extends AbstractUITest {

    @Autowired
    private PersonDao personDao;


    private WebTester tester;

    @BeforeMethod(groups = "web")
    public void setUp() {
        if (!personDao.usernameExists("jan.stebetak@seznam.cz")) {
            Person person = TestUtils.createPersonForTesting("jan.stebetak@seznam.cz", Util.ROLE_USER);
            person.setConfirmed(true);
            personDao.create(person);
        }
        tester = new WebTester();
        tester.setScriptingEnabled(false);

        //   tester.setBaseUrl("http://eeg2.kiv.zcu.cz:8080");
        tester.setBaseUrl(url);
        tester.beginAt("/home-page");
    }

    @Test(groups = "web")
    public void testLogin() throws IOException {

        tester.assertTitleEquals("Home Page");
        tester.setTextField("userName", "jan.stebetak@seznam.cz");
        tester.setTextField("password", "stebjan");
        tester.clickButtonWithText(getProperty("action.login"));
        tester.assertTextPresent(getProperty("action.logout"));

    }

    @Test(groups = "web")
    public void testUnsuccessfulLogin() throws IOException {

        tester.assertTitleEquals("Home Page");
        tester.setTextField("userName", "jan.stebetak@seznam.cz");
        tester.setTextField("password", "xxx");
        tester.clickButtonWithText(getProperty("action.login"));
        tester.assertTextPresent("User cannot be log in");

    }

    @Test(groups = "web")
    public void testEmptyFieldsLogin() throws IOException {

        tester.assertTitleEquals("Home Page");
        tester.setTextField("userName", "");
        tester.setTextField("password", "");
        tester.clickButtonWithText(getProperty("action.login"));
        tester.assertTextPresent("Field 'userName' is required");
        tester.assertTextPresent("Field 'password' is required");
    }

}
