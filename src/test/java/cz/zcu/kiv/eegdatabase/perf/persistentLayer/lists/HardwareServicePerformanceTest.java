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
 *   HardwareServicePerformanceTest.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.perf.persistentLayer.lists;

import cz.zcu.kiv.eegdatabase.data.dao.HardwareDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.perf.persistentLayer.PerformanceTest;
import org.junit.Test;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Richard Kocman,Kabourek
 * Date: 23.5.11
 * Time: 14:03
 * To change this template use File | Settings | File Templates.
 * Identificator of test PPT_LHW_2_WorWitHw_L. Contains document Testovaci scenare.docx.
 */
public class HardwareServicePerformanceTest extends PerformanceTest {



     /**
     * Constant for atribute of test data.
     */
    public static final String HARDWARE_DESCRIPTION = "popis testu";
    public static final String HARDWARE_TITLE = "Hlavicka";
    public static final String HARDWARE_TYPE = "Type";

    private Hardware hardware;
    private HardwareDao hardwareDao;


 /**
   * Method test create hardware for next test.
   *
   */

    public void createTestHardware(){
        hardware = new Hardware();
        hardware.setDescription(HARDWARE_DESCRIPTION);
        hardware.setTitle(HARDWARE_TITLE);
        hardware.setType(HARDWARE_TYPE);
        hardwareDao.create(hardware);
    }

/**
 * Method test create Hardware
 * Identificator of test / PPT_LHW_3_AddHw_F /. Contains document Testovaci scenare.docx.
 */
    @Test
    public void testCreateHardwareTest(){
       int countRecord = hardwareDao.getCountRecords();
       createTestHardware();
       assertEquals(hardwareDao.getCountRecords()-1, countRecord);
    }

/**
 * Method test edit Hardware
 * Identificator of test / PPT_LHW_4_EdiWitHw_F /. Contains document Testovaci scenare.docx.
 */
    @Test
    public void testEditHardwareTest(){
        List<Hardware> listRecords;
        createTestHardware();

        hardware.setDescription(HARDWARE_DESCRIPTION+"EDITOAVANY");
        hardwareDao.update(hardware);

        listRecords=hardwareDao.getAllRecords();
        assertEquals(hardwareDao.read(hardware.getHardwareId()).getDescription(), hardware.getDescription());
        hardwareDao.delete(hardware);
    }
/**
 * Method test delete Hardware.
 * Identificator of test / PPT_LHW_4_DelWitHw_F /. Contains document Testovaci scenare.docx.
 */
    @Test
    public void testDeleteHardwareTest(){
        createTestHardware();

        int countRecord = hardwareDao.getCountRecords();

        hardwareDao.delete(hardware);

        assertEquals(hardwareDao.getCountRecords()+1, countRecord);
    }


     public void setHardwareDao(HardwareDao hardwareDao) {
        this.hardwareDao = hardwareDao;
    }
}
