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
 *   ScenarioServicePerformanceTest.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.perf.persistentLayer;
import cz.zcu.kiv.eegdatabase.data.dao.ScenarioDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: Kabourek
 * Date: 17.5.11
 * Time: 20:02
 * To change this template use File | Settings | File Templates.
 * Identificator of test /PPT_S_1_WorWitSce_L/. Contains document Testovaci scenare.docx.
 */
public class ScenarioServicePerformanceTest extends PerformanceTest{

    @Autowired
    ScenarioDao scenarioDao;

    private Scenario scenario;

    /**
     * Method test insert file to experiment.
     * Identificator of test /PPT_S_2_AddSce_F/. Contains document Testovaci scenare.docx.
     */
    //@Test
    public void createScenarioTest(){
      scenario = new Scenario();
        scenario.setDescription("testovaci scenar");
//        scenario.setExperiments();
//        scenario.setMimetype();
//        scenario.setPerson();
//        scenario.setResearchGroup();
      scenarioDao.create(scenario);
    }
    /**
     * Method test insert file to experiment.
     * Identificator of test /PPT_S_3_EdiSce_F/. Contains document Testovaci scenare.docx.
     */
    //@Test
    public void editScenarioTest(){
      scenario.setDescription("testovaci scenar");
//        scenario.setExperiments();
//        scenario.setMimetype();
//        scenario.setPerson();
//        scenario.setResearchGroup();
      scenarioDao.update(scenario);

    }
}
