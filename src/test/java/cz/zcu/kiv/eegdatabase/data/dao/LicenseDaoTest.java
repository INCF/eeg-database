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
 *   LicenseDaoTest.java, 2014/07/07 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import java.math.BigDecimal;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.Util;

import org.junit.Before;
import org.testng.annotations.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeMethod;

import static org.junit.Assert.*;

public class LicenseDaoTest extends AbstractDataAccessTest {

    @Autowired
    private LicenseDao licenseDao;

    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private PersonDao personDao;

    private License license;

    @BeforeMethod(groups = "unit")
    public void setUp() {
        license = new License();
        license.setDescription("junit@test.description");
        license.setLicenseId(-231);
        license.setPrice(BigDecimal.valueOf(-1000f));
        license.setTitle("title");
        license.setLicenseType(LicenseType.OWNER);

    }

    @Test(groups = "unit")
    public void testCreateLicense() {
        int count = licenseDao.getCountRecords();
        int id = licenseDao.create(license);
        assertNotNull(licenseDao.read(id));
        assertEquals(count + 1, licenseDao.getCountRecords());
    }

    @Test(groups = "unit")
    public void testChangeLicenseType() {
        int id = licenseDao.create(license);
        assertNotNull(licenseDao.read(id));

        license = licenseDao.read(id);
        license.setLicenseType(LicenseType.ACADEMIC);
        licenseDao.update(license);

        license = licenseDao.read(id);

        assertEquals(LicenseType.ACADEMIC, license.getLicenseType());
    }

    @Test(groups = "unit")
    public void testGetLicenseType() {
        Person person = TestUtils.createPersonForTesting("test@test.com", Util.ROLE_ADMIN);
        int count = licenseDao.getCountRecords();
        personDao.create(person);

        ResearchGroup researchGroup = new ResearchGroup();
        researchGroup.setDescription("test-description");
        researchGroup.setTitle("test-title");
        researchGroup.setPerson(person);
        researchGroupDao.create(researchGroup);
        license.setResearchGroup(researchGroup);
        licenseDao.create(license);
        License license2 = new License();
        license2.setLicenseId(-231);
        license2.setPrice(BigDecimal.valueOf(-1000f));
        license2.setTitle("title");
        license2.setLicenseType(LicenseType.ACADEMIC);
        license2.setResearchGroup(researchGroup);
        licenseDao.create(license2);
        assertEquals(count + 2, licenseDao.getCountRecords());
        assertEquals(count + 1, licenseDao.getLicensesByType(researchGroup.getResearchGroupId(), LicenseType.ACADEMIC).size());


    }

    @Test(groups = "unit")
    public void testDeleteLicense() {
        int id = licenseDao.create(license);
        license = licenseDao.read(id);

        licenseDao.delete(license);
        assertNull(licenseDao.read(license.getLicenseId()));
    }
}
