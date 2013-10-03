/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   AddScenarionNSExperimenterTest.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.selenium.webdriver;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class AddScenarionNSExperimenterTest {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "http://localhost:8080/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testAddScenarionNSExperimenter() throws Exception {
    driver.get(baseUrl + "/home-page?1");
    // Warning: assertTextPresent may require manual changes
    assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*No user logged[\\s\\S]*$"));
    driver.findElement(By.xpath("//input[@wicketpath='login_userName']")).clear();
    driver.findElement(By.xpath("//input[@wicketpath='login_userName']")).sendKeys("testAccountForEEG2@seznam.cz");
    driver.findElement(By.xpath("//input[@wicketpath='login_password']")).clear();
    driver.findElement(By.xpath("//input[@wicketpath='login_password']")).sendKeys("123456");
    driver.findElement(By.xpath("//input[@wicketpath='login_submit']")).click();
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Logged user: testaccountforeeg2@seznam\\.cz[\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    driver.findElement(By.linkText("Scenarios")).click();
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*List of scenarios [\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    driver.findElement(By.xpath("//a[@wicketpath='leftMenu_menu_3_link']")).click();
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Add scenario[\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    // Nothing filled
    new Select(driver.findElement(By.xpath("//select[@wicketpath='form_researchGroup']"))).selectByVisibleText("Choose One");
    driver.findElement(By.xpath("//input[@wicketpath='form_title']")).clear();
    driver.findElement(By.xpath("//input[@wicketpath='form_title']")).sendKeys("");
    driver.findElement(By.xpath("//input[@wicketpath='form_scenarioLength']")).clear();
    driver.findElement(By.xpath("//input[@wicketpath='form_scenarioLength']")).sendKeys("0");
    driver.findElement(By.xpath("//textarea[@wicketpath='form_description']")).clear();
    driver.findElement(By.xpath("//textarea[@wicketpath='form_description']")).sendKeys("");
    driver.findElement(By.xpath("//input[@wicketpath='form_dataAvailable']")).click();
    driver.findElement(By.xpath("//input[@wicketpath='form_dataAvailable']")).click();
    driver.findElement(By.xpath("//input[@wicketpath='form_privateScenario']")).click();
    driver.findElement(By.xpath("//input[@wicketpath='form_privateScenario']")).click();
    driver.findElement(By.xpath("//input[@wicketpath='form_submit']")).click();
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Field 'Research group' is required\\.[\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Field 'Scenario title' is required\\.[\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Field 'Description' is required\\.[\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    // Filled only research group
    new Select(driver.findElement(By.xpath("//select[@wicketpath='form_researchGroup']"))).selectByVisibleText("Nová skupina_edited");
    driver.findElement(By.xpath("//input[@wicketpath='form_title']")).clear();
    driver.findElement(By.xpath("//input[@wicketpath='form_title']")).sendKeys("");
    driver.findElement(By.xpath("//input[@wicketpath='form_scenarioLength']")).clear();
    driver.findElement(By.xpath("//input[@wicketpath='form_scenarioLength']")).sendKeys("0");
    driver.findElement(By.xpath("//textarea[@wicketpath='form_description']")).clear();
    driver.findElement(By.xpath("//textarea[@wicketpath='form_description']")).sendKeys("");
    driver.findElement(By.xpath("//input[@wicketpath='form_dataAvailable']")).click();
    driver.findElement(By.xpath("//input[@wicketpath='form_dataAvailable']")).click();
    driver.findElement(By.xpath("//input[@wicketpath='form_privateScenario']")).click();
    driver.findElement(By.xpath("//input[@wicketpath='form_privateScenario']")).click();
    driver.findElement(By.xpath("//input[@wicketpath='form_submit']")).click();
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Field 'Scenario title' is required\\.[\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Field 'Description' is required\\.[\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    // Filled research group and title
    driver.findElement(By.xpath("//input[@wicketpath='form_title']")).clear();
    driver.findElement(By.xpath("//input[@wicketpath='form_title']")).sendKeys("Title");
    new Select(driver.findElement(By.xpath("//select[@wicketpath='form_researchGroup']"))).selectByVisibleText("Nová skupina_edited");
    driver.findElement(By.xpath("//input[@wicketpath='form_scenarioLength']")).clear();
    driver.findElement(By.xpath("//input[@wicketpath='form_scenarioLength']")).sendKeys("0");
    driver.findElement(By.xpath("//textarea[@wicketpath='form_description']")).clear();
    driver.findElement(By.xpath("//textarea[@wicketpath='form_description']")).sendKeys("");
    driver.findElement(By.xpath("//input[@wicketpath='form_dataAvailable']")).click();
    driver.findElement(By.xpath("//input[@wicketpath='form_dataAvailable']")).click();
    driver.findElement(By.xpath("//input[@wicketpath='form_privateScenario']")).click();
    driver.findElement(By.xpath("//input[@wicketpath='form_privateScenario']")).click();
    driver.findElement(By.xpath("//input[@wicketpath='form_submit']")).click();
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Field 'Description' is required\\.[\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    // Incorrect length - less than zero
    driver.findElement(By.xpath("//input[@wicketpath='form_title']")).clear();
    driver.findElement(By.xpath("//input[@wicketpath='form_title']")).sendKeys("Title");
    new Select(driver.findElement(By.xpath("//select[@wicketpath='form_researchGroup']"))).selectByVisibleText("Nová skupina_edited");
    driver.findElement(By.xpath("//input[@wicketpath='form_scenarioLength']")).clear();
    driver.findElement(By.xpath("//input[@wicketpath='form_scenarioLength']")).sendKeys("-1");
    driver.findElement(By.xpath("//textarea[@wicketpath='form_description']")).clear();
    driver.findElement(By.xpath("//textarea[@wicketpath='form_description']")).sendKeys("");
    driver.findElement(By.xpath("//input[@wicketpath='form_dataAvailable']")).click();
    driver.findElement(By.xpath("//input[@wicketpath='form_dataAvailable']")).click();
    driver.findElement(By.xpath("//input[@wicketpath='form_privateScenario']")).click();
    driver.findElement(By.xpath("//input[@wicketpath='form_privateScenario']")).click();
    driver.findElement(By.xpath("//input[@wicketpath='form_submit']")).click();
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Field 'Description' is required\\.[\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*'scenarioLength' musí být větší než 0\\.[\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    // Incorrect length - characters
    driver.findElement(By.xpath("//input[@wicketpath='form_title']")).clear();
    driver.findElement(By.xpath("//input[@wicketpath='form_title']")).sendKeys("Title");
    new Select(driver.findElement(By.xpath("//select[@wicketpath='form_researchGroup']"))).selectByVisibleText("Nová skupina_edited");
    driver.findElement(By.xpath("//input[@wicketpath='form_scenarioLength']")).clear();
    driver.findElement(By.xpath("//input[@wicketpath='form_scenarioLength']")).sendKeys("abc");
    driver.findElement(By.xpath("//textarea[@wicketpath='form_description']")).clear();
    driver.findElement(By.xpath("//textarea[@wicketpath='form_description']")).sendKeys("");
    driver.findElement(By.xpath("//input[@wicketpath='form_dataAvailable']")).click();
    driver.findElement(By.xpath("//input[@wicketpath='form_dataAvailable']")).click();
    driver.findElement(By.xpath("//input[@wicketpath='form_privateScenario']")).click();
    driver.findElement(By.xpath("//input[@wicketpath='form_privateScenario']")).click();
    driver.findElement(By.xpath("//input[@wicketpath='form_submit']")).click();
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Field 'Description' is required\\.[\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*'scenarioLength' není validní Integer\\.[\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    // Unfilled length
    driver.findElement(By.xpath("//input[@wicketpath='form_title']")).clear();
    driver.findElement(By.xpath("//input[@wicketpath='form_title']")).sendKeys("Title");
    new Select(driver.findElement(By.xpath("//select[@wicketpath='form_researchGroup']"))).selectByVisibleText("Nová skupina_edited");
    driver.findElement(By.xpath("//input[@wicketpath='form_scenarioLength']")).clear();
    driver.findElement(By.xpath("//input[@wicketpath='form_scenarioLength']")).sendKeys("");
    driver.findElement(By.xpath("//textarea[@wicketpath='form_description']")).clear();
    driver.findElement(By.xpath("//textarea[@wicketpath='form_description']")).sendKeys("");
    driver.findElement(By.xpath("//input[@wicketpath='form_dataAvailable']")).click();
    driver.findElement(By.xpath("//input[@wicketpath='form_dataAvailable']")).click();
    driver.findElement(By.xpath("//input[@wicketpath='form_privateScenario']")).click();
    driver.findElement(By.xpath("//input[@wicketpath='form_privateScenario']")).click();
    driver.findElement(By.xpath("//input[@wicketpath='form_submit']")).click();
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Field 'Description' is required\\.[\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Field 'scenarioLength' is required\\.[\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    // Incorrect length - double
    driver.findElement(By.xpath("//input[@wicketpath='form_title']")).clear();
    driver.findElement(By.xpath("//input[@wicketpath='form_title']")).sendKeys("Title");
    new Select(driver.findElement(By.xpath("//select[@wicketpath='form_researchGroup']"))).selectByVisibleText("Nová skupina_edited");
    driver.findElement(By.xpath("//input[@wicketpath='form_scenarioLength']")).clear();
    driver.findElement(By.xpath("//input[@wicketpath='form_scenarioLength']")).sendKeys("0.1");
    driver.findElement(By.xpath("//textarea[@wicketpath='form_description']")).clear();
    driver.findElement(By.xpath("//textarea[@wicketpath='form_description']")).sendKeys("");
    driver.findElement(By.xpath("//input[@wicketpath='form_dataAvailable']")).click();
    driver.findElement(By.xpath("//input[@wicketpath='form_dataAvailable']")).click();
    driver.findElement(By.xpath("//input[@wicketpath='form_privateScenario']")).click();
    driver.findElement(By.xpath("//input[@wicketpath='form_privateScenario']")).click();
    driver.findElement(By.xpath("//input[@wicketpath='form_submit']")).click();
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Field 'Description' is required\\.[\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*'scenarioLength' není validní Integer\\.[\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    // Data available without file
    driver.findElement(By.xpath("//input[@wicketpath='form_title']")).clear();
    driver.findElement(By.xpath("//input[@wicketpath='form_title']")).sendKeys("Title");
    new Select(driver.findElement(By.xpath("//select[@wicketpath='form_researchGroup']"))).selectByVisibleText("Nová skupina_edited");
    driver.findElement(By.xpath("//input[@wicketpath='form_scenarioLength']")).clear();
    driver.findElement(By.xpath("//input[@wicketpath='form_scenarioLength']")).sendKeys("1");
    driver.findElement(By.xpath("//textarea[@wicketpath='form_description']")).clear();
    driver.findElement(By.xpath("//textarea[@wicketpath='form_description']")).sendKeys("");
    driver.findElement(By.xpath("//input[@wicketpath='form_dataAvailable']")).click();
    driver.findElement(By.xpath("//input[@wicketpath='form_privateScenario']")).click();
    driver.findElement(By.xpath("//input[@wicketpath='form_privateScenario']")).click();
    driver.findElement(By.xpath("//input[@wicketpath='form_submit']")).click();
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Field 'Description' is required\\.[\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Field 'Data file' is required\\.[\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    // Unfilled title
    driver.findElement(By.xpath("//input[@wicketpath='form_title']")).clear();
    driver.findElement(By.xpath("//input[@wicketpath='form_title']")).sendKeys("");
    new Select(driver.findElement(By.xpath("//select[@wicketpath='form_researchGroup']"))).selectByVisibleText("Nová skupina_edited");
    driver.findElement(By.xpath("//input[@wicketpath='form_scenarioLength']")).clear();
    driver.findElement(By.xpath("//input[@wicketpath='form_scenarioLength']")).sendKeys("1");
    driver.findElement(By.xpath("//textarea[@wicketpath='form_description']")).clear();
    driver.findElement(By.xpath("//textarea[@wicketpath='form_description']")).sendKeys("description");
    driver.findElement(By.xpath("//input[@wicketpath='form_dataAvailable']")).click();
    driver.findElement(By.xpath("//input[@wicketpath='form_privateScenario']")).click();
    driver.findElement(By.xpath("//input[@wicketpath='form_submit']")).click();
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Field 'Scenario title' is required\\.[\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    driver.findElement(By.xpath("//a[@wicketpath='logout']")).click();
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*No user logged[\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alert.getText();
    } finally {
      acceptNextAlert = true;
    }
  }
}
