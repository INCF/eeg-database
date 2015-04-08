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
 * AddSoftwareTestIT.java, 2015/01/04 00:01 Jan Stebetak
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

/**
 * Created by Honza on 4.1.15.
 */
public class AddSoftwareTestIT extends AbstractUITest {
    @Autowired
    private PersonDao personDao;

    @BeforeMethod(groups = "web")
    public void setUp() throws IOException {
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
        tester.clickButtonWithText(getProperty("action.login"));
        tester.assertTextPresent(getProperty("action.logout"));

    }

    @Test(groups = "web")
    public void testSoftwareValidation() throws InterruptedException, IOException {

        createGroupIfNotExists();

        tester.clickLinkWithText(getProperty("menuItem.lists"));
        tester.assertLinkPresentWithText(getProperty("menuItem.softwareDefinitions"));
        tester.clickLinkWithText(getProperty("menuItem.softwareDefinitions"));

        tester.assertLinkPresentWithText(getProperty("link.addSoftwareDefinition"));
        tester.clickLinkWithText(getProperty("link.addSoftwareDefinition"));
        Thread.sleep(waitForAjax);
        tester.setTextField("title", "");
        // tester.setTextField("type", "");
        tester.setTextField("description", "");
        tester.clickButtonWithText(getProperty("button.save"));
        Thread.sleep(waitForAjax);
        tester.assertTextPresent("Field 'Title' is required.");
        // tester.assertTextPresent("Field 'Type' is required.");
        tester.assertTextPresent("Field 'Description' is required.");

        tester.clickLinkWithText(getProperty("action.logout"));

    }

    @Test(groups = "web")
    public void testAddSoftware() throws InterruptedException, IOException {

        createGroupIfNotExists();

        tester.clickLinkWithText(getProperty("menuItem.lists"));
        tester.assertLinkPresentWithText(getProperty("menuItem.softwareDefinitions"));
        tester.clickLinkWithText(getProperty("menuItem.softwareDefinitions"));

        tester.assertLinkPresentWithText(getProperty("link.addSoftwareDefinition"));
        tester.clickLinkWithText(getProperty("link.addSoftwareDefinition"));
        Thread.sleep(waitForAjax);
        tester.setTextField("title", "SwTitle");
        tester.setTextField("description", "desc");
        tester.clickButtonWithText(getProperty("button.save"));
        Thread.sleep(waitForAjax);

        tester.assertTextPresent("SwTitle");

        tester.clickLinkWithText(getProperty("action.logout"));

    }
}
