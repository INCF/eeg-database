
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackageLicense;
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
 * @author Yaseen ALi
 */
public class ExperimentPackageDaoTest extends AbstractDataAccessTest{
    
    @Autowired
    protected ExperimentPackageDao experimentPackageDao;
    @Autowired
    protected ExperimentPackageLicenseDao experimentPackageLicenseDao;

    ExperimentPackage experimentPackage;
    ExperimentPackageLicense experimentPackageLicense;  
    
    @Before
    public void setUp() {
        experimentPackageLicense = new ExperimentPackageLicense();
        experimentPackageLicense.setExperimentPackageLicenseId(-1);
       
        experimentPackage = new ExperimentPackage();
        experimentPackage.setExperimentPackageId(-1);
        experimentPackage.setName("junit@test.name");
        experimentPackage.setExperimentPackageConnections(null);
        experimentPackage.setExperimentPackageLicenses((Set<ExperimentPackageLicense>) experimentPackageLicense);
        experimentPackage.setResearchGroup(null);
    }

    @Test
    public void testCreateExperimentPackage() {
        experimentPackageDao.create(experimentPackage);
        assertNotNull(experimentPackageDao.read(experimentPackage.getExperimentPackageId()));
    }
    
    @Test
    public void testCreateExperimentPackageLicense(){
        int startCount= experimentPackageLicenseDao.getAllRecords().size();
        experimentPackageLicense.setExperimentPackage(experimentPackage);
        Integer id= (Integer) experimentPackageLicenseDao.create(experimentPackageLicense);
        assertNotNull(id);
        assertEquals(startCount + 1, experimentPackageLicenseDao.getAllRecords().size());
        
    }
    
    @Test
    public void testDeleteExperimentPackage(){
        experimentPackageDao.delete(experimentPackage);
        assertNull(experimentPackage);
    }
}