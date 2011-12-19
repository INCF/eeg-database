package cz.zcu.kiv.eegdatabase.selenium.generated;

import com.thoughtworks.selenium.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.regex.Pattern;

public class AddExperimentNoWebDrive extends SeleneseTestCase {
	@Before
	public void setUp() throws Exception {
		selenium = new DefaultSelenium("localhost", 4444, "*chrome", "http://localhost:8080/");
		selenium.start();
	}

	@Test
	public void testAddExperimentNoWebDrive() throws Exception {
		selenium.open("/home.html");
		selenium.type("id=j_username", "pitrs");
		selenium.type("id=j_password", "pitrs");
		selenium.click("css=input.lightButtonLink");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Experiments");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Add experiment");
		selenium.waitForPageToLoad("30000");
		selenium.select("id=subjectPerson", "label=Gott Karel");
		selenium.click("name=_target1");
		selenium.waitForPageToLoad("30000");
		selenium.select("id=selectScenario", "label=ERP_BANG!");
		selenium.addSelection("id=selectHardware", "label=Intel");
		selenium.select("id=selectWeather", "label=sunny");
		selenium.type("id=temperature", "33");
		selenium.click("name=_target2");
		selenium.waitForPageToLoad("30000");
		selenium.type("name=dataFile", "C:\\Users\\Seky\\Downloads\\test.txt");
		selenium.click("name=_finish");
		selenium.waitForPageToLoad("30000");
	}

	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}
}
