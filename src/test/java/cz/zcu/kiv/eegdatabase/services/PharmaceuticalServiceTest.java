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
 * PharmaceuticalServiceTest.java, 2014/08/08 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.services;

import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Pharmaceutical;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.wui.core.common.PharmaceuticalService;
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

public class PharmaceuticalServiceTest extends AbstractServicesTest {

    @Autowired
    private PharmaceuticalService pharmaceuticalService;

    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private PersonDao personDao;
    private Pharmaceutical pharmaceutical;
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
        pharmaceutical = createPharmaceutical("new Disease");
    }

    @Test(groups = "unit")
    public void testCreatePharmaceutical() {
        int pharmaCountBefore = pharmaceuticalService.getAllRecords().size();
        int pharmaceuticalID = pharmaceuticalService.create(pharmaceutical);
        assertEquals(pharmaCountBefore + 1, pharmaceuticalService.getAllRecords().size());
        assertEquals(pharmaceuticalID, pharmaceutical.getPharmaceuticalId());
    }


    @Test(groups = "unit")
    public void testCreatePharmaceuticalGroupRel() {
        int pharmaCountBefore = pharmaceuticalService.getAllRecords().size();
        int pharmaGroupBefore = pharmaceuticalService.getRecordsByGroup(researchGroup.getResearchGroupId()).size();
        pharmaceuticalService.createGroupRel(pharmaceutical, researchGroup);
        int id = pharmaceuticalService.create(pharmaceutical);
        assertEquals(pharmaCountBefore + 1, pharmaceuticalService.getAllRecords().size());

        List<Pharmaceutical> list = pharmaceuticalService.getRecordsByGroup(researchGroup.getResearchGroupId());
        assertEquals(pharmaGroupBefore + 1, list.size());
        assertTrue(pharmaceuticalService.hasGroupRel(id));
    }


    @Test(groups = "unit")
    public void testCanSaveTitle() {
        pharmaceuticalService.createGroupRel(pharmaceutical, researchGroup);
        int id = pharmaceuticalService.create(pharmaceutical);
        assertTrue(pharmaceuticalService.canSaveTitle(pharmaceutical.getTitle(), researchGroup.getResearchGroupId(), id));
        assertFalse(pharmaceuticalService.canSaveTitle(pharmaceutical.getTitle(), researchGroup.getResearchGroupId(), -1));
    }

    private Pharmaceutical createPharmaceutical(String title) {
        Pharmaceutical newPharmaceutical = new Pharmaceutical();
        newPharmaceutical.setTitle(title);
        newPharmaceutical.setDescription("This is new testing pharma");
        return newPharmaceutical;
    }
}
