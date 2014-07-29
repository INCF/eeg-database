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
 * ResearchGroupServiceTest.java, 2014/07/29 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.services;

import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.Assert.assertEquals;

/**
 * Created by stebjan on 29.7.2014.
 */
@Transactional
public class ResearchGroupServiceTest extends AbstractServicesTest {

    @Autowired
    private ResearchGroupService researchGroupService;

    @Autowired
    private PersonDao personDao;

    private Person person;
    private ResearchGroup researchGroup;

    @Before

    public void setUp() {
        person = TestUtils.createPersonForTesting("test@test.com", Util.ROLE_ADMIN);

        personDao.create(person);


        researchGroup = new ResearchGroup();
        researchGroup.setDescription("test-description");
        researchGroup.setTitle("test-title");
        researchGroup.setPerson(person);
    }

    @Test
    public void testCreateResearchGroup() {
        int count = researchGroupService.getCountForList();
        researchGroupService.create(researchGroup);
        assertEquals(count + 1, researchGroupService.getCountRecords());
        assertEquals("test-title", researchGroupService.getResearchGroupTitle(researchGroup.getResearchGroupId()));
        assertEquals("test@test.com", researchGroupService.read(researchGroup.getResearchGroupId()).getPerson().getUsername());

    }

    @Test
    public void testGetGroupsWhereOwner() {
        int count = researchGroupService.getResearchGroupsWhereOwner(person).size();
        researchGroupService.create(researchGroup);
        ResearchGroup group2 = new ResearchGroup();
        group2.setDescription("desc");
        group2.setTitle("test");
        group2.setPerson(person);
        researchGroupService.create(group2);
        assertEquals(count + 2, researchGroupService.getResearchGroupsWhereOwner(person).size());

    }
}
