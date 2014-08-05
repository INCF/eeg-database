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
 *   HardwareDaoTest.java, 2014/30/06 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.Util;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * User: Jan Stebetak
 * Date: 30.6.14
 */
public class HardwareDaoTest extends AbstractDataAccessTest {


    @Autowired
    private HardwareDao hardwareDao;
    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private PersonDao personDao;
    private Hardware hardware;
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
        hardware = new Hardware();
        hardware.setTitle("New HW");
        hardware.setDescription("This is new testing HW");
        hardware.setType("HW type");
        hardware.setDefaultNumber(0);
    }


    @Test
    @Transactional
    public void testCreateHardware() {
        int hardwareCountBefore = hardwareDao.getAllRecords().size();
        int hardwareID = hardwareDao.create(hardware);
        assertEquals(hardwareCountBefore + 1, hardwareDao.getAllRecords().size());
        assertEquals(hardwareID, hardware.getHardwareId());
    }


    @Test
    @Transactional
    public void testCreateDefaultRecord() throws Exception {
        int expectedValue = hardwareDao.getDefaultRecords().size();
        hardwareDao.createDefaultRecord(hardware);
        assertEquals(expectedValue + 1, hardwareDao.getDefaultRecords().size());
    }

    @Test
    @Transactional
    public void testCreateGroupHardware() {
        int hardwareCountBefore = hardwareDao.getAllRecords().size();
        int hardwareGroupBefore = hardwareDao.getRecordsByGroup(researchGroup.getResearchGroupId()).size();
        hardwareDao.createGroupRel(hardware, researchGroup);
        hardwareDao.create(hardware);
        assertEquals(hardwareCountBefore + 1, hardwareDao.getAllRecords().size());

        List<Hardware> list = hardwareDao.getRecordsByGroup(researchGroup.getResearchGroupId());
        assertEquals(hardwareGroupBefore + 1, list.size());
    }
}
