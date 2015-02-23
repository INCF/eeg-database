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
 *   EducationLevelDaoTest.java, 2015/04/02 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * Created by stebjan on 4.2.2015.
 */
public class EducationLevelDaoTest extends AbstractDataAccessTest {

    @Autowired
    private EducationLevelDao educationLevelDao;
    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private PersonDao personDao;

    private EducationLevel educationLevel;
    private ResearchGroup researchGroup;

    @BeforeMethod(groups = "unit")
    public void setUp() throws Exception {
        Person person = TestUtils.createPersonForTesting("test@test.com", Util.ROLE_ADMIN);

        personDao.create(person);

        researchGroup = new ResearchGroup();
        researchGroup.setDescription("test-description");
        researchGroup.setTitle("test-title");
        researchGroup.setPerson(person);
        researchGroupDao.create(researchGroup);
        educationLevel = new EducationLevel();
        educationLevel.setTitle("New level");
        educationLevel.setDefaultNumber(0);
    }


    @Test(groups = "unit")
    public void testCreateEducationLevel() {
        int educationLevelCountBefore = educationLevelDao.getAllRecords().size();
        int educationLevelID = educationLevelDao.create(educationLevel);
        assertEquals(educationLevelCountBefore + 1, educationLevelDao.getAllRecords().size());
        assertEquals(educationLevelID, educationLevel.getEducationLevelId());
    }


    @Test(groups = "unit")
    public void testCreateDefaultRecord() throws Exception {
        int expectedValue = educationLevelDao.getDefaultRecords().size();
        educationLevelDao.createDefaultRecord(educationLevel);
        assertEquals(expectedValue + 1, educationLevelDao.getDefaultRecords().size());
    }

    @Test(groups = "unit")
    public void testCreateGroupEducationLevel() {
        int educationLevelCountBefore = educationLevelDao.getAllRecords().size();
        int educationLevelGroupBefore = educationLevelDao.getRecordsByGroup(researchGroup.getResearchGroupId()).size();
        educationLevelDao.createGroupRel(educationLevel, researchGroup);
        educationLevelDao.create(educationLevel);
        assertEquals(educationLevelCountBefore + 1, educationLevelDao.getAllRecords().size());

        List<EducationLevel> list = educationLevelDao.getRecordsByGroup(researchGroup.getResearchGroupId());
        assertEquals(educationLevelGroupBefore + 1, list.size());
    }

    @Test(groups = "unit")
    public void testGetEducationLevels() {
        int countBefore = educationLevelDao.getEducationLevels("New level").size();
        int educationLevelID = educationLevelDao.create(educationLevel);
        assertEquals(educationLevelID, educationLevel.getEducationLevelId());
        assertEquals(countBefore + 1, educationLevelDao.getEducationLevels("New level").size());

        EducationLevel educationLevel = new EducationLevel();
        educationLevel.setTitle("New level");
        educationLevel.setDefaultNumber(0);
        educationLevelDao.create(educationLevel);
        assertEquals(countBefore + 2, educationLevelDao.getEducationLevels("New level").size());
        assertEquals(0, educationLevelDao.getEducationLevels("xxx").size());
    }

    @Test(groups = "unit")
    public void testEditEducationLevel() {
        int count = educationLevelDao.getCountRecords();
        int id = educationLevelDao.create(educationLevel);
        assertEquals(count + 1, educationLevelDao.getCountRecords());
        educationLevel.setTitle("new title");
        educationLevelDao.update(educationLevel);
        assertEquals(count + 1, educationLevelDao.getCountRecords());

        assertEquals("new title" , educationLevelDao.read(id).getTitle());
    }
}
