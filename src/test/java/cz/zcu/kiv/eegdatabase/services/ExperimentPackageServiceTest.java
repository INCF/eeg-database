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
 * ExperimentPackageServiceTest.java, 2014/08/20 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.services;

import static org.testng.Assert.assertEquals;

import java.math.BigDecimal;

import cz.zcu.kiv.eegdatabase.data.ExperimentGenerator;
import cz.zcu.kiv.eegdatabase.data.dao.ExperimentDao;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.dao.LicenseDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageService;

/**
 * Created by stebjan on 20.8.2014.
 */

public class ExperimentPackageServiceTest extends AbstractServicesTest{

    @Autowired
    private ExperimentPackageService experimentPackageService;

    @Autowired
    private ExperimentPackageConnectionService experimentPackageConnectionService;

    @Autowired
    private LicenseDao licenseDao;

    private ExperimentPackage experimentPackage;

    private ExperimentPackageConnection connection;

    @Autowired
    private ExperimentDao experimentDao;

    @Autowired
    private ExperimentGenerator experimentGenerator;

    @Autowired
    private ResearchGroupDao researchGroupDao;

    @Autowired
    private PersonDao personDao;

    private ResearchGroup researchGroup;
    private Experiment experiment;
    private Person person;
    private License license;

    @BeforeMethod(groups = "unit")
    public void setUp() {
        person = TestUtils.createPersonForTesting("test@test.com", Util.ROLE_ADMIN);

        personDao.create(person);

        experiment = experimentGenerator.generateExperiment(person);

        researchGroup = new ResearchGroup();
        researchGroup.setDescription("test-description");
        researchGroup.setTitle("test-title");
        researchGroup.setPerson(person);
        researchGroup.setPaidAccount(true);
        researchGroupDao.create(researchGroup);

        experimentPackage = new ExperimentPackage();
        experimentPackage.setName("test name");
        experimentPackage.setResearchGroup(researchGroup);

        connection = new ExperimentPackageConnection();

        license = new License();
        license.setDescription("junit@test.description");
        license.setLicenseId(-231);
        license.setPrice(BigDecimal.valueOf(-1000f));
        license.setTitle("title");
        license.setLicenseType(LicenseType.OWNER);
        license.setResearchGroup(researchGroup);
        licenseDao.create(license);
    }

    @Test(groups = "unit")
    public void testCreateExperimentPackage() {

        int countBefore = experimentPackageService.getCountRecords();
        experimentPackageService.create(experimentPackage, license);
        assertEquals(countBefore + 1, experimentPackageService.getCountRecords());
    }

    @Test(groups = "unit")
    public void testCreateExperimentPackageConnection() {

        experimentPackageService.create(experimentPackage, license);
        experimentDao.create(experiment);

        int countBefore = experimentPackageConnectionService.getCountRecords();
        connection.setExperimentPackage(experimentPackage);
        connection.setExperiment(experiment);
        experimentPackageConnectionService.create(connection);

        assertEquals(countBefore + 1, experimentPackageConnectionService.getCountRecords());
    }

    @Test(groups = "unit")
    public void testAddExperimentToPackage() {

        experimentPackageService.create(experimentPackage, license);
        experimentDao.create(experiment);

        if (experimentPackage.getExperimentPackageConnections() != null) {
            int countBefore = experimentPackage.getExperimentPackageConnections().size();
            System.out.println(countBefore);
        }
        connection.setExperimentPackage(experimentPackage);
        connection.setExperiment(experiment);
        experimentPackageConnectionService.addExperimentToPackage(experiment, experimentPackage);

        experiment = experimentGenerator.generateExperiment(person);
        experimentDao.create(experiment);
        experimentPackageConnectionService.addExperimentToPackage(experiment, experimentPackage);

        if (experimentPackage.getExperimentPackageConnections() != null) {
            System.out.println(experimentPackage.getExperimentPackageConnections().size());
        }
    }

    @Test(groups = "unit", enabled = false)
    public void testRemoveExperimentFromPackage() {

        experimentPackageService.create(experimentPackage, license);
        experimentDao.create(experiment);

        int countBefore = experimentPackageService.listExperimentPackagesByGroup(researchGroup.getResearchGroupId()).size();
        connection.setExperimentPackage(experimentPackage);
        connection.setExperiment(experiment);
        experimentPackageConnectionService.addExperimentToPackage(experiment, experimentPackage);

        experiment = experimentGenerator.generateExperiment(person);
        experimentDao.create(experiment);
        experimentPackageConnectionService.addExperimentToPackage(experiment, experimentPackage);

        assertEquals(countBefore + 2, experimentPackageService.listExperimentPackagesByGroup(researchGroup.getResearchGroupId()).size());

        experimentPackageConnectionService.removeExperimentFromPackage(experiment, experimentPackage);
        assertEquals(countBefore + 1, experimentPackageService.listExperimentPackagesByGroup(researchGroup.getResearchGroupId()).size());
    }

}
