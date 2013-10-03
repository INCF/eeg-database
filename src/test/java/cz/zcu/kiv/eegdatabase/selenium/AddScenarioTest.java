/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   AddScenarioTest.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.selenium;

import com.thoughtworks.selenium.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Koren
 * Date: 19.6.11
 * To change this template use File | Settings | File Templates.
 */
public class AddScenarioTest extends SeleneseTestCase {

     private final String FILE_PATH = "E:\\Skola\\5 semestr\\PRJ5\\XML scenarios\\p300\\xml\\scenarios.xml";

	@Before
	public void setUp() throws Exception {
		selenium = new DefaultSelenium("localhost", 4444, "*firefox", "http://localhost:8080");
		selenium.start();
	}

    @Test
	public void testTitleNotSet() throws Exception {
		selenium.open("/EEGDatabase/home.html");
		selenium.type("j_username", "pitrs");
		selenium.type("j_password", "pitrs");
		selenium.click("css=input.lightButtonLink");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Scenarios");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Add scenario");
		selenium.waitForPageToLoad("30000");
		selenium.type("title", "");
		selenium.type("length", "10");
		selenium.type("description", "description");
		selenium.click("isScenarioXml");
		selenium.click("noSchema");
		selenium.type("css=input[name=dataFileXml]", FILE_PATH);
		selenium.click("css=input.submitButton.lightButtonLink");
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isElementPresent("title.errors"));
		verifyTrue(selenium.isTextPresent("Field is required."));
		selenium.click("link=Log out");
		selenium.waitForPageToLoad("30000");
	}

    @Test
	public void testTitleUsed() throws Exception {
		selenium.open("/EEGDatabase/home.html");
		selenium.type("j_username", "pitrs");
		selenium.type("j_password", "pitrs");
		selenium.click("css=input.lightButtonLink");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Scenarios");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Add scenario");
		selenium.waitForPageToLoad("30000");
		selenium.type("title", "p300");
		selenium.type("length", "10");
		selenium.type("description", "description");
		selenium.click("isScenarioXml");
		selenium.click("noSchema");
		selenium.type("css=input[name=dataFileXml]", FILE_PATH);
		selenium.click("css=input.submitButton.lightButtonLink");
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isElementPresent("title.errors"));
		verifyTrue(selenium.isTextPresent("Value already in database."));
		selenium.click("link=Log out");
		selenium.waitForPageToLoad("30000");
	}

    @Test
	public void testLengthNotSet() throws Exception {
		selenium.open("/EEGDatabase/home.html");
		selenium.type("j_username", "pitrs");
		selenium.type("j_password", "pitrs");
		selenium.click("css=input.lightButtonLink");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Scenarios");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Add scenario");
		selenium.waitForPageToLoad("30000");
		selenium.type("title", "test");
		selenium.type("length", "");
		selenium.type("description", "description");
		selenium.click("isScenarioXml");
		selenium.click("noSchema");
		selenium.type("css=input[name=dataFileXml]", FILE_PATH);
		selenium.click("css=input.submitButton.lightButtonLink");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isElementPresent("length.errors"));
		verifyTrue(selenium.isTextPresent("Scenario length must be a number."));
		selenium.click("link=Log out");
		selenium.waitForPageToLoad("30000");
	}

    @Test
	public void testDescriptionNotSet() throws Exception {
		selenium.open("/EEGDatabase/home.html");
		selenium.type("j_username", "pitrs");
		selenium.type("j_password", "pitrs");
		selenium.click("css=input.lightButtonLink");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Scenarios");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Add scenario");
		selenium.waitForPageToLoad("30000");
		selenium.type("title", "test");
		selenium.type("length", "10");
		selenium.type("description", "");
		selenium.click("isScenarioXml");
		selenium.click("noSchema");
		selenium.type("css=input[name=dataFileXml]", FILE_PATH);
		selenium.click("css=input.submitButton.lightButtonLink");
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isElementPresent("description.errors"));
		assertTrue(selenium.isElementPresent("description.errors"));
		verifyTrue(selenium.isTextPresent("Field is required."));
		selenium.click("link=Log out");
		selenium.waitForPageToLoad("30000");
	}

    @Test
	public void testDataFileNotSet() throws Exception {
		selenium.open("/EEGDatabase/home.html");
		selenium.type("j_username", "pitrs");
		selenium.type("j_password", "pitrs");
		selenium.click("css=input.lightButtonLink");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Scenarios");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Add scenario");
		selenium.waitForPageToLoad("30000");
		selenium.type("title", "p300");
		selenium.type("length", "10");
		selenium.type("description", "description");
        if(selenium.isChecked("isScenarioXml")) {
            selenium.click("isScenarioXml");
        }
		selenium.click("css=input.submitButton.lightButtonLink");
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isElementPresent("dataFile.errors"));
		verifyTrue(selenium.isTextPresent("No data file was inserted."));
		selenium.click("link=Log out");
		selenium.waitForPageToLoad("30000");
	}

    @Test
	public void testDataFileXmlNotSet() throws Exception {
		selenium.open("/EEGDatabase/home.html");
		selenium.type("j_username", "pitrs");
		selenium.type("j_password", "pitrs");
		selenium.click("css=input.lightButtonLink");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Scenarios");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Add scenario");
		selenium.waitForPageToLoad("30000");
		selenium.type("title", "test");
		selenium.type("length", "10");
		selenium.type("description", "description");
        if(!selenium.isChecked("isScenarioXml")) {
            selenium.click("isScenarioXml");
        }
		selenium.click("noSchema");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isElementPresent("dataFileXml")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.type("css=input[name=dataFileXml]", "");
		selenium.click("css=input.submitButton.lightButtonLink");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("No XML data file was inserted.")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		verifyTrue(selenium.isTextPresent("No XML data file was inserted."));
		selenium.click("link=Log out");
		selenium.waitForPageToLoad("30000");
	}

    @Test
	public void testScenarioSchemaNotSet() throws Exception {
		selenium.open("/EEGDatabase/home.html");
		selenium.type("j_username", "pitrs");
		selenium.type("j_password", "pitrs");
		selenium.click("css=input.lightButtonLink");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Scenarios");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Add scenario");
		selenium.waitForPageToLoad("30000");
		selenium.type("title", "p300");
		selenium.type("length", "10");
		selenium.type("description", "description");
        if(!selenium.isChecked("isScenarioXml")) {
            selenium.click("isScenarioXml");
        }
		selenium.type("css=input[name=dataFileXml]", FILE_PATH);
		selenium.click("fromSchemaList");
		selenium.click("css=input.submitButton.lightButtonLink");
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isElementPresent("scenarioSchema.errors"));
		verifyTrue(selenium.isTextPresent("Scenario schema must be selected."));
		selenium.click("link=Log out");
		selenium.waitForPageToLoad("30000");
	}

    @Test
	public void testWithSchemaDataFileXmlNotSet() throws Exception {
		selenium.open("/EEGDatabase/home.html");
		selenium.type("j_username", "pitrs");
		selenium.type("j_password", "pitrs");
		selenium.click("css=input.lightButtonLink");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Scenarios");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Add scenario");
		selenium.waitForPageToLoad("30000");
		selenium.type("title", "p300");
		selenium.type("length", "10");
		selenium.type("description", "description");
		if(!selenium.isChecked("isScenarioXml")) {
            selenium.click("isScenarioXml");
        }
		selenium.click("fromSchemaList");
		selenium.select("schemaList", "label=p300.xsd");
		selenium.click("css=input.submitButton.lightButtonLink");
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isElementPresent("dataFileXml.errors"));
		verifyTrue(selenium.isTextPresent("No XML data file was inserted."));
		selenium.click("link=Log out");
		selenium.waitForPageToLoad("30000");
	}

    @Test
	public void testWithNoSchemaNoDataFileXmlSet() throws Exception {
		selenium.open("/EEGDatabase/home.html");
		selenium.type("j_username", "pitrs");
		selenium.type("j_password", "pitrs");
		selenium.click("css=input.lightButtonLink");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Scenarios");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Add scenario");
		selenium.waitForPageToLoad("30000");
		selenium.type("title", "p300");
		selenium.type("length", "10");
		selenium.type("description", "description");
		if(!selenium.isChecked("isScenarioXml")) {
            selenium.click("isScenarioXml");
        }
		selenium.click("noSchema");
		selenium.click("css=input.submitButton.lightButtonLink");
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isElementPresent("dataFileXml.errors"));
		verifyTrue(selenium.isTextPresent("No XML data file was inserted."));
		selenium.click("link=Log out");
		selenium.waitForPageToLoad("30000");
	}

	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}
}
