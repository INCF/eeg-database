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
 * SoftwareServiceTest.java, 2014/08/07 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.services;

import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Software;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.wui.core.common.SoftwareService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Honza on 7.8.14.
 */
@Transactional
public class SoftwareServiceTest extends AbstractServicesTest {

    @Autowired
    private SoftwareService softwareService;

    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private PersonDao personDao;
    private Software software;
    private ResearchGroup researchGroup;

    @Before
    public void setUp() throws Exception {
        Person person = TestUtils.createPersonForTesting("test@test.com", Util.ROLE_ADMIN);

        personDao.create(person);

        researchGroup = new ResearchGroup();
        researchGroup.setDescription("test-description");
        researchGroup.setTitle("test-title");
        researchGroup.setPerson(person);
        researchGroupDao.create(researchGroup);
        software = createSW("new SW");
    }

    @Test
    public void testCreateSoftware() {
        int softwareCountBefore = softwareService.getAllRecords().size();
        int softwareID = softwareService.create(software);
        assertEquals(softwareCountBefore + 1, softwareService.getAllRecords().size());
        assertEquals(softwareID, software.getSoftwareId());
    }
    @Test
    public void testCreateDefaultSoftware() {
        //TODO add this method to the Service class
//        int softwareCountBefore = softwareService.getAllRecords().size();
//        software.setDefaultNumber(1);
//        int softwareID = softwareService.createDefaultRecord(software);
//        assertEquals(softwareCountBefore + 1, softwareService.getAllRecords().size());
//        assertEquals(softwareID, software.getSoftwareId());
    }



    @Test
    public void testCreateSoftwareGroupRel() {
        int SoftwareCountBefore = softwareService.getAllRecords().size();
        int SoftwareGroupBefore = softwareService.getRecordsByGroup(researchGroup.getResearchGroupId()).size();
        softwareService.createGroupRel(software, researchGroup);
        int id = softwareService.create(software);
        assertEquals(SoftwareCountBefore + 1, softwareService.getAllRecords().size());

        List<Software> list = softwareService.getRecordsByGroup(researchGroup.getResearchGroupId());
        assertEquals(SoftwareGroupBefore + 1, list.size());
        assertTrue(softwareService.hasGroupRel(id));
    }


    @Test
    public void testCanSaveTitle() {
        softwareService.createGroupRel(software, researchGroup);
        int id = softwareService.create(software);
        assertTrue(softwareService.canSaveDefaultTitle(software.getTitle(), -1));
        assertTrue(softwareService.canSaveTitle(software.getTitle(), researchGroup.getResearchGroupId(), id));
        assertFalse(softwareService.canSaveTitle(software.getTitle(), researchGroup.getResearchGroupId(), -1));

        Software newSW = createSW("new SW2");
        newSW.setDefaultNumber(1);
        softwareService.create(newSW);
        assertFalse(softwareService.canSaveDefaultTitle(newSW.getTitle(), -1));
        assertTrue(softwareService.canSaveDefaultTitle(newSW.getTitle(), newSW.getSoftwareId()));
    }

    private Software createSW(String title) {
        Software newSw = new Software();
        newSw.setTitle(title);
        newSw.setDescription("This is new testing SW");
        newSw.setDefaultNumber(0);
        return newSw;
    }
}
