
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackageConnection;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Yaseen Ali
 */
public class ExperimentPackageConnectionDaoTest extends AbstractDataAccessTest{
    
    @Autowired
    protected ExperimentPackageConnectionDao experimentPackageConnectiondao;

    @Autowired
    private ExperimentPackageDao experimentPackageDao;
    @Autowired
    private ExperimentDao<Experiment, Integer> experimentDao;
    
    Experiment experiment;
    ExperimentPackage experimentPackage;
    ExperimentPackageConnection experimentPackageConnection;
    
    @Before
    public void setUp() {
        int id;
	experiment = new Experiment();
        experiment.setEnvironmentNote("junit@test-note");
        experiment.setExperimentId(-11);
        experiment.setPrivateExperiment(true);
        experiment.setTemperature(20);
        experiment.setWeather(null);
	id = experimentDao.create(experiment);
	experiment = experimentDao.read(id);

        experimentPackage = new ExperimentPackage();
        experimentPackage.setExperimentPackageId(-12);
        experimentPackage.setName("junit@test-name");
	id = experimentPackageDao.create(experimentPackage);
	experimentPackage = experimentPackageDao.read(id);

        experimentPackageConnection = new ExperimentPackageConnection();
        experimentPackageConnection.setExperiment(experiment);
        experimentPackageConnection.setExperimentPackage(experimentPackage);
        experimentPackageConnection.setExperimentPackgageConnectionId(-55);
    }
    
    @Test
    public void testCreateExperimentPackageConnection() {
        int id = (Integer) experimentPackageConnectiondao.create(experimentPackageConnection);
        assertNotNull(experimentPackageConnectiondao.read(id));  
    }
    
    @Test
    public void testDeleteExperimentPackageConnection(){
	int id = (Integer) experimentPackageConnectiondao.create(experimentPackageConnection);
        experimentPackageConnection = experimentPackageConnectiondao.read(id);

        experimentPackageConnectiondao.delete(experimentPackageConnection);
        assertNull(experimentPackageConnectiondao.read(experimentPackageConnection.getExperimentPackgageConnectionId()));
    }
}