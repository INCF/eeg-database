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
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

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
        //driver = new FirefoxDriver();
       // driver.get("http://eeg2.kiv.zcu.cz:8080/home-page");
        driver.get("http://localhost:8080/home-page");
        WebElement name = driver.findElement(By.name("userName"));
        name.sendKeys("jan.stebetak@seznam.cz");
        driver.findElement(By.name("password")).sendKeys("stebjan");
        WebElement button = driver.findElement(By.name(":submit"));
        button.click();
//
    }

    @Test(groups = "web")
    public void testAccountOverview() {

        driver.findElement(By.linkText("My account")).click();
        assertEquals(driver.getTitle(), "Account overview");
        assertTrue(driver.getPageSource().contains("E-mail (Login)"));
        assertTrue(driver.getPageSource().contains("Change password"));
        driver.quit();

    }

    @Test(groups = "web")
    public void testChangePassword() throws InterruptedException {

        driver.findElement(By.linkText("My account")).click();
        assertEquals(driver.getTitle(), "Account overview");
        assertTrue(driver.getPageSource().contains("Change password"));
        driver.findElement(By.linkText("Change password")).click();
        driver.findElement(By.name("oldPassword")).sendKeys("stebjan");
        driver.findElement(By.name("newPassword")).sendKeys("stebjan2");
        driver.findElement(By.name("verPassword")).sendKeys("stebjan2");
        WebElement button = driver.findElement(By.name(":submit"));
        button.click();

        assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Change password[\\s\\S]*$"));

         //return changes
        driver.findElement(By.linkText("Change password")).click();
        driver.findElement(By.name("oldPassword")).clear();
        driver.findElement(By.name("newPassword")).clear();
        driver.findElement(By.name("verPassword")).clear();
        driver.findElement(By.name("oldPassword")).sendKeys("stebjan2");
        driver.findElement(By.name("newPassword")).sendKeys("stebjan");
        driver.findElement(By.name("verPassword")).sendKeys("stebjan");
        button = driver.findElement(By.name(":submit"));
        button.click();

        Thread.sleep(1000);

        driver.quit();

    }

    @Test(groups = "web")
    public void testInvalidChangePassword() {

        driver.findElement(By.linkText("My account")).click();
        assertEquals(driver.getTitle(), "Account overview");
        assertTrue(driver.getPageSource().contains("Change password"));
        driver.findElement(By.linkText("Change password")).click();
        driver.findElement(By.name("oldPassword")).sendKeys("stebjanxxx");
        driver.findElement(By.name("newPassword")).sendKeys("stebjan2");
        driver.findElement(By.name("verPassword")).sendKeys("stebjan2");
        WebElement button = driver.findElement(By.name(":submit"));
        button.click();
        //assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Inserted password doesn't match current password\\.[\\s\\S]*$"));
        driver.quit();

    }

    @Test(groups = "web")
    public void testInvalidPasswordVerification() {

        driver.findElement(By.linkText("My account")).click();
        assertEquals(driver.getTitle(), "Account overview");
        assertTrue(driver.getPageSource().contains("Change password"));
        driver.findElement(By.linkText("Change password")).click();
        driver.findElement(By.name("oldPassword")).sendKeys("stebjan");
        driver.findElement(By.name("newPassword")).sendKeys("stebjan2");
        driver.findElement(By.name("verPassword")).sendKeys("stebjanxx");

        WebElement button = driver.findElement(By.name(":submit"));
        button.click();
        //assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Inserted passwords don't match\\.[\\s\\S]*$"));
        driver.quit();

    }
}
