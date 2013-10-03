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
 *   ExperimentOptParamDefPerformanceTest.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.perf.persistentLayer.lists;

import cz.zcu.kiv.eegdatabase.data.dao.ExperimentOptParamDefDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDef;
import cz.zcu.kiv.eegdatabase.perf.persistentLayer.PerformanceTest;
import org.junit.Test;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Richard Kocman,Kabourek
 * Date: 24.5.11
 * Time: 13:59
 * To change this template use File | Settings | File Templates.
 * Identificator of test  /PPT_LOPP_6_WorWitOpp_L/. Contains document Testovaci scenare.docx.
 */
public class ExperimentOptParamDefPerformanceTest extends PerformanceTest {

    /**
     * Constant for atribute of test data.
     */
    public static final String EXPERIMENT_OPT_PARAM_DEF_NAME = "testovaci parametr";
    public static final String EXPERIMENT_OPT_PARAM_DEF_DATA_TYPE = "testovaci datatype";


    private ExperimentOptParamDef experimentOptParamDef;
    private ExperimentOptParamDefDao experimentOptParamDefDao;
/**
* Method test create experimentOptParamDef for next test.
*
*/

    public void createTestExperimentOptParamDef(){
        experimentOptParamDef = new ExperimentOptParamDef();
        experimentOptParamDef.setParamName(EXPERIMENT_OPT_PARAM_DEF_NAME);
        experimentOptParamDef.setParamDataType(EXPERIMENT_OPT_PARAM_DEF_DATA_TYPE);

    }

/**
 * Method test create Experiment Opt.
 * Identificator of test /PPT_LOPP_7_AddOpp_F/. Contains document Testovaci scenare.docx.
 */
    @Test
    public void testCreateExperimentOptParamDef(){
       int countRecord = experimentOptParamDefDao.getCountRecords();
       createTestExperimentOptParamDef();
       experimentOptParamDefDao.create(experimentOptParamDef);
         List<ExperimentOptParamDef> list = experimentOptParamDefDao.getItemsForList();
         System.out.println("list je ----"+list.size());
       assertEquals(experimentOptParamDefDao.getCountRecords()-1, countRecord);
        experimentOptParamDefDao.delete(experimentOptParamDef);
    }

/**
 * Method test edit Experiment Opt.
 * Identificator of test / PPT_LOPP_8_EditOpp_F /. Contains document Testovaci scenare.docx.
 */
    @Test
    public void testEditExperimentOptParamDef(){
        List<ExperimentOptParamDef> listRecords;
        createTestExperimentOptParamDef();
        experimentOptParamDef.setParamName(EXPERIMENT_OPT_PARAM_DEF_NAME+"EDITOVANO");
        experimentOptParamDefDao.update(experimentOptParamDef);
        listRecords=experimentOptParamDefDao.getAllRecords();
        //assertEquals(listRecords.get(listRecords.size()-1).getParamName(), experimentOptParamDef.getParamName());

    }
/**
 * Method test delete Experiment Opt.
 * Identificator of test /PPT_LOPP_9_DelOpp_F/. Contains document Testovaci scenare.docx.
 */
    @Test
    public void testDeleteExperimentOptParamDef(){
        int countRecord = experimentOptParamDefDao.getCountRecords();
        createTestExperimentOptParamDef();
        experimentOptParamDefDao.create(experimentOptParamDef);
        experimentOptParamDefDao.delete(experimentOptParamDef);
        assertEquals(experimentOptParamDefDao.getCountRecords(), countRecord);
    }

     /**
     * Setter for DAO object.
     */

     public void setExperimentOptParamDefDao(ExperimentOptParamDefDao experimentOptParamDefDao) {
        this.experimentOptParamDefDao = experimentOptParamDefDao;
    }

}
