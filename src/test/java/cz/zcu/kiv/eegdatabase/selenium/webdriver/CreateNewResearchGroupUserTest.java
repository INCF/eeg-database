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

public class CreateNewResearchGroupUserTest {
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
  public void testCreateNewResearchGroupUser() throws Exception {
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
    driver.findElement(By.linkText("Groups")).click();
    driver.findElement(By.xpath("//a[@wicketpath='leftMenu_menu_4_link']")).click();
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Create group[\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    driver.findElement(By.xpath("//input[@wicketpath='form_title']")).clear();
    driver.findElement(By.xpath("//input[@wicketpath='form_title']")).sendKeys("Unique Grouú");
    driver.findElement(By.xpath("//input[@wicketpath='form_title']")).clear();
    driver.findElement(By.xpath("//input[@wicketpath='form_title']")).sendKeys("Unique Group 123");
    driver.findElement(By.xpath("//textarea[@wicketpath='form_description']")).clear();
    driver.findElement(By.xpath("//textarea[@wicketpath='form_description']")).sendKeys("description");
    driver.findElement(By.xpath("//input[@wicketpath='form_submit']")).click();
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
