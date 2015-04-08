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

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.Util;
import net.sourceforge.jwebunit.htmlunit.HtmlUnitTestingEngineImpl;
import net.sourceforge.jwebunit.junit.WebTester;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.AssertJUnit.fail;

/**
 * Created by stebjan on 3.10.14.
 */
public class AccountOverviewTestIT extends AbstractUITest {

    private WebTester tester;

    @Autowired
    private PersonDao personDao;

    @BeforeMethod
    public void setUp() throws IOException {
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
        tester.clickButtonWithText(getProperty("action.login"));
        tester.assertTextPresent(getProperty("action.logout"));

    }

    @Test
    public void testAccountOverview() throws IOException {

        tester.assertLinkPresentWithExactText(getProperty("general.page.myaccount.link"));
        tester.clickLinkWithText(getProperty("general.page.myaccount.link"));

        tester.assertTextPresent(getProperty("label.emailLogin"));
        tester.assertTextPresent(getProperty("menuItem.myAccount.changePassword"));


    }

    @Test
    public void testChangePassword() throws InterruptedException, IOException {


        tester.assertLinkPresentWithExactText(getProperty("general.page.myaccount.link"));
        tester.clickLinkWithText(getProperty("general.page.myaccount.link"));

        tester.assertTextPresent(getProperty("menuItem.myAccount.changePassword"));

        tester.clickLinkWithText(getProperty("menuItem.myAccount.changePassword"));
        tester.setTextField("oldPassword", "stebjan");
        tester.setTextField("newPassword", "stebjan2");
        tester.setTextField("verPassword", "stebjan2");
        tester.clickButtonWithText(getProperty("page.myAccount.changePasswordButton"));
        Thread.sleep(waitForAjax);

        tester.assertTextPresent(getProperty("pageTitle.changesWereMade"));


        // test if the password was changed
        tester.clickLinkWithText(getProperty("action.logout"));
        tester.setTextField("userName", "jan.stebetak@seznam.cz");
        tester.setTextField("password", "stebjan2");
        tester.clickButtonWithText(getProperty("action.login"));

        tester.assertLinkPresentWithExactText(getProperty("general.page.myaccount.link"));

        tester.clickLinkWithText(getProperty("general.page.myaccount.link"));

        // return changes
        tester.clickLinkWithText(getProperty("menuItem.myAccount.changePassword"));
        tester.setTextField("oldPassword", "stebjan2");
        tester.setTextField("newPassword", "stebjan");
        tester.setTextField("verPassword", "stebjan");
        tester.clickButtonWithText(getProperty("page.myAccount.changePasswordButton"));
        Thread.sleep(waitForAjax);

        tester.assertTextPresent(getProperty("pageTitle.changesWereMade"));

    }

    @Test
    public void testInvalidChangePassword() throws InterruptedException, IOException {

        tester.assertLinkPresentWithExactText(getProperty("general.page.myaccount.link"));
        tester.clickLinkWithText(getProperty("general.page.myaccount.link"));

        tester.assertTextPresent(getProperty("menuItem.myAccount.changePassword"));
        tester.clickLinkWithText(getProperty("menuItem.myAccount.changePassword"));

        tester.setTextField("oldPassword", "stebjanxxx");
        tester.setTextField("newPassword", "stebjan2");
        tester.setTextField("verPassword", "stebjan2");
        tester.clickButtonWithText(getProperty("page.myAccount.changePasswordButton"));
        Thread.sleep(waitForAjax);
        tester.assertTextPresent(getProperty("invalid.oldPassword"));


    }

    @Test
    public void testInvalidPasswordVerification() throws InterruptedException, IOException {

        tester.assertLinkPresentWithExactText(getProperty("general.page.myaccount.link"));
        tester.clickLinkWithText(getProperty("general.page.myaccount.link"));

        tester.assertTextPresent(getProperty("menuItem.myAccount.changePassword"));
        tester.clickLinkWithText(getProperty("menuItem.myAccount.changePassword"));

        tester.setTextField("oldPassword", "stebjan");
        tester.setTextField("newPassword", "stebjan2");
        tester.setTextField("verPassword", "stebjanxxx");
        tester.clickButtonWithText(getProperty("page.myAccount.changePasswordButton"));
        Thread.sleep(waitForAjax);
        //test if the form was not submitted
        tester.assertTextPresent(getProperty("invalid.passwordMatch"));


    }

}
