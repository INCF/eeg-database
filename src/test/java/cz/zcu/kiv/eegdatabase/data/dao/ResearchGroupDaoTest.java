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
 *   ResearchGroupDaoTest.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static net.sf.ezmorph.test.ArrayAssertions.assertEquals;

/**
 * User: Tomas Pokryvka
 * Date: 28.4.13
 */
public class ResearchGroupDaoTest extends AbstractDataAccessTest {

  @Autowired
  protected ResearchGroupDao researchGroupDao;
  @Autowired
  protected PersonDao personDao;

  protected ResearchGroup researchGroup;
  protected Person person;

  @Before
  public void setUp() throws Exception {
    person = personDao.getPerson("testaccountforeeg@seznam.cz"); // ROLE_ADMIN

    researchGroup = new ResearchGroup();
    researchGroup.setDescription("test-description");
    researchGroup.setTitle("test-title");
    researchGroup.setPerson(person);
  }

  @Test
  @Transactional
  public void testCreateResearchGroup(){
    int count = researchGroupDao.getCountRecords();
    researchGroupDao.create(researchGroup);
    assertEquals(count + 1, researchGroupDao.getCountRecords());
  }

  @Test
  @Transactional
  public void testGetResearchGroupTitle() throws Exception {
    researchGroupDao.create(researchGroup);
    assertEquals("test-title", researchGroupDao.getResearchGroupTitle(researchGroup.getResearchGroupId()));
  }


  @Test
  @Transactional
  public void testGetListOfGroupMembers() throws Exception {
    researchGroupDao.create(researchGroup);
    assertEquals(0, researchGroupDao.getListOfGroupMembers(researchGroup.getResearchGroupId()).size());
  }
}
