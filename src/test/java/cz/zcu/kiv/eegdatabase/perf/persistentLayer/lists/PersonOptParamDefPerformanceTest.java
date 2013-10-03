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
 *   PersonOptParamDefPerformanceTest.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.perf.persistentLayer.lists;

import cz.zcu.kiv.eegdatabase.data.dao.PersonOptParamDefDao;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamDef;
import cz.zcu.kiv.eegdatabase.perf.persistentLayer.PerformanceTest;
import java.util.List;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Kabourek
 * Date: 23.5.11
 * Time: 13:58
 * To change this template use File | Settings | File Templates.
 * Identificator of test  /PPT_LOPP_6_WorWitOpp_L/. Contains document Testovaci scenare.docx.
 */
public class PersonOptParamDefPerformanceTest extends PerformanceTest {


    /**
     * Constant for atribute of test data.
     */

    public static final String PERSON_OPT_PARAM_DEF_NAME = "testovaci parametr";
    public static final String PERSON_OPT_PARAM_DEF_DATA_TYPE = "testovaci datatype";



    PersonOptParamDefDao personOptParamDefDao;

    private PersonOptParamDef personOptParamDef;


    /**
* Method test create article for next test.
*
*/

     public void createTestPersonOptParamDef(){
        personOptParamDef = new PersonOptParamDef();
        personOptParamDef.setParamName(PERSON_OPT_PARAM_DEF_NAME);
        personOptParamDef.setParamDataType(PERSON_OPT_PARAM_DEF_DATA_TYPE);
        personOptParamDefDao.create(personOptParamDef);
    }

/**
 * Method test create personOptParamDef
 * Identificator of test / PPT_LOPP_7_AddOpp_F /. Contains document Testovaci scenare.docx.
 */
    @Test
    public void testCreatePersonOptParamTest(){
        int countRecord =  personOptParamDefDao.getCountRecords();

        createTestPersonOptParamDef();

         assertEquals(personOptParamDefDao.getCountRecords()-1, countRecord);
    }

/**
 * Method test edit personOptParamDef
 * Identificator of test / PPT_LOPP_8_EdiOpp_F/. Contains document Testovaci scenare.docx.
 */
    @Test
    public void testEditPersonOptParamTest(){
        createTestPersonOptParamDef();
        List<PersonOptParamDef> listRecords;

        personOptParamDef.setParamName(PERSON_OPT_PARAM_DEF_NAME+"Editovany");
        personOptParamDefDao.update(personOptParamDef);

        listRecords=personOptParamDefDao.getAllRecords();
        assertEquals(personOptParamDefDao.read(personOptParamDef.getPersonOptParamDefId()).getParamName(), personOptParamDef.getParamName());
    }
/**
 * Method test delete personOptParamDef
 * Identificator of test / PPT_LOPP_9_DelOpp_F /. Contains document Testovaci scenare.docx.
 */
    @Test
    public void testDeletePersonOptParamTest(){
        createTestPersonOptParamDef();

        int countRecord =  personOptParamDefDao.getCountRecords();
        personOptParamDefDao.delete(personOptParamDef);

        assertEquals(personOptParamDefDao.getCountRecords()+1, countRecord);
    }

      /**
     * Setter for DAO object.
     */
    public void setPersonOptParamDefDao(PersonOptParamDefDao personOptParamDefDao) {
        this.personOptParamDefDao = personOptParamDefDao;
    }
}
