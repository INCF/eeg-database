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
 *   ExperimentPackageConnectionDaoTest.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
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
    private ExperimentDao experimentDao;
    
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
