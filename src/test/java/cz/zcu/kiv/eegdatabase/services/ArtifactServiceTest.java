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
 * ArtifactServiceTest.java, 2014/08/10 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.services;

import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Artifact;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.wui.core.common.ArtifactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;


/**
 * Created by Honza on 10.8.14.
 */
public class ArtifactServiceTest extends AbstractServicesTest {

    @Autowired
    private ArtifactService artifactService;

    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private PersonDao personDao;
    private Artifact artifact;
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
        artifact = new Artifact();
        artifact.setCompensation("test comp");
        artifact.setRejectCondition("test condition");
    }

    @Test(groups = "unit")
    public void testCreateArtifact() {
        int artifactCountBefore = artifactService.getAllRecords().size();
        int id = artifactService.create(artifact);
        assertEquals(artifactCountBefore + 1, artifactService.getAllRecords().size());
        assertEquals(id, artifact.getArtifactId());
    }


    @Test(groups = "unit")
    public void testCreateArtifactGroupRel() {
        int artifactCountBefore = artifactService.getAllRecords().size();
        int artifactGroupBefore = artifactService.getRecordsByGroup(researchGroup.getResearchGroupId()).size();
        int id = artifactService.create(artifact);
        assertEquals(artifactCountBefore + 1, artifactService.getAllRecords().size());

        List<Artifact> list = artifactService.getRecordsByGroup(researchGroup.getResearchGroupId());
        assertEquals(artifactGroupBefore, list.size());
        assertFalse(artifactService.hasGroupRel(id));
    }

}
