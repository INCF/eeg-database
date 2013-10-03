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
 *   FileMetadataParamDefDaoTest.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamDef;
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
public class FileMetadataParamDefDaoTest extends AbstractDataAccessTest {

  @Autowired
  protected FileMetadataParamDefDao fileMetadataParamDefDao;
  @Autowired
  protected ResearchGroupDao researchGroupDao;

  protected FileMetadataParamDef fileMetadataParamDef;

  @Before
  public void setUp() throws Exception {
    fileMetadataParamDef = new FileMetadataParamDef();
    fileMetadataParamDef.setParamDataType("test-paramDataType");
    fileMetadataParamDef.setParamName("test-paramName");
  }

  @Test
  @Transactional
  public void testCreateFileMetadataParamDef(){
    int count = fileMetadataParamDefDao.getCountRecords();
    fileMetadataParamDefDao.create(fileMetadataParamDef);
    assertEquals(count + 1, fileMetadataParamDefDao.getCountRecords()) ;
  }

  @Test
  @Transactional
  public void testCreateDefaultRecord() throws Exception {
    assertFalse(fileMetadataParamDefDao.isDefault(fileMetadataParamDef.getFileMetadataParamDefId()));
    fileMetadataParamDefDao.createDefaultRecord(fileMetadataParamDef);
    assertTrue(fileMetadataParamDefDao.isDefault(fileMetadataParamDef.getFileMetadataParamDefId()));
  }

  @Test
  @Transactional
  public void testCreateGroupRel() throws Exception {
    int old = fileMetadataParamDef.getResearchGroups().size();
    ResearchGroup researchGroup = researchGroupDao.getAllRecords().get(0);
    fileMetadataParamDefDao.createGroupRel(fileMetadataParamDef, researchGroup);
    assertEquals(old + 1, fileMetadataParamDef.getResearchGroups().size());
  }
}
