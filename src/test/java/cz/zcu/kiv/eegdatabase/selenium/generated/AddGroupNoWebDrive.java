package cz.zcu.kiv.eegdatabase.selenium.generated;

import com.thoughtworks.selenium.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.regex.Pattern;

public class AddGroupNoWebDrive extends SeleneseTestCase {
	@Before
	public void setUp() throws Exception {
		selenium = new DefaultSelenium("localhost", 4444, "*chrome", "http://localhost:8080/");
		selenium.start();
	}

	@Test
	public void testAddGroupNoWebDrive() throws Exception {
		selenium.open("/home.html");
		selenium.type("id=j_username", "pitrs");
		selenium.type("id=j_password", "pitrs");
		selenium.click("css=input.lightButtonLink");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Groups");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Create group");
		selenium.waitForPageToLoad("30000");
		selenium.type("id=researchGroupTitle", "TESTGROUPE");
		selenium.type("id=researchGroupDescription", "Testovací grupa");
		selenium.click("css=input.submitButton.lightButtonLink");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Add member to group");
		selenium.waitForPageToLoad("30000");
		selenium.type("id=userName", "jan");
		selenium.select("id=userRole", "label=Reader");
		selenium.click("css=input.submitButton.lightButtonLink");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Add member to group");
		selenium.waitForPageToLoad("30000");
		selenium.type("id=userName", "test155");
		selenium.select("id=userRole", "label=Experimenter");
		selenium.click("css=input.submitButton.lightButtonLink");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Back to group detail");
		selenium.waitForPageToLoad("30000");
	}

	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}
}
