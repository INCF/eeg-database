package cz.zcu.kiv.eegdatabase.selenium.generated;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class EditScenarioWebDrive {
	private WebDriver driver;
	private String baseUrl="";
	private StringBuffer verificationErrors = new StringBuffer();
	@Before
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testEditScenarioWebDrive() throws Exception {
		driver.get("/home.html");
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("pitrs");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pitrs");
		driver.findElement(By.cssSelector("input.lightButtonLink")).click();
		driver.findElement(By.linkText("Scenarios")).click();
		driver.findElement(By.xpath("//a[contains(@href, 'detail.html?scenarioId=680')]")).click();
		driver.findElement(By.linkText("Edit")).click();
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("TEST SCENARIO WITHOUT DATA FILE AND EDITING");
		driver.findElement(By.cssSelector("input.submitButton.lightButtonLink")).click();
		driver.findElement(By.xpath("//a[contains(@href, 'detail.html?scenarioId=680')]")).click();
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
