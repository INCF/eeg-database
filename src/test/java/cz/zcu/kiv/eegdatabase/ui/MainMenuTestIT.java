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

import java.io.IOException;

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
    public void testUsersMainMenu() throws IOException {
        loginUser("jan.stebetak@seznam.cz", Util.ROLE_USER);
        tester.clickLinkWithText(getProperty("menuItem.articles"));
        tester.assertTextPresent(getProperty("pageTitle.allArticles"));
        tester.clickLinkWithText(getProperty("menuItem.experiments"));
        tester.assertTextPresent(getProperty("pageTitle.allExperiments"));
        tester.clickLinkWithText(getProperty("menuItem.scenarios"));
        tester.assertTextPresent(getProperty("pageTitle.listOfScenarios"));
        tester.clickLinkWithText(getProperty("menuItem.groups"));
        tester.assertTextPresent(getProperty("pageTitle.listOfGroups"));
        tester.assertTextNotPresent(getProperty("menuItem.people"));
        tester.clickLinkWithText(getProperty("menuItem.lists"));
        tester.assertTitleEquals(getProperty("title.page.lists"));
//        tester.clickLinkWithText("History");
//        tester.assertTitleEquals("History Page");

        tester.assertTextNotPresent(getProperty("menuItem.administration"));

    }

    @Test(groups = "web")
    public void testAdminsMainMenu() throws IOException {
        loginUser("jan.stebetak2@seznam.cz", Util.ROLE_ADMIN);
        tester.clickLinkWithText(getProperty("menuItem.articles"));
        tester.assertTextPresent(getProperty("pageTitle.allArticles"));
        tester.clickLinkWithText(getProperty("menuItem.experiments"));
        tester.assertTextPresent(getProperty("pageTitle.allExperiments"));
        tester.clickLinkWithText(getProperty("menuItem.scenarios"));
        tester.assertTextPresent(getProperty("pageTitle.listOfScenarios"));
        tester.clickLinkWithText(getProperty("menuItem.groups"));
        tester.assertTextPresent(getProperty("pageTitle.listOfGroups"));
        tester.clickLinkWithText(getProperty("menuItem.people"));
        tester.assertTextPresent(getProperty("pageTitle.listOfPeople"));
        tester.clickLinkWithText(getProperty("menuItem.lists"));
        tester.assertTitleEquals(getProperty("title.page.lists"));
//        tester.clickLinkWithText("History");
//        tester.assertTitleEquals("History Page");
        tester.clickLinkWithText(getProperty("menuItem.administration"));
        tester.assertTextPresent(getProperty("menuItem.manageRoles"));

    }

    private void loginUser(String username, String role) throws IOException {
        Person person = TestUtils.createPersonForTesting(username, role);
        person.setConfirmed(true);
        if (!personDao.usernameExists(username)) {

            personDao.create(person);
        }
        tester.setTextField("userName", username);
        tester.setTextField("password", "stebjan");
        tester.clickButtonWithText(getProperty("action.login"));
        tester.assertTextPresent(getProperty("action.logout"));

    }
}
