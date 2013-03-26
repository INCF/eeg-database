package cz.zcu.kiv.eegdatabase.selenium.webdriver;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class CreateNewUserByUser {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "http://147.228.64.172:8080/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testCreateNewUserByUser() throws Exception {
    driver.get(baseUrl + "/home-page?0");
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*No user logged[\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    driver.findElement(By.xpath("//input[@wicketpath='login_userName']")).clear();
    driver.findElement(By.xpath("//input[@wicketpath='login_userName']")).sendKeys("pokryvka@seznam.cz");
    driver.findElement(By.xpath("//input[@wicketpath='login_password']")).clear();
    driver.findElement(By.xpath("//input[@wicketpath='login_password']")).sendKeys("KDrJm4nTbRpPjmZc");
    driver.findElement(By.xpath("//input[@wicketpath='login_submit']")).click();
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Logged user: testaccountforeeg2@seznam\\.cz[\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    driver.findElement(By.linkText("People")).click();
    driver.findElement(By.xpath("//a[@wicketpath='leftMenu_menu_2_link']")).click();
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Add person[\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    driver.findElement(By.xpath("//input[@wicketpath='form_givenname']")).clear();
    driver.findElement(By.xpath("//input[@wicketpath='form_givenname']")).sendKeys("NewName");
    driver.findElement(By.xpath("//input[@wicketpath='form_surname']")).clear();
    driver.findElement(By.xpath("//input[@wicketpath='form_surname']")).sendKeys("NewSurname");
    driver.findElement(By.cssSelector("span.ui-icon.ui-icon-circle-triangle-w")).click();
    driver.findElement(By.cssSelector("span.ui-icon.ui-icon-circle-triangle-w")).click();
    driver.findElement(By.cssSelector("span.ui-icon.ui-icon-circle-triangle-w")).click();
    driver.findElement(By.cssSelector("span.ui-icon.ui-icon-circle-triangle-w")).click();
    driver.findElement(By.xpath("//input[@wicketpath='form_dateOfBirth']")).clear();
    driver.findElement(By.xpath("//input[@wicketpath='form_dateOfBirth']")).sendKeys("1/1/1980");
    driver.findElement(By.xpath("//input[@wicketpath='form_gender_input_0']")).click();
    driver.findElement(By.xpath("//textarea[@wicketpath='form_note']")).clear();
    driver.findElement(By.xpath("//textarea[@wicketpath='form_note']")).sendKeys("New user");
    new Select(driver.findElement(By.xpath("//select[@wicketpath='form_educationLevel']"))).selectByVisibleText("1 NotKnown");
    driver.findElement(By.xpath("//input[@wicketpath='form_submit']")).click();
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
