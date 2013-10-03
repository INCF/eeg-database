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
 *   ExperimentPackageLicenseDaoTest.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackageLicense;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Yaseen Ali
 */
public class ExperimentPackageLicenseDaoTest extends AbstractDataAccessTest {
    
    @Autowired
    protected ExperimentPackageLicenseDao experimentPackageLicenseDao;
    @Autowired
    private LicenseDao licenseDao;
    @Autowired
    private ExperimentPackageDao experimentPackageDao;
    
    ExperimentPackageLicense experimentPackageLicense;
    License license;
    ExperimentPackage experimentPackage;
        
    @Before
    public void setUp() {
        int id;
       license = new License();
       license.setDescription("junit@test.description");
       license.setLicenseId(-111);
       license.setPrice(-1000f);
       license.setTitle("junit@test.title");
       license.setLicenseType(LicenseType.OWNER);
       id = licenseDao.create(license);
       license = licenseDao.read(id);
       
       experimentPackage = new ExperimentPackage();
       experimentPackage.setExperimentPackageId(-1);
       experimentPackage.setName("junit@test.name");
       experimentPackage.setResearchGroup(null);
       id = experimentPackageDao.create(experimentPackage);
       experimentPackage = experimentPackageDao.read(id);
       
       experimentPackageLicense = new ExperimentPackageLicense();
       experimentPackageLicense.setExperimentPackage(experimentPackage);
       experimentPackageLicense.setLicense(license);
       experimentPackageLicense.setExperimentPackageLicenseId(-11);
    }

    @Test
    public void testCreateExperimentPackageLicense() {
     int id = (Integer) experimentPackageLicenseDao.create(experimentPackageLicense);
     assertNotNull(experimentPackageLicenseDao.read(id));
    }
    
    @Test
    public void testDeleteExperimentPackageLicense(){
	int id = (Integer) experimentPackageLicenseDao.create(experimentPackageLicense);
	experimentPackageLicense = experimentPackageLicenseDao.read(id);

        experimentPackageLicenseDao.delete(experimentPackageLicense);
        assertNull(experimentPackageLicenseDao.read(experimentPackageLicense.getExperimentPackageLicenseId()));
    }
}
