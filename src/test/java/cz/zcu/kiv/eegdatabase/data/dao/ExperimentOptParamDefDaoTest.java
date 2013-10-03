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
 *   ExperimentOptParamDefDaoTest.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * User: Tomas Pokryvka
 * Date: 26.4.13
 */
public class ExperimentOptParamDefDaoTest extends AbstractDataAccessTest {

  @Autowired
  protected ExperimentOptParamDefDao experimentOptParamDefDao;
  @Autowired
  protected ResearchGroupDao researchGroupDao;


  protected ExperimentOptParamDef experimentOptParamDef;

  @Before
  public void setUp() throws Exception {
    experimentOptParamDef = new ExperimentOptParamDef();
    experimentOptParamDef.setParamDataType("test-paramDataType");
    experimentOptParamDef.setParamName("test-paramName");
  }

  @Test
  @Transactional
  public void createExperimentOptParamDef(){
    int count = experimentOptParamDefDao.getCountRecords();
    experimentOptParamDefDao.create(experimentOptParamDef);
    assertEquals(count + 1, experimentOptParamDefDao.getCountRecords());
  }

  @Test
  @Transactional
  public void testCreateDefaultRecord() throws Exception {
    assertFalse(experimentOptParamDefDao.isDefault(experimentOptParamDef.getExperimentOptParamDefId()));
    experimentOptParamDefDao.createDefaultRecord(experimentOptParamDef);
    assertTrue(experimentOptParamDefDao.isDefault(experimentOptParamDef.getExperimentOptParamDefId()));
  }

  @Test
  public void testGetDefaultRecords() throws Exception {

  }


  @Test
  public void testDeleteGroupRel() throws Exception {

  }

  @Test
  public void testGetGroupRel() throws Exception {

  }

  @Test
  @Transactional
  public void testCreateGroupRel() throws Exception {
    ResearchGroup researchGroup = researchGroupDao.getAllRecords().get(0);
    int old = experimentOptParamDef.getResearchGroups().size();
    experimentOptParamDefDao.createGroupRel(experimentOptParamDef, researchGroup);
    assertEquals(old + 1, experimentOptParamDef.getResearchGroups().size());
  }
}
