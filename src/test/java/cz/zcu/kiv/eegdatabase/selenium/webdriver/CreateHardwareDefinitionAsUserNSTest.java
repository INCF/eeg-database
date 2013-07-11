package cz.zcu.kiv.eegdatabase.selenium.webdriver;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class CreateHardwareDefinitionAsUserNSTest {
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
  public void testCreateHardwareDefinitionAsUserNS() throws Exception {
    driver.get(baseUrl + "/home-page?1");
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*No user logged[\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
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
    driver.findElement(By.linkText("Lists")).click();
    driver.findElement(By.xpath("//a[@wicketpath='leftMenu_menu_0_link']")).click();
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*List of hardware definitions[\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    driver.findElement(By.xpath("//a[@wicketpath='addHardwareLink']")).click();
    driver.findElement(By.xpath("//input[@wicketpath='form_submit']")).click();
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Field 'Description' is required\\.[\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Field 'Title' is required\\.[\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Field 'Type' is required\\.[\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    driver.findElement(By.xpath("//input[@wicketpath='form_title']")).clear();
    driver.findElement(By.xpath("//input[@wicketpath='form_title']")).sendKeys("NewTitle");
    driver.findElement(By.xpath("//input[@wicketpath='form_type']")).clear();
    driver.findElement(By.xpath("//input[@wicketpath='form_type']")).sendKeys("");
    driver.findElement(By.xpath("//input[@wicketpath='form_description']")).clear();
    driver.findElement(By.xpath("//input[@wicketpath='form_description']")).sendKeys("");
    driver.findElement(By.xpath("//input[@wicketpath='form_submit']")).click();
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Field 'Description' is required\\.[\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Field 'Type' is required\\.[\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    driver.findElement(By.xpath("//input[@wicketpath='form_title']")).clear();
    driver.findElement(By.xpath("//input[@wicketpath='form_title']")).sendKeys("");
    driver.findElement(By.xpath("//input[@wicketpath='form_type']")).clear();
    driver.findElement(By.xpath("//input[@wicketpath='form_type']")).sendKeys("NewType");
    driver.findElement(By.xpath("//input[@wicketpath='form_description']")).clear();
    driver.findElement(By.xpath("//input[@wicketpath='form_description']")).sendKeys("");
    driver.findElement(By.xpath("//input[@wicketpath='form_submit']")).click();
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Field 'Description' is required\\.[\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Field 'Title' is required\\.[\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    driver.findElement(By.xpath("//input[@wicketpath='form_title']")).clear();
    driver.findElement(By.xpath("//input[@wicketpath='form_title']")).sendKeys("");
    driver.findElement(By.xpath("//input[@wicketpath='form_type']")).clear();
    driver.findElement(By.xpath("//input[@wicketpath='form_type']")).sendKeys("");
    driver.findElement(By.xpath("//input[@wicketpath='form_description']")).clear();
    driver.findElement(By.xpath("//input[@wicketpath='form_description']")).sendKeys("NewDescription");
    driver.findElement(By.xpath("//input[@wicketpath='form_submit']")).click();
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Field 'Title' is required\\.[\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Field 'Type' is required\\.[\\s\\S]*$"));
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
