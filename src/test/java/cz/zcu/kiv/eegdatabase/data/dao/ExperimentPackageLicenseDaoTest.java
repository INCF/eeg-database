
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