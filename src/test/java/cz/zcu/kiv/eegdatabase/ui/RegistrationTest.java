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
import junit.framework.Assert;
import net.sourceforge.jwebunit.junit.WebTester;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.testng.annotations.BeforeMethod;

/**
 * Created by stebjan on 16.9.2014.
 */
public class RegistrationTest extends AbstractUITest {

    private WebTester tester;

    private WebDriver driver;

    private DefaultSelenium selenium;

    @BeforeMethod
    public void setUp() {
//        tester = new WebTester();
//        tester.setBaseUrl("http://eeg2.kiv.zcu.cz:8080");
        // tester.setBaseUrl("http://localhost:8080");
//        driver = new HtmlUnitDriver();
//        driver.get("http://eeg2.kiv.zcu.cz:8080/home-page");
        selenium = new DefaultSelenium("localhost", 4444, "*firefox","eeg2.kiv.zcu.cz:8080/home-page" );
        selenium.start();
    }

    @Test
    public void testRegistration() {

//        driver.findElement(By.linkText("Register")).click();
//        Assert.assertEquals(driver.getTitle(), "Registration");
//        driver.findElement(By.name(":submit")).click();
//        Assert.assertEquals(driver.getTitle(), "Registration");
//        driver.quit();
selenium.stop();
    }
}
