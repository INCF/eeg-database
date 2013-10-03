
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