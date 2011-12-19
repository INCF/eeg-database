package cz.zcu.kiv.eegdatabase.selenium.generated;

import com.thoughtworks.selenium.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.regex.Pattern;

public class AddPersonNoWebDrive extends SeleneseTestCase {
	@Before
	public void setUp() throws Exception {
		selenium = new DefaultSelenium("localhost", 4444, "*chrome", "http://localhost:8080/");
		selenium.start();
	}

	@Test
	public void testAddPersonNoWebDrive() throws Exception {
		selenium.open("/home.html");
		selenium.type("id=j_username", "pitrs");
		selenium.type("id=j_password", "pitrs");
		selenium.click("css=input.lightButtonLink");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=People");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Add person");
		selenium.waitForPageToLoad("30000");
		selenium.type("id=givenname", "TESTsss");
		selenium.type("id=surname", "Testovaci");
		selenium.click("id=dateOfBirth");
		selenium.type("id=dateOfBirth", "22/11/1980");
		selenium.click("//form[@id='addPerson']/fieldset/div[4]");
		selenium.click("id=gender1");
		selenium.type("id=email", "taziman03@seznam.cz");
		selenium.type("id=phoneNumber", "777333888");
		selenium.type("id=note", "Testovaci uziv");
		selenium.click("css=input.submitButton.lightButtonLink");
		selenium.waitForPageToLoad("30000");
	}

	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}
}
