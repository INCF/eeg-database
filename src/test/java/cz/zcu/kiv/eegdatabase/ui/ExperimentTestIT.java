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
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.dao.ScenarioDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.Util;
import net.sourceforge.jwebunit.junit.WebTester;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Created by Honza on 4.12.14.
 */
public class ExperimentTestIT extends AbstractUITest {

    @Autowired
    private PersonDao personDao;

    @Autowired
    private ResearchGroupDao researchGroupDao;

    @BeforeMethod(groups = "web")
    public void setUp() throws InterruptedException {
        if (!personDao.usernameExists("jan.stebetak@seznam.cz")) {
            Person person = TestUtils.createPersonForTesting("jan.stebetak@seznam.cz", Util.ROLE_USER);
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

    }
    @Test(groups = "web")
    public void testExperimentValidation() throws InterruptedException {

        createGroupIfNotExists();
        prepareMetadata();

        tester.clickLinkWithText("Experiments");
        tester.assertLinkPresentWithText("Add experiments");
        tester.clickLinkWithText("Add experiments");

        tester.setTextField("view:scenario", "");
        tester.setTextField("view:personBySubjectPersonId", "");
        tester.clickButtonWithText("Next >");
        Thread.sleep(waitForAjax);
        tester.assertTextPresent("Field 'Group' is required.");
        tester.assertTextPresent("Field 'Scenario' is required.");
        tester.assertTextPresent("Field 'Subject person' is required.");

        tester.selectOption("view:researchGroup", "new group");
        Thread.sleep(waitForAjax);
        tester.assertTextPresent("Research group new group can't create or edit experiment. Research group is locked.");

        //unlockGroup();

        tester.setTextField("view:scenario", "scenarioForExperiment");
        tester.setTextField("view:personBySubjectPersonId", "jan.stebetak@seznam.cz");
        tester.clickButtonWithText("Next >");
        Thread.sleep(waitForAjax);
       //Next page with hw, sw, ...
        tester.clickButtonWithText("Next >");
        Thread.sleep(waitForAjax);

        tester.assertTextPresent("Field 'Hardware' is required.");
        tester.assertTextPresent("Field 'Software' is required.");
        tester.assertTextPresent("Field 'Weather' is required.");

        tester.clickLinkWithText("Log out");

    }

    private void prepareMetadata() throws InterruptedException {
        tester.clickLinkWithText("Scenarios");
        tester.assertLinkPresentWithText("Add scenario");
        tester.clickLinkWithText("Add scenario");

        tester.selectOption("researchGroup", "new group");
        tester.setTextField("title", "scenarioForExperiment");
        tester.setTextField("description", "description");
        tester.setTextField("scenarioLength", "10");

        tester.clickButtonWithText("Save");
        Thread.sleep(waitForAjax);
    }

    private void unlockGroup() {
        List<ResearchGroup> groups = researchGroupDao.getAllRecords();
        for (ResearchGroup group: groups) {
            if (group.getTitle().equals("new group")) {
                group.setLock(false);
                researchGroupDao.update(group);
                break;
            }

        }

    }

}
