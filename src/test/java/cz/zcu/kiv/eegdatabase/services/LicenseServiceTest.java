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
 * LicenseServiceTest.java, 2014/08/25 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.services;

import java.math.BigDecimal;

import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseService;
import cz.zcu.kiv.eegdatabase.wui.core.license.PersonalLicenseService;

import org.testng.annotations.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeMethod;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by stebjan on 25.8.2014.
 */
public class LicenseServiceTest extends AbstractServicesTest {

    @Autowired
    private LicenseService licenseService;

    @Autowired
    private PersonalLicenseService personalLicenseService;

    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private PersonDao personDao;
    private ResearchGroup researchGroup;
    private Person person;

    private License license;

    @BeforeMethod(groups = "unit")
    public void setUp() {

        person = TestUtils.createPersonForTesting("test@test.com", Util.ROLE_ADMIN);

        personDao.create(person);
        license = new License();
        license.setDescription("junit@test.description");
        license.setLicenseId(-231);
        license.setPrice(BigDecimal.valueOf(-1000f));
        license.setTitle("title");
        license.setLicenseType(LicenseType.OWNER);
    }

    @Test(groups = "unit")
    public void testCreateLicense() {
        int count = licenseService.getCountRecords();
        int id = licenseService.create(license);
        assertNotNull(licenseService.read(id));
        assertEquals(count + 1, licenseService.getCountRecords());
    }

    @Test(groups = "unit")
    public void testGetPublicLicense() {
        int count = licenseService.getCountRecords();
        int id = licenseService.create(license);
        assertNotNull(licenseService.read(id));


        License newLicense = new License();
        newLicense.setDescription("desc");
        newLicense.setPrice(BigDecimal.valueOf(1000f));
        newLicense.setTitle("test-title");
        newLicense.setLicenseType(LicenseType.OPEN_DOMAIN);
        licenseService.create(newLicense);

        assertEquals("test-title", licenseService.getPublicLicense().getTitle());
        assertEquals(count + 2, licenseService.getCountRecords());

    }

    @Test(groups = "unit")
    public void testGetLicensesForGroup() {
        int count = licenseService.getCountRecords();
        int id = licenseService.create(license);
        assertNotNull(licenseService.read(id));

        researchGroup = new ResearchGroup();
        researchGroup.setDescription("test-description");
        researchGroup.setTitle("test-title");
        researchGroup.setPerson(person);
        researchGroupDao.create(researchGroup);

        int groupCount = licenseService.getLicensesForGroup(researchGroup, LicenseType.OPEN_DOMAIN).size();

        License newLicense = new License();
        newLicense.setDescription("desc");
        newLicense.setPrice(BigDecimal.valueOf(1000f));
        newLicense.setTitle("test-title");
        newLicense.setLicenseType(LicenseType.OPEN_DOMAIN);
        newLicense.setResearchGroup(researchGroup);
        licenseService.create(newLicense);

        assertEquals(groupCount + 1, licenseService.getLicensesForGroup(researchGroup, LicenseType.OPEN_DOMAIN).size());
        assertEquals(count + 2, licenseService.getCountRecords());

    }

}
