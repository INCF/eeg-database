package cz.zcu.kiv.eegdatabase.selenium.generated;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class AddExperiment {
	private WebDriver driver;
	private String baseUrl="";
	private StringBuffer verificationErrors = new StringBuffer();
	@Before
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testAddExperiment() throws Exception {
		driver.get("/home.html");
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys("pitrs");
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("pitrs");
		driver.findElement(By.cssSelector("input.lightButtonLink")).click();
		driver.findElement(By.linkText("Experiments")).click();
		driver.findElement(By.linkText("Add experiment")).click();
		// ERROR: Caught exception [ERROR: Unsupported command [select]]
		driver.findElement(By.name("_target1")).click();
		// ERROR: Caught exception [ERROR: Unsupported command [select]]
		// ERROR: Caught exception [ERROR: Unsupported command [addSelection]]
		// ERROR: Caught exception [ERROR: Unsupported command [select]]
		driver.findElement(By.id("temperature")).clear();
		driver.findElement(By.id("temperature")).sendKeys("33");
		driver.findElement(By.name("_target2")).click();
		driver.findElement(By.name("dataFile")).clear();
		driver.findElement(By.name("dataFile")).sendKeys("C:\\Users\\Seky\\Downloads\\test.txt");
		driver.findElement(By.name("_finish")).click();
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
