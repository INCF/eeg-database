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

import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.dao.LicenseDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageConnectionService;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.Assert.assertEquals;

/**
 * Created by stebjan on 20.8.2014.
 */
@Transactional
public class ExperimentPackageServiceTest extends AbstractServicesTest{

    @Autowired
    private ExperimentPackageService experimentPackageService;

//    @Autowired
//    private ExperimentPackageConnectionService experimentPackageConnectionService;

    @Autowired
    private LicenseDao licenseDao;

    private ExperimentPackage experimentPackage;

    @Autowired
    private ResearchGroupDao researchGroupDao;

    @Autowired
    private PersonDao personDao;

    private ResearchGroup researchGroup;

    @Before
    public void setUp() {
        Person person = TestUtils.createPersonForTesting("test@test.com", Util.ROLE_ADMIN);

        personDao.create(person);

        researchGroup = new ResearchGroup();
        researchGroup.setDescription("test-description");
        researchGroup.setTitle("test-title");
        researchGroup.setPerson(person);
        researchGroup.setPaidAccount(true);
        researchGroupDao.create(researchGroup);

        experimentPackage = new ExperimentPackage();
        experimentPackage.setName("test name");
        experimentPackage.setResearchGroup(researchGroup);
    }

    @Test
    public void testCreateExperimentPackage() {
        License license = new License();
        license.setDescription("junit@test.description");
        license.setLicenseId(-231);
        license.setPrice(-1000f);
        license.setTitle("title");
        license.setLicenseType(LicenseType.OWNER);
        license.setResearchGroup(researchGroup);
        licenseDao.create(license);

        int countBefore = experimentPackageService.getCountRecords();
        experimentPackageService.create(experimentPackage, license);
        assertEquals(countBefore + 1, experimentPackageService.getCountRecords());
    }
}
