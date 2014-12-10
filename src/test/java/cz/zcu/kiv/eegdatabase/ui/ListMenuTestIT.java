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

/**
 * Created by Jan Stebetak on 10.12.2014.
 */
public class ListMenuTestIT extends AbstractUITest {
    private WebTester tester;

    @Autowired
    private PersonDao personDao;

    @BeforeMethod(groups = "web")
    public void setUp() {
        if (!personDao.usernameExists("jan.stebetak2@seznam.cz")) {
            Person person = TestUtils.createPersonForTesting("jan.stebetak2@seznam.cz", Util.ROLE_ADMIN);
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

    }
    @Test(groups = "web")
    public void testListLeftMenu() {
        tester.clickLinkWithText("Lists");
        tester.assertTextPresent("List of definitions");
        tester.assertLinkPresentWithText("Hardware definition");
        tester.assertLinkPresentWithText("Optional parameters for people");
        tester.assertLinkPresentWithText("Optional parameters for experiments");
        tester.assertLinkPresentWithText("File metadata definitions");
        tester.assertLinkPresentWithText("Weather definitions");
        tester.assertLinkPresentWithText("Artifact definitions");
        //TODO other definitions (sw, disease, pharmaceuticals, ...)
        tester.clickLinkWithText("Log out");
    }
    @Test(groups = "web")
    public void testListDefaultGroupsValidationAdmin() {
        tester.clickLinkWithText("Lists");
        tester.assertTextPresent("List of definitions");
        tester.clickLinkWithText("Hardware definition");
        tester.assertSelectOptionValuePresent("groups", "Default hardware");

        tester.clickLinkWithText("Optional parameters for people");
        tester.assertSelectOptionValuePresent("groups", "Default optional parameters");

        tester.clickLinkWithText("Optional parameters for experiments");
        tester.assertSelectOptionValuePresent("groups", "Default optional parameters");

        tester.clickLinkWithText("File metadata definitions");
        tester.assertSelectOptionValuePresent("groups", "Default metadata parameters");

        tester.clickLinkWithText("Weather definitions");
        tester.assertSelectOptionValuePresent("groups", "Default weather");

        tester.clickLinkWithText("Artifact definitions"); //Artifact do not have the Default group
        tester.assertSelectOptionValueNotPresent("groups", "Default artifact");

        tester.clickLinkWithText("Log out");
    }
}
