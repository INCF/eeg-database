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
 *   PersonalLicenseDaoTest.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonalLicense;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Yaseen Ali
 */
public class PersonalLicenseDaoTest extends AbstractDataAccessTest {

    @Autowired
    protected PersonalLicenseDao personalLicenseDao;
    @Autowired
    private LicenseDao licenseDao;
    @Autowired
    private PersonDao personDao;
    
    License license;
    Person person;
    PersonalLicense personalLicense;
    
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
       
       person = new Person();
       person.setConfirmed(true);
       person.setGivenname("junit@test-givenname");
       person.setPersonId(-11);
       id = personDao.create(person);
       person = personDao.read(id);
       
       personalLicense = new PersonalLicense();
       personalLicense.setLicense(license);
       personalLicense.setPerson(person);
      // personalLicense.setPersonalLicenseId(personalLicenseId);
    }

    @Test
    public void testCreatePersonalLicense() {
       int id = (Integer) personalLicenseDao.create(personalLicense);
       assertNotNull(personalLicenseDao.read(id));
    }
    
    @Test
    public void testDeletePersonalLicense(){
	int id = (Integer) personalLicenseDao.create(personalLicense);
	personalLicense = personalLicenseDao.read(id);
        personalLicenseDao.delete(personalLicense);
        assertNull(personalLicenseDao.read(personalLicense.getPersonalLicenseId()));
    }
}
