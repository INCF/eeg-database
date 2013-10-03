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
 *   PersonOptParamDefDaoTest.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.Assert.*;
import static junit.framework.Assert.assertEquals;

/**
 * User: Tomas Pokryvka
 * Date: 26.4.13
 */
public class PersonOptParamDefDaoTest extends AbstractDataAccessTest {

  @Autowired
  protected PersonOptParamDefDao personOptParamDefDao;

  @Autowired
  protected ResearchGroupDao researchGroupDao;

  protected PersonOptParamDef personOptParamDef;


  @Before
  public void setUp() throws Exception {
    personOptParamDef = new PersonOptParamDef();
    personOptParamDef.setParamDataType("test-paramDataType");
    personOptParamDef.setParamName("test-paramName");
  }

  @Test
  @Transactional
  public void testCreatePersonOptParamDef(){
    int count = personOptParamDefDao.getCountRecords();
    personOptParamDefDao.create(personOptParamDef);
    assertEquals(count + 1, personOptParamDefDao.getCountRecords());
  }

  @Test
  @Transactional
  public void testCreateGroupRel() throws Exception {
    ResearchGroup researchGroup = researchGroupDao.getAllRecords().get(0);
    int old = personOptParamDef.getResearchGroups().size();
    personOptParamDefDao.createGroupRel(personOptParamDef, researchGroup);
    assertEquals(old + 1, personOptParamDef.getResearchGroups().size());
  }

  @Test
  @Transactional
  public void testCreateDefaultRecord() throws Exception {
    assertFalse(personOptParamDefDao.isDefault(personOptParamDef.getPersonOptParamDefId()));
    personOptParamDefDao.createDefaultRecord(personOptParamDef);
    assertTrue(personOptParamDefDao.isDefault(personOptParamDef.getPersonOptParamDefId()));
  }
}
