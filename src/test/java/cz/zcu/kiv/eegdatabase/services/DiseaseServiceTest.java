/*******************************************************************************
 * This file is part of the EEG-database project
 *
 * ==========================================
 *
 * Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 * ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * ***********************************************************************************************************************
 *
 * DiseaseServiceTest.java, 2014/08/08 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.services;

import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Disease;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.wui.core.common.DiseaseService;
import org.testng.annotations.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Created by Honza on 8.8.14.
 */

public class DiseaseServiceTest extends AbstractServicesTest {

    @Autowired
    private DiseaseService diseaseService;

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
        disease = createDisease("new Disease");
    }

    @Test(groups = "unit")
    public void testCreateDisease() {
        int diseaseCountBefore = diseaseService.getAllRecords().size();
        int diseaseID = diseaseService.create(disease);
        assertEquals(diseaseCountBefore + 1, diseaseService.getAllRecords().size());
        assertEquals(diseaseID, disease.getDiseaseId());
    }


    @Test(groups = "unit")
    public void testCreateDiseaseGroupRel() {
        int diseaseCountBefore = diseaseService.getAllRecords().size();
        int diseaseGroupBefore = diseaseService.getRecordsByGroup(researchGroup.getResearchGroupId()).size();
        diseaseService.createGroupRel(disease, researchGroup);
        int id = diseaseService.create(disease);
        assertEquals(diseaseCountBefore + 1, diseaseService.getAllRecords().size());

        List<Disease> list = diseaseService.getRecordsByGroup(researchGroup.getResearchGroupId());
        assertEquals(diseaseGroupBefore + 1, list.size());
        assertTrue(diseaseService.hasGroupRel(id));
    }


    @Test(groups = "unit")
    public void testCanSaveTitle() {
        diseaseService.createGroupRel(disease, researchGroup);
        int id = diseaseService.create(disease);
        assertTrue(diseaseService.canSaveTitle(disease.getTitle(), researchGroup.getResearchGroupId(), id));
        assertFalse(diseaseService.canSaveTitle(disease.getTitle(), researchGroup.getResearchGroupId(), -1));
    }

    private Disease createDisease(String title) {
        Disease newDisease = new Disease();
        newDisease.setTitle(title);
        newDisease.setDescription("This is new testing Disease");
        return newDisease;
    }
}
