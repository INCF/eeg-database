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
 *   DigitizationServiceTest.java, 2015/03/01 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.services;

import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Digitization;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.wui.core.common.DigitizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

/**
 * Created by Honza on 1.3.15.
 */
public class DigitizationServiceTest extends AbstractServicesTest {
    @Autowired
    private DigitizationService digitizationService;

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
        digitization = createDigitization();
    }

    @Test(groups = "unit")
    public void testCreateDigitization() {
        int digitizationCountBefore = digitizationService.getAllRecords().size();
        int digitizationID = digitizationService.create(digitization);
        assertEquals(digitizationCountBefore + 1, digitizationService.getAllRecords().size());
        assertEquals(digitizationID, digitization.getDigitizationId());
    }


    @Test(groups = "unit")
    public void testCreateDigitizationGroupRel() {
        int digitizationCountBefore = digitizationService.getAllRecords().size();
        int digitizationGroupBefore = digitizationService.getRecordsByGroup(researchGroup.getResearchGroupId()).size();
        digitizationService.createGroupRel(digitization, researchGroup);
        int id = digitizationService.create(digitization);
        assertEquals(digitizationCountBefore + 1, digitizationService.getAllRecords().size());

        List<Digitization> list = digitizationService.getRecordsByGroup(researchGroup.getResearchGroupId());
        assertEquals(digitizationGroupBefore + 1, list.size());
        assertTrue(digitizationService.hasGroupRel(id));
    }

    @Test(groups = "unit")
    public void testGetDigitizationByParam() {
        int digitizationCountBefore = digitizationService.getAllRecords().size();
        int digitizationID = digitizationService.create(digitization);
        assertEquals(digitizationCountBefore + 1, digitizationService.getAllRecords().size());
        List<Digitization> list = digitizationService.readByParameter("filter", "new filter");
        assertEquals(1, list.size());
    }


    private Digitization createDigitization() {
        Digitization newDigitization = new Digitization();
        newDigitization.setFilter("new filter");
        newDigitization.setGain(1f);
        newDigitization.setSamplingRate(500f);
        return newDigitization;
    }
}
