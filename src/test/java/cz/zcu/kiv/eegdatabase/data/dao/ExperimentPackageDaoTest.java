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
 *   ExperimentPackageDaoTest.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackageLicense;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Yaseen ALi
 */
public class ExperimentPackageDaoTest extends AbstractDataAccessTest{
    
    @Autowired
    protected ExperimentPackageDao experimentPackageDao;
    @Autowired
    protected ExperimentPackageLicenseDao experimentPackageLicenseDao;

    ExperimentPackage experimentPackage;
    
    @Before
    public void setUp() {       
        experimentPackage = new ExperimentPackage();
        experimentPackage.setExperimentPackageId(-1);
        experimentPackage.setName("junit@test.name");
        experimentPackage.setResearchGroup(null);
    }

    @Test
    public void testCreateExperimentPackage() {
        int id = (Integer) experimentPackageDao.create(experimentPackage);
        assertNotNull(experimentPackageDao.read(id));
    }
       
    @Test
    public void testDeleteExperimentPackage(){
        int id = (Integer) experimentPackageDao.create(experimentPackage);
        experimentPackage = experimentPackageDao.read(id);
        experimentPackageDao.delete(experimentPackage);
    }
}
