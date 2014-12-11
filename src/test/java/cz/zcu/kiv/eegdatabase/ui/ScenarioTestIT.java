/*******************************************************************************
 * This file is part of the EEG-database project
 *
 * ==========================================
 *
 * Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 * ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * ***********************************************************************************************************************
 *
 * ScenarioTestIT.java, 2014/11/25 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.ui;

import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.Util;
import net.sourceforge.jwebunit.junit.WebTester;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Honza on 30.11.14.
 */
public class ScenarioTestIT extends AbstractUITest {
    private WebTester tester;

    @Autowired
    private PersonDao personDao;

    @BeforeMethod(groups = "web")
    public void setUp() {
        if (!personDao.usernameExists("jan.stebetak@seznam.cz")) {
            Person person = TestUtils.createPersonForTesting("jan.stebetak@seznam.cz", Util.ROLE_USER);
            person.setConfirmed(true);
            personDao.create(person);
        }

        tester = new WebTester();
        // tester.setBaseUrl("http://eeg2.kiv.zcu.cz:8080");
        tester.setBaseUrl(url);
        tester.beginAt("/home-page");
        tester.setTextField("userName", "jan.stebetak@seznam.cz");
        tester.setTextField("password", "stebjan");
        tester.clickButtonWithText("Log in");
        tester.assertTextPresent("Log out");

    }
    @Test(groups = "web")
    public void testScenarioValidation() throws InterruptedException {

        createGroupIfNotExists();

        tester.clickLinkWithText("Scenarios");
        tester.assertLinkPresentWithText("Add scenario");
        tester.clickLinkWithText("Add scenario");

        tester.setTextField("title", "");
        tester.setTextField("description", "");
        tester.clickButtonWithText("Save");
        Thread.sleep(waitForAjax);
        tester.assertTextPresent("Field 'Research group' is required.");
        tester.assertTextPresent("Field 'Scenario title' is required.");
        tester.assertTextPresent("Field 'Description' is required.");

        tester.selectOption("researchGroup", "new group");
        tester.setTextField("title", "test");
        tester.setTextField("description", "test");
        tester.setTextField("scenarioLength", "-1");
        tester.clickButtonWithText("Save");
        Thread.sleep(waitForAjax);
        tester.assertTextPresent("'scenarioLength'");   //TODO validation message
        tester.clickLinkWithText("Log out");

    }
    @Test(groups = "web")
    public void testAddScenario() throws InterruptedException {

        createGroupIfNotExists();

        createScenario("testScenario");
        tester.clickButtonWithText("Save");
        Thread.sleep(waitForAjax);

        tester.assertTextPresent("Scenario detail");
        tester.assertTextPresent("testScenario");
        tester.assertTextPresent("This scenario contains no data file. ");
        tester.clickLinkWithText("Log out");

    }

    @Test(groups = "web")
    public void testAddScenarioWithFileValidation() throws InterruptedException, IOException {

        createGroupIfNotExists();
        createScenario("testScenario2");
        File file = createFile();
        Assert.assertNotNull(file, "Error while creating file for scenario");
        tester.checkCheckbox("dataAvailable");

        tester.clickButtonWithText("Save");
        Thread.sleep(waitForAjax);

        tester.assertTextPresent("Field 'Data file' is required.");
        tester.setFormElement("contailer:file", file.getAbsolutePath());
        tester.clickButtonWithText("Save");
        Thread.sleep(waitForAjax);

        tester.assertTextPresent("Scenario detail");
        tester.assertTextPresent("testScenario2");
        tester.assertLinkPresentWithText("Download scenario file");

        tester.clickLinkWithText("Log out");
        file.delete();

    }
    @Test(groups = "web")
    public void testUniqueTitle() throws InterruptedException {

        createGroupIfNotExists();
        tester.clickLinkWithText("Scenarios");
        try {
            tester.assertTextPresent("testScenario");
        } catch (AssertionError er) {
            createScenario("testScenario"); //create if not exists
            tester.clickButtonWithText("Save");
            Thread.sleep(waitForAjax);
        }

        createScenario("testScenario");
        tester.clickButtonWithText("Save");
        Thread.sleep(waitForAjax);

        tester.assertTextPresent("Title already in database.");
        tester.clickLinkWithText("Log out");

    }


    private void createGroupIfNotExists() throws InterruptedException {
        tester.clickLinkWithText("Groups");
        try {
            tester.assertTextPresent("new group");
        } catch (AssertionError ex) {
            tester.clickLinkWithText("Create group");
            tester.setTextField("title", "new group");
            tester.setTextField("description", "description");
            tester.clickButtonWithText("Save");
            Thread.sleep(waitForAjax);
        }

    }
    private void createScenario(String title) {
        tester.clickLinkWithText("Scenarios");
        tester.assertLinkPresentWithText("Add scenario");
        tester.clickLinkWithText("Add scenario");

        tester.selectOption("researchGroup", "new group");
        tester.setTextField("title", title);
        tester.setTextField("description", "description");
        tester.setTextField("scenarioLength", "10");


    }
    private File createFile() throws IOException {
        File tmpFile = null;
        FileOutputStream fos = null;
        try {
            tmpFile = File.createTempFile("tmpDataFile", null);
            fos = new FileOutputStream(tmpFile);
            byte[] buffer = new byte[1024];
            Arrays.fill(buffer, (byte) 0x0C);
            for (int i = 0; i < 1024; i++) {
                fos.write(buffer);
            }
        } catch (IOException e) {
            tmpFile = null;
        } finally {
            if (fos != null) fos.close();
            return tmpFile;
        }
    }
}
