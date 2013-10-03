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
 *   LicenseDaoTest.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackageLicense;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Yaseen ALi
 */
public class LicenseDaoTest extends AbstractDataAccessTest{
    
    @Autowired
    protected LicenseDao licenseDao;
    
    License license;
    
    @Before
    public void setUp() {       
       license = new License();
       license.setDescription("junit@test.description");
       license.setLicenseId(-231);
       license.setPrice(-1000f);
       license.setTitle("junit@test.title");
       license.setLicenseType(LicenseType.OWNER);
       
    }

    @Test
    public void testCreateLicense(){
        int id= (Integer)licenseDao.create(license);
        assertNotNull(licenseDao.read(id));
    }
    
    @Test
    public void testChangeLicenseType(){
        int id= (Integer) licenseDao.create(license);
        assertNotNull(licenseDao.read(id));
        
        license = (License) licenseDao.read(id);
        license.setLicenseType(LicenseType.ACADEMIC);
        licenseDao.update(license);
        
        license = (License) licenseDao.read(id);
        
        assertEquals(LicenseType.ACADEMIC, license.getLicenseType());
    }
    
    @Test
    public void testDeleteLicense(){
        int id= (Integer) licenseDao.create(license);
        license = (License) licenseDao.read(id);

        licenseDao.delete(license);
        assertNull(licenseDao.read(license.getLicenseId()));
    }
}
