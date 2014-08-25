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

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.core.license.LicenseService;
import cz.zcu.kiv.eegdatabase.wui.core.license.PersonalLicenseService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by stebjan on 25.8.2014.
 */
@Transactional
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

    private License license;

    @Before
    public void setUp() {
        license = new License();
        license.setDescription("junit@test.description");
        license.setLicenseId(-231);
        license.setPrice(-1000f);
        license.setTitle("title");
        license.setLicenseType(LicenseType.OWNER);
    }

    @Test
    public void testCreateLicense() {
        int count = licenseService.getCountRecords();
        int id = licenseService.create(license);
        assertNotNull(licenseService.read(id));
        assertEquals(count + 1, licenseService.getCountRecords());
    }

}
