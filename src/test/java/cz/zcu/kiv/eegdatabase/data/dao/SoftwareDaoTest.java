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
 *   SoftwareDaoTest.java, 2014/30/06 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Software;
import cz.zcu.kiv.eegdatabase.logic.Util;
import org.testng.annotations.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeMethod;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * User: Jan Stebetak
 * Date: 30.6.14
 */
public class SoftwareDaoTest extends AbstractDataAccessTest {


    @Autowired
    private SimpleSoftwareDao softwareDao;
    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private PersonDao personDao;
    private Software software;
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
        software = new Software();
        software.setTitle("SW_test");
        software.setDescription("This is new testing SW");
        software.setDefaultNumber(0);
    }


    @Test(groups = "unit")
    public void testCreateSoftware() {
        int softwareCountBefore = softwareDao.getAllRecords().size();
        int softwareID = softwareDao.create(software);
        assertEquals(softwareCountBefore + 1, softwareDao.getAllRecords().size());
        assertEquals(softwareID, software.getSoftwareId());
    }


    @Test(groups = "unit")
    public void testCreateDefaultRecord() throws Exception {
        int expectedValue = softwareDao.getDefaultRecords().size();
        softwareDao.createDefaultRecord(software);
        assertEquals(expectedValue + 1, softwareDao.getDefaultRecords().size());
    }

    @Test(groups = "unit")
    public void testCreateGroupSoftware() {
        int softwareCountBefore = softwareDao.getAllRecords().size();
        int softwareGroupBefore = softwareDao.getRecordsByGroup(researchGroup.getResearchGroupId()).size();
        softwareDao.createGroupRel(software, researchGroup);
        softwareDao.create(software);
        assertEquals(softwareCountBefore + 1, softwareDao.getAllRecords().size());

        List<Software> list = softwareDao.getRecordsByGroup(researchGroup.getResearchGroupId());
        assertEquals(softwareGroupBefore + 1, list.size());
    }
}
