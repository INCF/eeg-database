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
 * EditScenarioTestIT.java, 2015/01/02 00:01 Jan Stebetak
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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

/**
 * Created by Honza on 2.1.15.
 */
public class EditScenarioTestIT extends AbstractUITest {

    @Autowired
    private PersonDao personDao;

    @Autowired
    private ScenarioDao scenarioDao;

    @BeforeMethod(groups = "web")

    public void setUp() throws InterruptedException {
        Person person = TestUtils.createPersonForTesting("jan.stebetak@seznam.cz", Util.ROLE_USER);
        if (!personDao.usernameExists("jan.stebetak@seznam.cz")) {

            person.setConfirmed(true);
            personDao.create(person);
        }

        tester = new WebTester();
        tester.setBaseUrl(url);
        tester.beginAt("/home-page");
        tester.setTextField("userName", "jan.stebetak@seznam.cz");
        tester.setTextField("password", "stebjan");
        tester.clickButtonWithText("Log in");
        tester.assertTextPresent("Log out");

        if (scenarioDao.getScenariosWhereOwner(person).size() == 0) {
            createScenario();
        }

    }
    @Test(groups = "web")
    public void testEditScenario() throws InterruptedException {
        int count = scenarioDao.getCountRecords();
        tester.clickLinkWithText("Scenarios");
        tester.assertLinkPresentWithText("Detail");
        tester.clickLinkWithText("Detail");
        tester.assertLinkPresentWithText("Edit");
        tester.clickLinkWithText("Edit");

        //tester.assertTextNotInElement("title", "");
        try {
            tester.assertTextFieldEquals("title", "");
            fail("The field 'title' should not be empty!");
        } catch (AssertionError e) {
            //Its ok (there is no method for NotEquals)
        }
        tester.setTextField("title", "newTitle");

        tester.clickButtonWithText("Save");
        Thread.sleep(waitForAjax);

        tester.assertTextPresent("newTitle");
        assertEquals(count, scenarioDao.getCountRecords());
        tester.clickLinkWithText("Log out");



    }

    private void createScenario() throws InterruptedException {
        createGroupIfNotExists();
        tester.clickLinkWithText("Scenarios");
        tester.assertLinkPresentWithText("Add scenario");
        tester.clickLinkWithText("Add scenario");

        tester.selectOption("researchGroup", "new group");
        tester.setTextField("title", "scenarioForEditing");
        tester.setTextField("description", "description");
        tester.setTextField("scenarioLength", "10");

        tester.clickButtonWithText("Save");
        Thread.sleep(waitForAjax);

    }
}
