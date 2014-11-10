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
 * AccountOverviewTest.java, 2014/0/03 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.ui;

import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.Util;
import net.sourceforge.jwebunit.junit.WebTester;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;


/**
 * Created by stebjan on 3.10.14.
 */
public class MainMenuTestIT extends AbstractUITest {
    private WebTester tester;

    @Autowired
    private PersonDao personDao;

    @BeforeMethod(groups = "web")
    public void setUp() {

        tester = new WebTester();
        tester.setScriptingEnabled(false);

     //   tester.setBaseUrl("http://eeg2.kiv.zcu.cz:8080");
        tester.setBaseUrl(url);
        tester.beginAt("/home-page");
    }

    @Test(groups = "web")
    public void testUsersMainMenu() {
        loginUser(Util.ROLE_USER);
        tester.clickLinkWithText("Articles");
        tester.assertTextPresent("All articles");
        tester.clickLinkWithText("Experiments");
        tester.assertTextPresent("All experiments");
        tester.clickLinkWithText("Scenarios");
        tester.assertTextPresent("List of scenarios");
        tester.clickLinkWithText("Groups");
        tester.assertTextPresent("List of groups");
        tester.clickLinkWithText("People");
        tester.assertTextPresent("List of people");
        tester.clickLinkWithText("Lists");
        tester.assertTitleEquals("Lists Page");
//        tester.clickLinkWithText("History");
//        tester.assertTitleEquals("History Page");

        tester.assertTextNotPresent("Administration");

    }

    @Test(groups = "web")
    public void testAdminsMainMenu() {
        loginUser(Util.ROLE_ADMIN);
        tester.clickLinkWithText("Articles");
        tester.assertTextPresent("All articles");
        tester.clickLinkWithText("Experiments");
        tester.assertTextPresent("All experiments");
        tester.clickLinkWithText("Scenarios");
        tester.assertTextPresent("List of scenarios");
        tester.clickLinkWithText("Groups");
        tester.assertTextPresent("List of groups");
        tester.clickLinkWithText("People");
        tester.assertTextPresent("List of people");
        tester.clickLinkWithText("Lists");
        tester.assertTitleEquals("Lists Page");
//        tester.clickLinkWithText("History");
//        tester.assertTitleEquals("History Page");
//        tester.clickLinkWithText("Administration");
//        tester.assertTextPresent("Manage user roles");

    }

    private void loginUser(String role) {
        Person person = TestUtils.createPersonForTesting("jan.stebetak@seznam.cz", role);
        person.setConfirmed(true);
        if (!personDao.usernameExists("jan.stebetak@seznam.cz")) {

            personDao.create(person);
        }
        tester.setTextField("userName", "jan.stebetak@seznam.cz");
        tester.setTextField("password", "stebjan");
        tester.clickButtonWithText("Log in");
        tester.assertTextPresent("Log out");

    }
}
