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
 * PersonTestIT.java, 2014/12/19 00:01 Jan Stebetak
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
 * Created by Honza on 19.12.14.
 */
public class PersonTestIT extends AbstractUITest{

    @Autowired
    private PersonDao personDao;

    @BeforeMethod(groups = "web")
    public void setUp() {
        if (!personDao.usernameExists("jan.stebetak2@seznam.cz")) {
            Person person = TestUtils.createPersonForTesting("jan.stebetak2@seznam.cz", Util.ROLE_USER);
            person.setConfirmed(true);
            personDao.create(person);
        }

        tester = new WebTester();
        // tester.setBaseUrl("http://eeg2.kiv.zcu.cz:8080");
        tester.setBaseUrl(url);
        tester.beginAt("/home-page");
        tester.setTextField("userName", "jan.stebetak2@seznam.cz");
        tester.setTextField("password", "stebjan");
        tester.clickButtonWithText("Log in");
        tester.assertTextPresent("Log out");

    }
    @Test(groups = "web")
    public void testAddPersonValidationEmptyFields() throws InterruptedException {


        tester.clickLinkWithText("People");
        tester.assertLinkPresentWithText("Add person");
        tester.clickLinkWithText("Add person");
        tester.setTextField("givenname", "");
        tester.setTextField("surname", "");
        tester.setTextField("dateOfBirth", "");
        tester.setTextField("username", "");
        tester.clickButtonWithText("Save");
        Thread.sleep(waitForAjax);

        tester.assertTextPresent("Field 'Name' is required.");
        tester.assertTextPresent("Field 'Surname' is required.");
        tester.assertTextPresent("Field 'Date of birth' is required.");
        tester.assertTextPresent("Field 'Gender' is required.");
        tester.assertTextPresent("Field 'E-mail' is required.");
     //   tester.assertTextPresent("Field 'Education Level' is required");  TODO need to be fixed in form

        tester.clickLinkWithText("Log out");

    }
    @Test(groups = "web")
    public void testAddPersonEmailValidation() throws InterruptedException {


        tester.clickLinkWithText("People");
        tester.assertLinkPresentWithText("Add person");
        tester.clickLinkWithText("Add person");
        tester.setTextField("givenname", "Test");
        tester.setTextField("surname", "Test");
        tester.setTextField("dateOfBirth", "10/10/2010");
        tester.setTextField("username", "xxx");
        tester.clickButtonWithText("Save");
        Thread.sleep(waitForAjax);

        tester.assertTextPresent("'E-mail' is not a valid email address.");

        tester.setTextField("username", "xxx@xxx");
        tester.clickButtonWithText("Save");
        Thread.sleep(waitForAjax);

        tester.assertTextPresent("'E-mail' is not a valid email address.");

        tester.setTextField("username", "xxx@xxx.com");
        tester.clickButtonWithText("Save");
        Thread.sleep(waitForAjax);

        tester.assertTextNotPresent("'E-mail' is not a valid email address.");


        tester.clickLinkWithText("Log out");

    }

    @Test(groups = "web")
    public void testAddPersonUniqueUsernameValidation() throws InterruptedException {


        tester.clickLinkWithText("People");
        tester.assertLinkPresentWithText("Add person");
        tester.clickLinkWithText("Add person");
        tester.setTextField("givenname", "Test");
        tester.setTextField("surname", "Test");
        tester.setTextField("dateOfBirth", "10/10/2010");
        tester.clickRadioOption("gender", "0");//"Male"
        tester.setTextField("username", "jan.stebetak2@seznam.cz");
        tester.clickButtonWithText("Save");
        Thread.sleep(waitForAjax);

        tester.assertTextPresent("Email is already used.");


        tester.clickLinkWithText("Log out");

    }
}
