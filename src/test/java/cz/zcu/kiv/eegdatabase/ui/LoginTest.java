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
 * LoginTest.java, 2014/09/10 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.ui;


import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;

import static junit.framework.Assert.*;



import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeMethod;

/**
 * Created by stebjan on 10.9.2014.
 */
public class LoginTest extends AbstractUITest {

    private Person person;

    @Autowired
    private PersonDao personDao;


    private WebDriver driver;

    @BeforeMethod(groups = "web")
    public void setUp() {
//        person = TestUtils.createPersonForTesting("test@test.com", Util.ROLE_USER);
//        person.setConfirmed(true);
//        personDao.create(person);

        driver = new HtmlUnitDriver();
        driver.get("http://eeg2.kiv.zcu.cz:8080/home-page");
        //driver.get("http://localhost:8080/home-page");
    }

    @Test(groups = "web")
    public void testLogin() {

        assertEquals(driver.getTitle(), "Home Page");
        WebElement name = driver.findElement(By.name("userName"));
        name.sendKeys("jan.stebetak@seznam.cz");
        driver.findElement(By.name("password")).sendKeys("stebjan");
        WebElement button = driver.findElement(By.name(":submit"));
        button.click();
        assertTrue(driver.getPageSource().contains("Log out"));
        driver.quit();

    }

    @Test(groups = "web")
    public void testUnsuccesfullLogin() {
        assertEquals(driver.getTitle(), "Home Page");
        WebElement name = driver.findElement(By.name("userName"));
        name.sendKeys("xxx");
        driver.findElement(By.name("password")).sendKeys("xxx");
        WebElement button = driver.findElement(By.name(":submit"));
        button.click();
        assertTrue(driver.getPageSource().contains("User cannot be log in"));
        driver.quit();

    }

    @Test(groups = "web")
    public void testEmptyFieldsLogin() {
        assertEquals(driver.getTitle(), "Home Page");
        WebElement name = driver.findElement(By.name("userName"));
        name.sendKeys("");
        driver.findElement(By.name("password")).sendKeys("");
        WebElement button = driver.findElement(By.name(":submit"));
        button.click();
        assertTrue(driver.getPageSource().contains("Field 'userName' is required"));
        assertTrue(driver.getPageSource().contains("Field 'password' is required"));
        driver.quit();
    }

}
