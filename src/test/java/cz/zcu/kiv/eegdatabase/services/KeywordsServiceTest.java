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
 * KeywordsServiceTest.java, 2014/08/10 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.services;

import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Keywords;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.wui.core.common.KeywordsService;
import org.testng.annotations.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeMethod;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Honza on 10.8.14.
 */
@Transactional
public class KeywordsServiceTest extends AbstractServicesTest {

    @Autowired
    private KeywordsService keywordsService;

    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private PersonDao personDao;
    private Keywords keywords;
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
        keywords = new Keywords();
        keywords.setKeywordsText("keyword");
        keywords.setResearchGroup(researchGroup);
    }

    @Test(groups = "unit")
    public void testCreateArtifact() {
        int keywordsCountBefore = keywordsService.getAllRecords().size();
        int id = keywordsService.create(keywords);
        assertEquals(keywordsCountBefore + 1, keywordsService.getAllRecords().size());
        assertEquals(id, keywords.getKeywordsId());
    }


    @Test(groups = "unit")
    public void testGetKeywords() {
        int keywordsCountBefore = keywordsService.getAllRecords().size();

        keywordsService.create(keywords);
        assertEquals(keywordsCountBefore + 1, keywordsService.getAllRecords().size());
        assertEquals("keyword", keywordsService.getKeywords(researchGroup.getResearchGroupId()));
    }

}
