package cz.zcu.kiv.eegdatabase.selenium.generated;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class AddPersonWebDrive {
	private WebDriver driver;
	private String baseUrl="";
	private StringBuffer verificationErrors = new StringBuffer();
	@Before
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testAddPersonWebDrive() throws Exception {
		driver.get("/home.html");
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("pitrs");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pitrs");
		driver.findElement(By.cssSelector("input.lightButtonLink")).click();
		driver.findElement(By.linkText("People")).click();
		driver.findElement(By.linkText("Add person")).click();
		driver.findElement(By.id("givenname")).clear();
		driver.findElement(By.id("givenname")).sendKeys("TESTsss");
		driver.findElement(By.id("surname")).clear();
		driver.findElement(By.id("surname")).sendKeys("Testovaci");
		driver.findElement(By.id("dateOfBirth")).click();
		driver.findElement(By.id("dateOfBirth")).clear();
		driver.findElement(By.id("dateOfBirth")).sendKeys("22/11/1980");
		driver.findElement(By.xpath("//form[@id='addPerson']/fieldset/div[4]")).click();
		driver.findElement(By.id("gender1")).click();
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys("taziman03@seznam.cz");
		driver.findElement(By.id("phoneNumber")).clear();
		driver.findElement(By.id("phoneNumber")).sendKeys("777333888");
		driver.findElement(By.id("note")).clear();
		driver.findElement(By.id("note")).sendKeys("Testovaci uziv");
		driver.findElement(By.cssSelector("input.submitButton.lightButtonLink")).click();
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
}
