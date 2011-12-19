package cz.zcu.kiv.eegdatabase.selenium.generated;

import com.thoughtworks.selenium.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.regex.Pattern;

public class AddScenarioWithoutDataNoWebDrive extends SeleneseTestCase {
	@Before
	public void setUp() throws Exception {
		selenium = new DefaultSelenium("localhost", 4444, "*chrome", "http://localhost:8080/");
		selenium.start();
	}

	@Test
	public void testAddScenarioWithoutDataNoWebDrive() throws Exception {
		selenium.open("/home.html");
		selenium.type("id=j_username", "pitrs");
		selenium.type("id=j_password", "pitrs");
		selenium.click("css=input.lightButtonLink");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Scenarios");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Add scenario");
		selenium.waitForPageToLoad("30000");
		selenium.select("id=researchGroup", "label=superskupina");
		selenium.type("id=title", "TESS_TT_SCENAR");
		selenium.type("id=length", "55");
		selenium.type("id=description", "TEST SCENARIO WITHOUT DATA FILE");
		selenium.click("css=input.submitButton.lightButtonLink");
		selenium.waitForPageToLoad("30000");
		selenium.click("//a[contains(@href, 'detail.html?scenarioId=680')]");
		selenium.waitForPageToLoad("30000");
	}

	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}
}
