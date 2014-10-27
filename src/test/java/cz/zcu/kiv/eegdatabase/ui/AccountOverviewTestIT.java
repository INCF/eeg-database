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

import net.sourceforge.jwebunit.junit.WebTester;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.fail;

/**
 * Created by stebjan on 3.10.14.
 */
public class AccountOverviewTestIT extends AbstractUITest {

    private WebTester tester;

    @BeforeMethod
    public void setUp() {

        tester = new WebTester();
       // tester.setBaseUrl("http://eeg2.kiv.zcu.cz:8080");
        tester.setBaseUrl("http://localhost:8080");
        tester.beginAt("/home-page");
        tester.setTextField("userName", "jan.stebetak@seznam.cz");
        tester.setTextField("password", "stebjan");
        tester.clickButtonWithText("Log in");
        tester.assertTextPresent("Log out");

    }

    @Test
    public void testAccountOverview() {

        tester.assertLinkPresentWithExactText("My account");
        tester.clickLinkWithText("My account");

        tester.assertTextPresent("E-mail (Login)");
        tester.assertTextPresent("Change password");


    }

   @Test
    public void testChangePassword() throws InterruptedException {


       tester.assertLinkPresentWithExactText("My account");
       tester.clickLinkWithText("My account");

       tester.assertTextPresent("Change password");

       tester.clickLinkWithText("Change password");
       tester.setTextField("oldPassword", "stebjan");
       tester.setTextField("newPassword", "stebjan2");
       tester.setTextField("verPassword", "stebjan2");
       tester.clickButtonWithText("Change password");
       Thread.sleep(2000);

       tester.assertTextPresent("Changes were made");


       // test if the password was changed
       tester.clickLinkWithText("Log out");
       tester.setTextField("userName", "jan.stebetak@seznam.cz");
       tester.setTextField("password", "stebjan2");
       tester.clickButtonWithText("Log in");

       tester.assertLinkPresentWithExactText("My account");

       tester.clickLinkWithText("My account");

       // return changes
       tester.clickLinkWithText("Change password");
       tester.setTextField("oldPassword", "stebjan2");
       tester.setTextField("newPassword", "stebjan");
       tester.setTextField("verPassword", "stebjan");
       tester.clickButtonWithText("Change password");
       Thread.sleep(2000);

       tester.assertTextPresent("Changes were made");

   }

    @Test
    public void testInvalidChangePassword() throws InterruptedException {

        tester.assertLinkPresentWithExactText("My account");
        tester.clickLinkWithText("My account");
        tester.assertTextPresent("Change password");
        tester.clickLinkWithText("Change password");
        tester.setTextField("oldPassword", "stebjanxxx");
        tester.setTextField("newPassword", "stebjan2");
        tester.setTextField("verPassword", "stebjan2");
        tester.clickButtonWithText("Change password");
        Thread.sleep(2000);
        tester.assertTextPresent("Inserted password doesn't match current password");


    }

    @Test
    public void testInvalidPasswordVerification() throws InterruptedException {

        tester.assertLinkPresentWithExactText("My account");
        tester.clickLinkWithText("My account");
        tester.assertTextPresent("Change password");
        tester.clickLinkWithText("Change password");
        tester.setTextField("oldPassword", "stebjan");
        tester.setTextField("newPassword", "stebjan2");
        tester.setTextField("verPassword", "stebjanxxx");
        tester.clickButtonWithText("Change password");
        Thread.sleep(2000);
        //test if the form was not submitted
        tester.assertTextPresent("Inserted passwords don't match");
        //assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Inserted passwords don't match\\.[\\s\\S]*$"));


    }

}
