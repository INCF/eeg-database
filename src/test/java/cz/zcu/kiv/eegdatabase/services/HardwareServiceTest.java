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
 * HardwareServiceTest.java, 2014/08/07 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.services;

import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.wui.core.common.HardwareService;
import org.testng.annotations.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Created by Honza on 7.8.14.
 */
public class HardwareServiceTest extends AbstractServicesTest {

    @Autowired
    private HardwareService hardwareService;

    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private PersonDao personDao;
    private Hardware hardware;
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
        hardware = createHW("new HW");
    }

    @Test(groups = "unit")
    public void testCreateHardware() {
        int hardwareCountBefore = hardwareService.getAllRecords().size();
        int hardwareID = hardwareService.create(hardware);
        assertEquals(hardwareCountBefore + 1, hardwareService.getAllRecords().size());
        assertEquals(hardwareID, hardware.getHardwareId());
    }

    @Test(groups = "unit")
    public void testCreateDefaultHardware() {
        int hardwareCountBefore = hardwareService.getAllRecords().size();
        hardware.setDefaultNumber(1);
        hardwareService.createDefaultRecord(hardware);
        assertEquals(hardwareCountBefore + 1, hardwareService.getAllRecords().size());

        assertTrue(hardwareService.isDefault(hardware.getHardwareId()));
    }
    @Test(groups = "unit")
    public void testCreateHardwareGroupRel() {
        int hardwareCountBefore = hardwareService.getAllRecords().size();
        int hardwareGroupBefore = hardwareService.getRecordsByGroup(researchGroup.getResearchGroupId()).size();
        hardwareService.createGroupRel(hardware, researchGroup);
        int id = hardwareService.create(hardware);
        assertEquals(hardwareCountBefore + 1, hardwareService.getAllRecords().size());

        List<Hardware> list = hardwareService.getRecordsByGroup(researchGroup.getResearchGroupId());
        assertEquals(hardwareGroupBefore + 1, list.size());
        assertTrue(hardwareService.hasGroupRel(id));
    }

    @Test(groups = "unit")
    public void testGetDefaultRecords() {
        int hardwareCountBefore = hardwareService.getAllRecords().size();
        int hardwareDefaultBefore = hardwareService.getDefaultRecords().size();
        //This is not a default HW
        int id = hardwareService.create(hardware);

        Hardware newHW = createHW("new HW2");
        newHW.setDefaultNumber(1);
        hardwareService.createDefaultRecord(newHW);
        assertEquals(hardwareCountBefore + 2, hardwareService.getAllRecords().size());

        assertEquals(hardwareDefaultBefore + 1, hardwareService.getDefaultRecords().size());

        assertFalse(hardwareService.hasGroupRel(newHW.getHardwareId()));
    }

    @Test(groups = "unit")
    public void testCanSaveTitle() {
        hardwareService.createGroupRel(hardware, researchGroup);
        int id = hardwareService.create(hardware);
        assertTrue(hardwareService.canSaveDefaultTitle(hardware.getTitle(), -1));
        assertTrue(hardwareService.canSaveTitle(hardware.getTitle(), researchGroup.getResearchGroupId(), id));
        assertFalse(hardwareService.canSaveTitle(hardware.getTitle(), researchGroup.getResearchGroupId(), -1));

        Hardware newHw = createHW("new HW2");
        newHw.setDefaultNumber(1);
        hardwareService.createDefaultRecord(newHw);
        assertFalse(hardwareService.canSaveDefaultTitle(newHw.getTitle(), -1));
        assertTrue(hardwareService.canSaveDefaultTitle(newHw.getTitle(), newHw.getHardwareId()));
    }

    private Hardware createHW(String title) {
        Hardware newHw = new Hardware();
        newHw.setTitle(title);
        newHw.setDescription("This is new testing HW");
        newHw.setType("HW type");
        newHw.setDefaultNumber(0);
        return newHw;
    }
}
