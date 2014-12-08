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
 * ExperimentTestIT.java, 2014/12/04 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.ui;

import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ScenarioDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.Util;
import net.sourceforge.jwebunit.junit.WebTester;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Created by Honza on 4.12.14.
 */
public class ExperimentTestIT extends AbstractUITest {

    private WebTester tester;

    @Autowired
    private PersonDao personDao;

    @Autowired
    private ScenarioDao scenarioDao;
    private String scenarioTitle;

    @BeforeMethod(groups = "web")
    public void setUp() throws InterruptedException {
        if (!personDao.usernameExists("jan.stebetak@seznam.cz")) {
            Person person = TestUtils.createPersonForTesting("jan.stebetak@seznam.cz", Util.ROLE_USER);
            person.setConfirmed(true);
            personDao.create(person);
        }


        tester = new WebTester();
        // tester.setBaseUrl("http://eeg2.kiv.zcu.cz:8080");
        tester.setBaseUrl(url);
        tester.beginAt("/home-page");
        tester.setTextField("userName", "jan.stebetak@seznam.cz");
        tester.setTextField("password", "stebjan");
        tester.clickButtonWithText("Log in");
        tester.assertTextPresent("Log out");

//        if (scenarioDao.getAllRecords().size() == 0) {
//            createGroupIfNotExists();
//            tester.clickLinkWithText("Scenarios");
//            tester.assertLinkPresentWithText("Add scenario");
//            tester.clickLinkWithText("Add scenario");
//
//            tester.selectOption("researchGroup", "new group");
//            tester.setTextField("title", "new scenario");
//            tester.setTextField("description", "description");
//            tester.setTextField("scenarioLength", "10");
//            tester.clickButtonWithText("Save");
//            Thread.sleep(waitForAjax);
//            scenarioTitle = "new scenario";
//        } else {
//            scenarioTitle = scenarioDao.getAllRecords().get(0).getTitle();
//        }
    }
    @Test(groups = "web")
    public void testExperimentValidation() throws InterruptedException {

        createGroupIfNotExists();

        tester.clickLinkWithText("Experiments");
        tester.assertLinkPresentWithText("Add experiments");
        tester.clickLinkWithText("Add experiments");

        tester.setTextField("view:scenario", "");
        tester.setTextField("view:personBySubjectPersonId", "");
        tester.clickButtonWithText("Next");
        Thread.sleep(waitForAjax);
        tester.assertTextPresent("Field 'Group' is required.");
        tester.assertTextPresent("Field 'Scenario' is required.");
        tester.assertTextPresent("Field 'Subject person' is required.");

        tester.clickLinkWithText("Log out");

    }


    private void createGroupIfNotExists() throws InterruptedException {
        tester.clickLinkWithText("Groups");
        try {
            tester.assertTextPresent("new group");
        } catch (AssertionError ex) {
            tester.clickLinkWithText("Create group");
            tester.setTextField("title", "new group");
            tester.setTextField("description", "description");
            tester.clickButtonWithText("Save");
            Thread.sleep(waitForAjax);
        }

    }
}
