
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackageLicense;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonalLicense;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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
    
    License license;
    Person person;
    PersonalLicense personalLicense;
    
    @Before
    public void setUp() {
       license = new License();
       license.setDescription("junit@test.description");
       license.setLicenseId(-111);
       license.setPrice(-1000);
       license.setTitle("junit@test.title");
       license.setLicenseType(LicenseType.PRIVATE);
       license.setPersonalLicenses(null);  
       
       person = new Person();
       person.setConfirmed(true);
       person.setGivenname("junit@test-givenname");
       person.setPersonId(-11);
       
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
        personalLicenseDao.delete(personalLicense);
        assertNull(personalLicenseDao.read(personalLicense.getPersonalLicenseId()));
    }
}