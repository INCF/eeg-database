
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackageLicense;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import java.util.Set;
import org.jfree.ui.about.Licences;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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
    @Autowired
    protected ExperimentPackageLicenseDao experimentPackageLicenseDao;
    
    ExperimentPackageLicense experimentPackageLicense;
    License license;
    
    @Before
    public void setUp() {
       experimentPackageLicense = new ExperimentPackageLicense();
       experimentPackageLicense.setExperimentPackageLicenseId(-1);
       
       license = new License();
       license.setDescription("junit@test.description");
     //license.setLicenseId(-111);
       license.setPrice(-1000);
       license.setTitle("junit@test.title");
       license.setExperimentPackageLicenses((Set<ExperimentPackageLicense>) experimentPackageLicense);
       license.setLicenseType(LicenseType.PRIVATE);
       license.setPersonalLicenses(null);
       
    }

    @Test
    public void testCreateExperimentPackageLicense() {
        experimentPackageLicense.setLicense(license);
        int id= (Integer) experimentPackageLicenseDao.create(experimentPackageLicense);
        assertNotNull(experimentPackageLicenseDao.read(id));
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
        licenseDao.delete(license);
        assertNull(licenseDao.read(license.getLicenseId()));
    }
}