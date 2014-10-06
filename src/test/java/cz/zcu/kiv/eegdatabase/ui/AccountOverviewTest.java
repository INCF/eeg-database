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

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by stebjan on 3.10.14.
 */
public class AccountOverviewTest extends AbstractUITest {

    private WebDriver driver;

    @BeforeMethod(groups = "web")
    public void setUp() {


        driver = new HtmlUnitDriver();
        driver.get("http://eeg2.kiv.zcu.cz:8080/home-page");
        WebElement name = driver.findElement(By.name("userName"));
        name.sendKeys("jan.stebetak@seznam.cz");
        driver.findElement(By.name("password")).sendKeys("stebjan");
        WebElement button = driver.findElement(By.name(":submit"));
        button.click();
//        driver.get("http://localhost:8080/home-page");
    }

    @Test(groups = "web")
    public void testAccountOverview() {

        driver.findElement(By.linkText("Account overview")).click();
        assertEquals(driver.getTitle(), "Account overview");
        assertTrue(driver.getPageSource().contains("E-mail (Login)"));
        assertTrue(driver.getPageSource().contains("Change password"));
        driver.quit();

    }

    @Test(groups = "web")
    public void testChangePassword() {

        driver.findElement(By.linkText("Account overview")).click();
        assertEquals(driver.getTitle(), "Account overview");
        assertTrue(driver.getPageSource().contains("Change password"));
        driver.findElement(By.linkText("Change password")).click();
        driver.findElement(By.name("oldPassword")).sendKeys("stebjan");
        driver.findElement(By.name("newPassword")).sendKeys("stebjan2");
        driver.findElement(By.name("verPassword")).sendKeys("stebjan2");
        driver.findElement(By.name("submit")).click();

        driver.quit();

    }

    @Test(groups = "web")
    public void testInvalidChangePassword() {

        driver.findElement(By.linkText("Account overview")).click();
        assertEquals(driver.getTitle(), "Account overview");
        assertTrue(driver.getPageSource().contains("Change password"));
        driver.findElement(By.linkText("Change password")).click();
        driver.findElement(By.name("oldPassword")).sendKeys("stebjanxxx");
        driver.findElement(By.name("newPassword")).sendKeys("stebjan2");
        driver.findElement(By.name("verPassword")).sendKeys("stebjan2");
        driver.findElement(By.name("submit")).click();
        //TODO invalid password message
        driver.quit();

    }

    @Test(groups = "web")
    public void testInvalidPasswordVerification() {

        driver.findElement(By.linkText("Account overview")).click();
        assertEquals(driver.getTitle(), "Account overview");
        assertTrue(driver.getPageSource().contains("Change password"));
        driver.findElement(By.linkText("Change password")).click();
        driver.findElement(By.name("oldPassword")).sendKeys("stebjan");
        driver.findElement(By.name("newPassword")).sendKeys("stebjan2");
        driver.findElement(By.name("verPassword")).sendKeys("stebjanxx");
        //TODO invalid password message
        driver.findElement(By.name("submit")).click();

        driver.quit();

    }
}
