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
* RegistrationTest.java, 2014/09/16 00:01 Jan Stebetak
******************************************************************************/
package cz.zcu.kiv.eegdatabase.ui;

import com.thoughtworks.selenium.DefaultSelenium;
import cz.zcu.kiv.eegdatabase.wui.ui.security.RegistrationPage;
import junit.framework.Assert;
import org.apache.wicket.util.tester.WicketTester;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.testng.annotations.BeforeMethod;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
* Created by stebjan on 16.9.2014.
*/
public class RegistrationTest extends AbstractUITest {


    private WebDriver driver;


    @BeforeMethod(groups = "web")
    public void setUp() {

        driver = new RemoteWebDriver(DesiredCapabilities.htmlUnit());
        //driver.get("http://eeg2.kiv.zcu.cz:8080/home-page");
        driver.get("http://127.0.0.1:4444/wd/hub/home-page");
//        selenium = new DefaultSelenium("localhost", 4444, "*firefox","eeg2.kiv.zcu.cz:8080/home-page" );
//        selenium.start();
    }

    @Test(groups = "web")
    public void testEmptyFields() {

       driver.findElement(By.linkText("Register")).click();

       Assert.assertEquals(driver.getTitle(), "Registration");

        driver.findElement(By.xpath("//input[@wicketpath='registration_submit']")).click();
        assertTrue(driver.getPageSource().contains("Field 'Name' is required"));
        assertTrue(driver.getPageSource().contains("Field 'Surname' is required"));
        assertTrue(driver.getPageSource().contains("Field 'E-mail' is required"));
        assertTrue(driver.getPageSource().contains("Field 'Password' is required"));
        assertTrue(driver.getPageSource().contains("Field 'Repeat password' is required"));
        assertTrue(driver.getPageSource().contains("Field 'Gender' is required"));
        assertTrue(driver.getPageSource().contains("Field 'Control text' is required"));
        assertTrue(driver.getPageSource().contains("Field 'Education Level' is required"));

        Assert.assertEquals(driver.getTitle(), "Registration");
        driver.quit();

    }

    @Test(groups = "web")
    public void testEmailFormat() {

        driver.findElement(By.linkText("Register")).click();
        Assert.assertEquals(driver.getTitle(), "Registration");
        driver.findElement(By.name("name")).sendKeys("testName");
        driver.findElement(By.name("surname")).sendKeys("testSurname");
        driver.findElement(By.name("gender")).sendKeys("M");
        driver.findElement(By.name("email")).sendKeys("xxx@seznam");
        driver.findElement(By.name("password")).sendKeys("password");
        driver.findElement(By.name("passwordVerify")).sendKeys("password");

        driver.findElement(By.xpath("//input[@wicketpath='registration_submit']")).click();
        Assert.assertEquals(driver.getTitle(), "Registration");
        assertTrue(driver.getPageSource().contains("is not a valid email address"));

        driver.findElement(By.name("email")).clear();
        driver.findElement(By.name("email")).sendKeys("xxx@seznam.cz");

        driver.findElement(By.xpath("//input[@wicketpath='registration_submit']")).click();
        assertFalse(driver.getPageSource().contains("is not a valid email address"));

        driver.quit();

    }

    @Test(groups = "web")
    public void testPasswordVerification() {

        driver.findElement(By.linkText("Register")).click();
        Assert.assertEquals(driver.getTitle(), "Registration");
        driver.findElement(By.name("name")).sendKeys("testName");
        driver.findElement(By.name("surname")).sendKeys("testSurname");
        driver.findElement(By.name("dateOfBirth")).sendKeys("22/11/1998");
        driver.findElement(By.name("gender")).sendKeys("M");
        driver.findElement(By.name("email")).sendKeys("xxx@xxx.com");
        driver.findElement(By.name("password")).sendKeys("password");
        driver.findElement(By.name("passwordVerify")).sendKeys("passwordxx");

        driver.findElement(By.xpath("//input[@wicketpath='registration_submit']")).click();
        Assert.assertEquals(driver.getTitle(), "Registration");
        //assertTrue(driver.getPageSource().contains("Inserted passwords don't match."));

        driver.findElement(By.name("password")).clear();
        driver.findElement(By.name("password")).sendKeys("password");
        driver.findElement(By.name("passwordVerify")).clear();
        driver.findElement(By.name("passwordVerify")).sendKeys("password");

        assertFalse(driver.getPageSource().contains("Inserted passwords don't match."));
        driver.findElement(By.xpath("//input[@wicketpath='registration_submit']")).click();

        driver.quit();

    }
}
