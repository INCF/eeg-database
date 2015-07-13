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
 * ExperimentTestIT.java, 2014/12/04 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.ui;

import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.dao.ScenarioDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.Util;
import net.sourceforge.jwebunit.api.IElement;
import net.sourceforge.jwebunit.junit.WebTester;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Honza on 4.12.14.
 */
public class ExperimentTestIT extends AbstractUITest {

    @Autowired
    private PersonDao personDao;

    @Autowired
    private ResearchGroupDao researchGroupDao;

    @BeforeMethod(groups = "web")
    public void setUp() throws IOException {
        if (!personDao.usernameExists("jan.stebetak@seznam.cz")) {
            Person person = TestUtils.createPersonForTesting("jan.stebetak@seznam.cz", Util.ROLE_USER);
            person.setConfirmed(true);
            personDao.create(person);
        }


        tester = new WebTester();
        tester.setBaseUrl(url);
        tester.beginAt("/home-page");
        tester.setTextField("userName", "jan.stebetak@seznam.cz");
        tester.setTextField("password", "stebjan");
        tester.clickButtonWithText(getProperty("action.login"));
        tester.assertTextPresent(getProperty("action.logout"));

    }
    @Test(groups = "web")
    public void testAddExperimentPermission() throws InterruptedException, IOException {
        createGroupIfNotExists();
        tester.clickLinkWithText(getProperty("menuItem.experiments"));
        tester.assertLinkPresentWithText(getProperty("menuItem.experiments.addExperiment"));

        tester.clickLinkWithText(getProperty("action.logout"));

        /*
         * This person is admin but it is not member of any group. He should not be able to create new experiments.
         */
        if (!personDao.usernameExists("jan.stebetak2@seznam.cz")) {
            Person person = TestUtils.createPersonForTesting("jan.stebetak2@seznam.cz", Util.ROLE_ADMIN);
            person.setConfirmed(true);
            personDao.create(person);
        }
        tester.setTextField("userName", "jan.stebetak2@seznam.cz");
        tester.setTextField("password", "stebjan");
        tester.clickButtonWithText(getProperty("action.login"));
        tester.assertTextPresent(getProperty("action.logout"));

        tester.clickLinkWithText(getProperty("menuItem.experiments"));
        tester.assertLinkNotPresentWithText(getProperty("menuItem.experiments.addExperiment"));

        tester.clickLinkWithText(getProperty("action.logout"));

    }

    @Test(groups = "web")
    public void testExperimentValidation() throws InterruptedException, IOException {

        createGroupIfNotExists();
        prepareMetadata();

        tester.clickLinkWithText(getProperty("menuItem.experiments"));
        tester.assertLinkPresentWithText(getProperty("menuItem.experiments.addExperiment"));
        tester.clickLinkWithText(getProperty("menuItem.experiments.addExperiment"));

        tester.setTextField("view:scenario", "");
        tester.setTextField("view:personBySubjectPersonId", "");
        tester.clickButtonWithText("Next >");
        Thread.sleep(waitForAjax);
        tester.assertTextPresent("Field 'Group' is required.");
        tester.assertTextPresent("Field 'Scenario' is required.");
//        tester.assertTextPresent("Field 'Subject person' is required.");

        tester.selectOption("view:researchGroup", "new group");
//        Thread.sleep(waitForAjax);
//        tester.assertTextPresent("Research group new group can't create or edit experiment. Research group is locked.");


        tester.setTextField("view:scenario", "scenarioForExperiment");
        tester.setTextField("view:personBySubjectPersonId", "jan.stebetak@seznam.cz");
        tester.clickButtonWithText("Next >");
        Thread.sleep(waitForAjax);
        //Next page with hw, sw, ...
//        tester.clickButtonWithText("Next >");
//        Thread.sleep(waitForAjax);

//        tester.assertTextPresent("Field 'Hardware' is required.");
//        tester.assertTextPresent("Field 'Software' is required.");
//        tester.assertTextPresent("Field 'Weather' is required.");
//
//        tester.selectOption("view:hardwares", "HardwareForExperiment");
//        tester.selectOption("view:softwares", "SoftwareForExperiment");
//        tester.selectOption("view:weather", "WeatherForExperiment");
//        tester.clickButtonWithText("Next >");
//        Thread.sleep(waitForAjax);
        tester.clickButtonWithText("Finish");
        Thread.sleep(waitForAjax);
        tester.assertTextNotPresent(getProperty("text.group.lock.experiment.create"));
        tester.clickLinkWithText(getProperty("action.logout"));

    }
    @Test(groups = "web", dependsOnMethods = {"testExperimentValidation"})
    public void testAddExperiment() throws InterruptedException, IOException {

        unlockGroup();

        tester.clickLinkWithText(getProperty("menuItem.experiments"));
        tester.assertLinkPresentWithText(getProperty("menuItem.experiments.addExperiment"));
        tester.clickLinkWithText(getProperty("menuItem.experiments.addExperiment"));

        tester.selectOption("view:researchGroup", "new group");
        Thread.sleep(waitForAjax);
        tester.assertTextNotPresent(getProperty("text.group.lock.experiment.create"));

        tester.setTextField("view:scenario", "scenarioForExperiment");
        tester.setTextField("view:personBySubjectPersonId", "jan.stebetak@seznam.cz");
        tester.clickButtonWithText("Next >");
        Thread.sleep(waitForAjax);
//        //Next page with hw, sw, ...
//
//        tester.selectOption("view:hardwares", "HardwareForExperiment");
//        tester.selectOption("view:softwares", "SoftwareForExperiment");
//        tester.selectOption("view:weather", "WeatherForExperiment");
//        tester.clickButtonWithText("Next >");
//        Thread.sleep(waitForAjax);

        tester.clickButtonWithText("Finish");
        Thread.sleep(waitForAjax);
        tester.assertTextPresent(getProperty("required.dataFile"));
        File file = createFile();
        IElement element = tester.getElementByXPath("//input[@wicketpath='wizard_form_view_resultFile_upload']");
        element.setTextContent(file.getAbsolutePath());
        tester.clickButtonWithText("Finish");
        Thread.sleep(waitForAjax);
        tester.clickLinkWithText(getProperty("action.logout"));
        file.delete();

    }

    private void prepareMetadata() throws InterruptedException, IOException {
        tester.clickLinkWithText(getProperty("menuItem.scenarios"));
        tester.assertLinkPresentWithText(getProperty("menuItem.addScenario"));
        tester.clickLinkWithText(getProperty("menuItem.addScenario"));

        tester.selectOption("researchGroup", "new group");
        tester.setTextField("title", "scenarioForExperiment");
        tester.setTextField("description", "description");
        tester.setTextField("scenarioLength", "10");

        tester.clickButtonWithText(getProperty("button.save"));
        Thread.sleep(waitForAjax);

        tester.clickLinkWithText(getProperty("menuItem.lists"));
        tester.assertLinkPresentWithText(getProperty("menuItem.hardwareDefinitions"));
        tester.clickLinkWithText(getProperty("menuItem.hardwareDefinitions"));

        tester.assertLinkPresentWithText(getProperty("link.addHardwareDefinition"));
        tester.clickLinkWithText(getProperty("link.addHardwareDefinition"));
        Thread.sleep(waitForAjax);
        tester.setTextField("title", "HardwareForExperiment");
        tester.setTextField("type", "type");
        tester.setTextField("description", "desc");
        tester.clickButtonWithText(getProperty("button.save"));
        Thread.sleep(waitForAjax);

        tester.assertLinkPresentWithText(getProperty("menuItem.softwareDefinitions"));
        tester.clickLinkWithText(getProperty("menuItem.softwareDefinitions"));

        tester.assertLinkPresentWithText(getProperty("link.addSoftwareDefinition"));
        tester.clickLinkWithText(getProperty("link.addSoftwareDefinition"));
        Thread.sleep(waitForAjax);
        tester.setTextField("title", "SoftwareForExperiment");
        tester.setTextField("description", "desc");
        tester.clickButtonWithText(getProperty("button.save"));
        Thread.sleep(waitForAjax);

        tester.assertLinkPresentWithText(getProperty("menuItem.weatherDefinitions"));
        tester.clickLinkWithText(getProperty("menuItem.weatherDefinitions"));

        tester.assertLinkPresentWithText(getProperty("link.addWeatherDefinition"));
        tester.clickLinkWithText(getProperty("link.addWeatherDefinition"));
        Thread.sleep(waitForAjax);
        tester.setTextField("title", "WeatherForExperiment");
        tester.setTextField("description", "desc");
        tester.clickButtonWithText(getProperty("button.save"));
        Thread.sleep(waitForAjax);

    }

    private void unlockGroup() {
        List<ResearchGroup> groups = researchGroupDao.getAllRecords();
        for (ResearchGroup group: groups) {
            if (group.getTitle().equals("new group")) {
                group.setLock(false);
                researchGroupDao.update(group);
                break;
            }

        }

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
        }
        return tmpFile;
    }

}
