
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