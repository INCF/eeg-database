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

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import org.testng.annotations.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;

import static org.testng.Assert.*;

public class LicenseDaoTest extends AbstractDataAccessTest {

    @Autowired
    private LicenseDao licenseDao;

    private License license;

    @BeforeMethod(groups = "unit")
    public void setUp() {
        license = new License();
        license.setDescription("junit@test.description");
        license.setLicenseId(-231);
        license.setTitle("title");
        license.setLicenseType(LicenseType.NON_COMMERCIAL);
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
        license.setLicenseType(LicenseType.COMMERCIAL);
        licenseDao.update(license);

        license = licenseDao.read(id);

        assertEquals(LicenseType.COMMERCIAL, license.getLicenseType());
    }

    @Test(groups = "unit")
    public void testGetLicenseByType() {
        int count = licenseDao.getCountRecords();
        
        licenseDao.create(license);
        License license2 = new License();
        license2.setLicenseId(-231);
        license2.setTitle("title");
        license2.setLicenseType(LicenseType.COMMERCIAL);
        licenseDao.create(license2);
        
        assertEquals(count + 2, licenseDao.getCountRecords());
        assertEquals(count + 1, licenseDao.getLicensesByType(LicenseType.COMMERCIAL).size());
    }

    @Test(groups = "unit")
    public void testDeleteLicense() {
        int id = licenseDao.create(license);
        license = licenseDao.read(id);

        licenseDao.delete(license);
        assertNull(licenseDao.read(license.getLicenseId()));
    }
}
