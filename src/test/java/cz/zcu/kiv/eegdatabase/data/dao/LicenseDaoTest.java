
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
       license.setPrice(-1000);
       license.setTitle("junit@test.title");
       license.setLicenseType(LicenseType.PRIVATE);
       
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