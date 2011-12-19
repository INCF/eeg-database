package cz.zcu.kiv.eegdatabase.selenium.generated;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class AddScenarioWithoutDataWebDrive {
	private WebDriver driver;
	private String baseUrl="";
	private StringBuffer verificationErrors = new StringBuffer();
	@Before
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testAddScenarioWithoutDataWebDrive() throws Exception {
		driver.get("/home.html");
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("pitrs");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pitrs");
		driver.findElement(By.cssSelector("input.lightButtonLink")).click();
		driver.findElement(By.linkText("Scenarios")).click();
		driver.findElement(By.linkText("Add scenario")).click();
		// ERROR: Caught exception [ERROR: Unsupported command [select]]
		driver.findElement(By.id("title")).clear();
		driver.findElement(By.id("title")).sendKeys("TESS_TT_SCENAR");
		driver.findElement(By.id("length")).clear();
		driver.findElement(By.id("length")).sendKeys("55");
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("TEST SCENARIO WITHOUT DATA FILE");
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
