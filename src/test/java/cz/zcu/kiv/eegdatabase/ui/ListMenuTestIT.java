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
 * ListMenuTestIT.java, 2014/12/10 00:01 Jan Stebetak
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
 * Created by Jan Stebetak on 10.12.2014.
 */
public class ListMenuTestIT extends AbstractUITest {
    private WebTester tester;

    @Autowired
    private PersonDao personDao;

    @BeforeMethod(groups = "web")
    public void setUp() throws IOException {
        if (!personDao.usernameExists("jan.stebetak2@seznam.cz")) {
            Person person = TestUtils.createPersonForTesting("jan.stebetak2@seznam.cz", Util.ROLE_ADMIN);
            person.setConfirmed(true);
            personDao.create(person);
        }

        tester = new WebTester();
        // tester.setBaseUrl("http://eeg2.kiv.zcu.cz:8080");
        tester.setBaseUrl(url);
        tester.beginAt("/home-page");
        tester.setTextField("userName", "jan.stebetak2@seznam.cz");
        tester.setTextField("password", "stebjan");
        tester.clickButtonWithText(getProperty("action.login"));
        tester.assertTextPresent(getProperty("action.logout"));

    }
    @Test(groups = "web")
    public void testListLeftMenu() throws IOException {
        tester.clickLinkWithText(getProperty("menuItem.lists"));
        tester.assertTextPresent(getProperty("pageTitle.listOfDefinitions"));
        tester.assertLinkPresentWithText(getProperty("menuItem.hardwareDefinitions"));
        tester.assertLinkPresentWithText(getProperty("menuItem.optionalParametersForPeople"));
        tester.assertLinkPresentWithText(getProperty("menuItem.optionalParametersForExperiments"));
        tester.assertLinkPresentWithText(getProperty("menuItem.fileMetadataDefinitions"));
        tester.assertLinkPresentWithText(getProperty("menuItem.weatherDefinitions"));
        tester.assertLinkPresentWithText(getProperty("menuItem.artifactDefinitions"));
        //TODO other definitions (sw, disease, pharmaceuticals, ...)
        tester.clickLinkWithText(getProperty("action.logout"));
    }
    @Test(groups = "web")
    public void testListDefaultGroupsValidationAdmin() throws IOException {
        tester.clickLinkWithText(getProperty("menuItem.lists"));
        tester.assertTextPresent(getProperty("pageTitle.listOfDefinitions"));
        tester.assertLinkPresentWithText(getProperty("menuItem.hardwareDefinitions"));
        tester.assertSelectOptionPresent("groups", "Default hardware");

        tester.clickLinkWithText(getProperty("menuItem.optionalParametersForPeople"));
        tester.assertSelectOptionPresent("groups", "Default optional parameters");

        tester.clickLinkWithText(getProperty("menuItem.optionalParametersForExperiments"));
        tester.assertSelectOptionPresent("groups", "Default optional parameters");

        tester.clickLinkWithText(getProperty("menuItem.fileMetadataDefinitions"));
        tester.assertSelectOptionPresent("groups", "Default metadata parameters");

        tester.clickLinkWithText(getProperty("menuItem.weatherDefinitions"));
        tester.assertSelectOptionPresent("groups", "Default weather");

        tester.clickLinkWithText(getProperty("menuItem.artifactDefinitions")); //Artifact do not have the Default group
        tester.assertSelectOptionNotPresent("groups", "Default artifact");

        tester.clickLinkWithText(getProperty("action.logout"));
    }
}
