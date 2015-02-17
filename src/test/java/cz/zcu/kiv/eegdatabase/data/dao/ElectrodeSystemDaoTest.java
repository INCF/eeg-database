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
 *   ElectrodeSystemDaoTest.java, 2014/07/08 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.pojo.ElectrodeSystem;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.Util;
import org.junit.Before;
import org.testng.annotations.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeMethod;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by stebjan on 8.7.14.
 */
public class ElectrodeSystemDaoTest extends AbstractDataAccessTest {
    @Autowired
    private SimpleElectrodeSystemDao electrodeSystemDao;
    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private PersonDao personDao;
    private ElectrodeSystem electrodeSystem;
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
        electrodeSystem = new ElectrodeSystem();
        electrodeSystem.setTitle("New System");
        electrodeSystem.setDescription("This is new testing system");
        electrodeSystem.setDefaultNumber(0);
    }


    @Test(groups = "unit")
    public void testCreateElectrodeSystem() {
        int countBefore = electrodeSystemDao.getAllRecords().size();
        int id = electrodeSystemDao.create(electrodeSystem);
        assertEquals(countBefore + 1, electrodeSystemDao.getCountRecords());
        assertEquals(id, electrodeSystem.getElectrodeSystemId());
    }


    @Test(groups = "unit")
    public void testCreateGroupElectrodeSystem() {
        int countBefore = electrodeSystemDao.getAllRecords().size();
        int croupBefore = electrodeSystemDao.getRecordsByGroup(researchGroup.getResearchGroupId()).size();
        electrodeSystemDao.createGroupRel(electrodeSystem, researchGroup);
        electrodeSystemDao.create(electrodeSystem);
        assertEquals(countBefore + 1, electrodeSystemDao.getAllRecords().size());

        List<ElectrodeSystem> list = electrodeSystemDao.getRecordsByGroup(researchGroup.getResearchGroupId());
        assertEquals(croupBefore + 1, list.size());
    }

    @Test(groups = "unit")
    public void testCreateDefaultRecord() {
        int count = electrodeSystemDao.getCountRecords();
        electrodeSystemDao.createDefaultRecord(electrodeSystem);
        assertEquals(count + 1, electrodeSystemDao.getCountRecords());
    }
}
