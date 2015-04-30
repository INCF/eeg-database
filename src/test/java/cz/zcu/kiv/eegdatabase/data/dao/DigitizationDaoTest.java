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
 *   DigitizationDaoTest.java, 2015/02/02 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.pojo.Digitization;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by stebjan on 3.2.2015.
 */
public class DigitizationDaoTest extends AbstractDataAccessTest {

    @Autowired
    private DigitizationDao digitizationDao;
    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private PersonDao personDao;

    private Digitization digitization;
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
        digitization = new Digitization();
        digitization.setFilter("Filter");
        digitization.setGain(1.0f);
        digitization.setSamplingRate(1000f);
    }

    @Test(groups = "unit")
    public void testCreateDigitization() {
        int digitizationCountBefore = digitizationDao.getAllRecords().size();
        int digitizationID = digitizationDao.create(digitization);
        assertEquals(digitizationCountBefore + 1, digitizationDao.getAllRecords().size());
        assertEquals(digitizationID, digitization.getDigitizationId());
    }



    @Test(groups = "unit")
    public void testCreateGroupDigitization() {
        int digitizationCountBefore = digitizationDao.getAllRecords().size();
        int digitizationGroupBefore = digitizationDao.getRecordsByGroup(researchGroup.getResearchGroupId()).size();
        digitizationDao.createGroupRel(digitization, researchGroup);
        digitizationDao.create(digitization);
        assertEquals(digitizationCountBefore + 1, digitizationDao.getAllRecords().size());

        List<Digitization> list = digitizationDao.getRecordsByGroup(researchGroup.getResearchGroupId());
        assertEquals(digitizationGroupBefore + 1, list.size());
    }

    @Test(groups = "unit")
    public void testGetDigitizationByParams() {
        int digitizationCountBefore = digitizationDao.getAllRecords().size();
        int digitizationID = digitizationDao.create(digitization);
        assertEquals(digitizationCountBefore + 1, digitizationDao.getAllRecords().size());
        assertEquals(digitizationID, digitization.getDigitizationId());
        Digitization dig = digitizationDao.getDigitizationByParams(1000f, 1.0f, "Filter");
        assertNotNull(dig);

        dig = digitizationDao.getDigitizationByParams(10f, 1f, "xxx");
        assertNull(dig);
    }

    @Test(groups = "unit")
    public void testEditDigitization() {
        int count = digitizationDao.getCountRecords();
        int id = digitizationDao.create(digitization);
        assertEquals(count + 1, digitizationDao.getCountRecords());
        digitization.setSamplingRate(500f);
        digitizationDao.update(digitization);
        assertEquals(count + 1, digitizationDao.getCountRecords());

        assertEquals(500f , digitizationDao.read(id).getSamplingRate(), 0.1);
    }
}
