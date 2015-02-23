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
 *   DiseaseDaoTest.java, 2014/07/07 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.pojo.Disease;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.Util;
import org.testng.annotations.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;

import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * User: Jan Stebetak
 * Date: 7.7.14
 */
public class DiseaseDaoTest extends AbstractDataAccessTest {


    @Autowired
    private SimpleDiseaseDao diseaseDao;
    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private PersonDao personDao;
    private Disease disease;
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
        disease = new Disease();
        disease.setTitle("New Disease");
        disease.setDescription("This is new testing disease");
    }


    @Test(groups = "unit")
    public void testCreateDisease() {
        int diseaseCountBefore = diseaseDao.getAllRecords().size();
        int diseaseID = diseaseDao.create(disease);
        assertEquals(diseaseCountBefore + 1, diseaseDao.getAllRecords().size());
        assertEquals(diseaseID, disease.getDiseaseId());
    }



    @Test(groups = "unit")
    public void testCreateGroupDisease() {
        int diseaseCountBefore = diseaseDao.getAllRecords().size();
        int diseaseGroupBefore = diseaseDao.getRecordsByGroup(researchGroup.getResearchGroupId()).size();
        diseaseDao.createGroupRel(disease, researchGroup);
        diseaseDao.create(disease);
        assertEquals(diseaseCountBefore + 1, diseaseDao.getAllRecords().size());

        List<Disease> list = diseaseDao.getRecordsByGroup(researchGroup.getResearchGroupId());
        assertEquals(diseaseGroupBefore + 1, list.size());
    }
    @Test(groups = "unit")
    public void testEditDisease() {
        int count = diseaseDao.getCountRecords();
        int id = diseaseDao.create(disease);
        assertEquals(count + 1, diseaseDao.getCountRecords());
        disease.setDescription("new desc");
        diseaseDao.update(disease);
        assertEquals(count + 1, diseaseDao.getCountRecords());

        assertEquals("new desc" , diseaseDao.read(id).getDescription());
    }
}
