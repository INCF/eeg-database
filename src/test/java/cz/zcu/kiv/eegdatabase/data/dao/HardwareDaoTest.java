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
 *   HardwareDaoTest.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

/**
 * User: Tomas Pokryvka
 * Date: 26.4.13
 */
public class HardwareDaoTest extends AbstractDataAccessTest {


  @Autowired
  protected HardwareDao hardwareDao;

  protected Hardware hardware;

  @Before
  public void setUp() throws Exception {
    hardware = new Hardware();
    hardware.setTitle("New HW");
    hardware.setDescription("This is new testing HW");
    hardware.setType("HW type");
    hardware.setDefaultNumber(0);
  }



  @Test
  @Transactional
  public void testCreateHardware() {
    int hardwareCountBefore  = hardwareDao.getAllRecords().size();
    int hardwareID = hardwareDao.create(hardware);
    assertEquals(hardwareCountBefore + 1, hardwareDao.getAllRecords().size());
    assertEquals(hardwareID, hardware.getHardwareId());
  }


  @Test
  @Transactional
  public void testCreateDefaultRecord() throws Exception {
    int expectedValue = hardwareDao.getDefaultRecords().size();
    hardwareDao.createDefaultRecord(hardware);
    assertEquals(expectedValue + 1, hardwareDao.getDefaultRecords().size());
  }
}
