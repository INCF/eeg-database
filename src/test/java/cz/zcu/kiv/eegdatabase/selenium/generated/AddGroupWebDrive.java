package cz.zcu.kiv.eegdatabase.selenium.generated;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class AddGroupWebDrive {
	private WebDriver driver;
	private String baseUrl="";
	private StringBuffer verificationErrors = new StringBuffer();
	@Before
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testAddGroupWebDrive() throws Exception {
		driver.get("/home.html");
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("pitrs");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pitrs");
		driver.findElement(By.cssSelector("input.lightButtonLink")).click();
		driver.findElement(By.linkText("Groups")).click();
		driver.findElement(By.linkText("Create group")).click();
		driver.findElement(By.id("researchGroupTitle")).clear();
		driver.findElement(By.id("researchGroupTitle")).sendKeys("TESTGROUPE");
		driver.findElement(By.id("researchGroupDescription")).clear();
		driver.findElement(By.id("researchGroupDescription")).sendKeys("Testovací grupa");
		driver.findElement(By.cssSelector("input.submitButton.lightButtonLink")).click();
		driver.findElement(By.linkText("Add member to group")).click();
		driver.findElement(By.id("userName")).clear();
		driver.findElement(By.id("userName")).sendKeys("jan");
		// ERROR: Caught exception [ERROR: Unsupported command [select]]
		driver.findElement(By.cssSelector("input.submitButton.lightButtonLink")).click();
		driver.findElement(By.linkText("Add member to group")).click();
		driver.findElement(By.id("userName")).clear();
		driver.findElement(By.id("userName")).sendKeys("test155");
		// ERROR: Caught exception [ERROR: Unsupported command [select]]
		driver.findElement(By.cssSelector("input.submitButton.lightButtonLink")).click();
		driver.findElement(By.linkText("Back to group detail")).click();
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
