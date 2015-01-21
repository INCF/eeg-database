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
 * AddHardwareTestIT.java, 2014/12/13 00:01 Jan Stebetak
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

/**
 * Created by Honza on 13.12.14.
 */
public class AddHardwareTestIT extends AbstractUITest {

    @Autowired
    private PersonDao personDao;

    @BeforeMethod(groups = "web")
    public void setUp() {
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
    public void testHardwareValidation() throws InterruptedException {

        createGroupIfNotExists();

        tester.clickLinkWithText("Lists");
        tester.assertLinkPresentWithText("Hardware definitions");
        tester.clickLinkWithText("Hardware definitions");

        tester.assertLinkPresentWithText("Add hardware definition");
        tester.clickLinkWithText("Add hardware definition");
        Thread.sleep(waitForAjax);
        tester.setTextField("title", "");
        tester.setTextField("type", "");
        tester.setTextField("description", "");
        tester.clickButtonWithText("Save");
        Thread.sleep(waitForAjax);
        tester.assertTextPresent("Field 'Title' is required.");
        tester.assertTextPresent("Field 'Type' is required.");
        tester.assertTextPresent("Field 'Description' is required.");

        tester.clickLinkWithText("Log out");

    }

    @Test(groups = "web")
    public void testAddHardware() throws InterruptedException {

        createGroupIfNotExists();

        tester.clickLinkWithText("Lists");
        tester.assertLinkPresentWithText("Hardware definitions");
        tester.clickLinkWithText("Hardware definitions");

        tester.assertLinkPresentWithText("Add hardware definition");
        tester.clickLinkWithText("Add hardware definition");
        Thread.sleep(waitForAjax);
        tester.setTextField("title", "HwTitle");
        tester.setTextField("type", "type");
        tester.setTextField("description", "desc");
        tester.clickButtonWithText("Save");
        Thread.sleep(waitForAjax);

        tester.assertTextPresent("HwTitle");

        tester.clickLinkWithText("Log out");

    }
}
