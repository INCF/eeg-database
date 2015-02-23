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
 *   ResearchGroupDaoTest.java, 2014/06/05 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.Util;

import org.testng.annotations.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.BeforeMethod;

import static org.testng.Assert.assertEquals;

@TransactionConfiguration(defaultRollback = true)
public class ResearchGroupDaoTest extends AbstractDataAccessTest {

    @Autowired
    protected ResearchGroupDao researchGroupDao;
    @Autowired
    protected PersonDao personDao;

    protected ResearchGroup researchGroup;
    protected Person person;

    @BeforeMethod(groups = "unit")
    public void setUp() {
        person = TestUtils.createPersonForTesting("test@test.com", Util.ROLE_ADMIN);

        personDao.create(person);


        researchGroup = new ResearchGroup();
        researchGroup.setDescription("test-description");
        researchGroup.setTitle("test-title");
        researchGroup.setPerson(person);
    }

    @Test(groups = "unit")
    public void testCreateResearchGroup() {
        int count = researchGroupDao.getCountForList();
        researchGroupDao.create(researchGroup);
        assertEquals(count + 1, researchGroupDao.getCountRecords());
        assertEquals("test-title", researchGroupDao.getResearchGroupTitle(researchGroup.getResearchGroupId()));
        assertEquals("test@test.com", researchGroupDao.read(researchGroup.getResearchGroupId()).getPerson().getUsername());

    }

    @Test(groups = "unit")
    public void testGetGroupsWhereOwner() {
        int count = researchGroupDao.getResearchGroupsWhereOwner(person).size();
        researchGroupDao.create(researchGroup);
        ResearchGroup group2 = new ResearchGroup();
        group2.setDescription("desc");
        group2.setTitle("test");
        group2.setPerson(person);
        researchGroupDao.create(group2);
        assertEquals(count + 2, researchGroupDao.getResearchGroupsWhereOwner(person).size());

    }

    @Test(groups = "unit")
    public void testGetMembership() {
        researchGroupDao.create(researchGroup);
        assertEquals(0, researchGroupDao.getResearchGroupsWhereMember(person).size());

    }
    @Test(groups = "unit")
    public void testEditResearchGroup() {
        int count = researchGroupDao.getCountRecords();
        int id = researchGroupDao.create(researchGroup);
        assertEquals(count + 1, researchGroupDao.getCountRecords());
        researchGroup.setDescription("new desc");
        researchGroupDao.update(researchGroup);
        assertEquals(count + 1, researchGroupDao.getCountRecords());

        assertEquals("new desc" , researchGroupDao.read(id).getDescription());
    }

}
