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
 *   AddScenarioSchemaTest.java, 2013/10/02 00:01 Jakub Rinkes
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
public class AddScenarioSchemaTest extends SeleneseTestCase {

    private final String FILE_PATH = "E:\\Skola\\5 semestr\\PRJ5\\XML scenarios\\p300\\xml\\070608_p300.xsd";

    @Before
	public void setUp() throws Exception {
		selenium = new DefaultSelenium("localhost", 4444, "*firefox", "http://localhost:8080/");
		selenium.start();
	}

    @Test
	public void testAddSchemaFileNotSet() throws Exception {
		selenium.open("/EEGDatabase/home.html");
		selenium.type("j_username", "pitrs");
		selenium.type("j_password", "pitrs");
		selenium.click("css=input.lightButtonLink");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Scenarios");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Add scenario schema");
		selenium.waitForPageToLoad("30000");
		selenium.type("schemaDescription", "description");
		selenium.click("css=input.submitButton.lightButtonLink");
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isElementPresent("schemaFile.errors"));
		verifyTrue(selenium.isTextPresent("No schema file was inserted."));
		selenium.click("link=Log out");
		selenium.waitForPageToLoad("30000");
	}

    @Test
	public void testAddSchemaDescriptionNotSet() throws Exception {
		selenium.open("/EEGDatabase/home.html");
		selenium.type("j_username", "pitrs");
		selenium.type("j_password", "pitrs");
		selenium.click("css=input.lightButtonLink");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Scenarios");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Add scenario schema");
		selenium.waitForPageToLoad("30000");
		selenium.type("css=input[name=schemaFile]", FILE_PATH);
		selenium.click("css=input.submitButton.lightButtonLink");
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isElementPresent("schemaDescription.errors"));
		verifyTrue(selenium.isTextPresent("Field is required."));
		selenium.click("link=Log out");
		selenium.waitForPageToLoad("30000");
	}

	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}

}
