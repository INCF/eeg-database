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
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by stebjan on 3.10.14.
 */
public class MainMenuTest extends AbstractUITest {
    private WebDriver driver;

    @BeforeMethod(groups = "web")
    public void setUp() {


        driver = new HtmlUnitDriver();
        driver.get("http://eeg2.kiv.zcu.cz:8080/home-page");

        //driver.get("http://localhost:8080/home-page");
    }

    @Test(groups = "web")
    public void testUsersMainMenu() {
        loginUser();
        driver.findElement(By.linkText("Articles")).click();
        assertEquals(driver.getTitle(), "Articles Page");
        driver.findElement(By.linkText("Experiments")).click();
        assertTrue(driver.getPageSource().contains("All experiments"));
        driver.findElement(By.linkText("Scenarios")).click();
        assertEquals(driver.getTitle(), "List of scenarios");
        driver.findElement(By.linkText("Groups")).click();
        assertEquals(driver.getTitle(), "List of groups");
        driver.findElement(By.linkText("People")).click();
        assertEquals(driver.getTitle(), "List of people");
        driver.findElement(By.linkText("Lists")).click();
        assertEquals(driver.getTitle(), "Lists Page");
        driver.findElement(By.linkText("History")).click();
        assertEquals(driver.getTitle(), "History Page");

        assertFalse(driver.getPageSource().contains("Administration"));

        driver.quit();

    }

    @Test(groups = "web")
    public void testAdminsMainMenu() {
        loginAdmin();
        driver.findElement(By.linkText("Articles")).click();
        assertEquals(driver.getTitle(), "Articles Page");
        driver.findElement(By.linkText("Experiments")).click();
        assertTrue(driver.getPageSource().contains("All experiments"));
        driver.findElement(By.linkText("Scenarios")).click();
        assertEquals(driver.getTitle(), "List of scenarios");
        driver.findElement(By.linkText("Groups")).click();
        assertEquals(driver.getTitle(), "List of groups");
        driver.findElement(By.linkText("People")).click();
        assertEquals(driver.getTitle(), "List of people");
        driver.findElement(By.linkText("Lists")).click();
        assertEquals(driver.getTitle(), "Lists Page");
        driver.findElement(By.linkText("History")).click();
        assertEquals(driver.getTitle(), "History Page");
        driver.findElement(By.linkText("Administration")).click();
        assertEquals(driver.getTitle(), "Change user role");

        driver.quit();

    }

    private void loginUser() {
        //TODO find or create not-admin user
        WebElement name = driver.findElement(By.name("userName"));
        name.sendKeys("testAccountForEEG2@seznam.cz");
        driver.findElement(By.name("password")).sendKeys("123456");
        WebElement button = driver.findElement(By.name(":submit"));
        button.click();

    }

    private void loginAdmin() {
        WebElement name = driver.findElement(By.name("userName"));
        name.sendKeys("jan.stebetak@seznam.cz");
        driver.findElement(By.name("password")).sendKeys("stebjan");
        WebElement button = driver.findElement(By.name(":submit"));
        button.click();

    }
}
