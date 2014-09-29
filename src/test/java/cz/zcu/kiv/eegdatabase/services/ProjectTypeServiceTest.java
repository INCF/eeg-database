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
 * ProjectTypeServiceTest.java, 2014/08/08 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.services;

import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ProjectType;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.wui.core.common.ProjectTypeService;
import org.testng.annotations.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeMethod;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Honza on 8.8.14.
 */
public class ProjectTypeServiceTest extends AbstractServicesTest {

    @Autowired
    private ProjectTypeService projectTypeService;

    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private PersonDao personDao;
    private ProjectType projectType;
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
        projectType = createProjectType("new Disease");
    }

    @Test(groups = "unit")
    public void testCreateProjectType() {
        int pTCountBefore = projectTypeService.getAllRecords().size();
        int id = projectTypeService.create(projectType);
        assertEquals(pTCountBefore + 1, projectTypeService.getAllRecords().size());
        assertEquals(id, projectType.getProjectTypeId());
    }


    @Test(groups = "unit")
    public void testCreateProjectGroupRel() {
        int pTCountBefore = projectTypeService.getAllRecords().size();
        int diseaseGroupBefore = projectTypeService.getRecordsByGroup(researchGroup.getResearchGroupId()).size();
        projectTypeService.createGroupRel(projectType, researchGroup);
        int id = projectTypeService.create(projectType);
        assertEquals(pTCountBefore + 1, projectTypeService.getAllRecords().size());

        List<ProjectType> list = projectTypeService.getRecordsByGroup(researchGroup.getResearchGroupId());
        assertEquals(diseaseGroupBefore + 1, list.size());
        assertTrue(projectTypeService.hasGroupRel(id));
    }


    @Test(groups = "unit")
    public void testCanSaveTitle() {
        projectTypeService.createGroupRel(projectType, researchGroup);
        int id = projectTypeService.create(projectType);
        assertTrue(projectTypeService.canSaveTitle(projectType.getTitle(), researchGroup.getResearchGroupId(), id));
        assertFalse(projectTypeService.canSaveTitle(projectType.getTitle(), researchGroup.getResearchGroupId(), -1));
    }

    private ProjectType createProjectType(String title) {
        ProjectType newPT = new ProjectType();
        newPT.setTitle(title);
        newPT.setDescription("This is new testing PT");
        return newPT;
    }
}
