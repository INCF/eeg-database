
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackageLicense;
import cz.zcu.kiv.eegdatabase.data.pojo.License;
import cz.zcu.kiv.eegdatabase.data.pojo.LicenseType;
import java.io.Serializable;
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
public class ExperimentPackageLicenseDaoTest extends AbstractDataAccessTest {
    
    @Autowired
    protected ExperimentPackageLicenseDao experimentPackageLicenseDao;
    
    ExperimentPackageLicense experimentPackageLicense;
    License license;
    ExperimentPackage experimentPackage;
        
    @Before
    public void setUp() {
        
       license = new License();
       license.setDescription("junit@test.description");
       license.setLicenseId(-111);
       license.setPrice(-1000);
       license.setTitle("junit@test.title");
       license.setExperimentPackageLicenses((Set<ExperimentPackageLicense>) experimentPackageLicense);
       license.setLicenseType(LicenseType.PRIVATE);
       license.setPersonalLicenses(null); 
       
       experimentPackage = new ExperimentPackage();
       experimentPackage.setExperimentPackageId(-1);
       experimentPackage.setName("junit@test.name");
       experimentPackage.setExperimentPackageConnections(null);
       experimentPackage.setExperimentPackageLicenses((Set<ExperimentPackageLicense>) experimentPackageLicense);
       experimentPackage.setResearchGroup(null); 
       
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
        experimentPackageLicenseDao.delete(experimentPackageLicense);
        assertNull(experimentPackageLicenseDao.read(experimentPackageLicense.getExperimentPackageLicenseId()));
    }
}