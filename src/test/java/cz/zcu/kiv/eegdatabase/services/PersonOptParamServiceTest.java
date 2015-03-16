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
 * PersonOptParamServiceTest.java, 2015/03/15 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.services;

import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.wui.core.person.param.PersonOptParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Honza on 15.3.15.
 */
public class PersonOptParamServiceTest extends AbstractServicesTest {
    @Autowired
    private PersonOptParamService personOptParamService;

    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private PersonDao personDao;
    private PersonOptParamDef paramDef;
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
        paramDef = new PersonOptParamDef();
        paramDef.setParamDataType("type");
        paramDef.setParamName("name");
        paramDef.setDefaultNumber(0);
    }

    @Test(groups = "unit")
    public void testCreateOptParamDef() {
        int paramDefCountBefore = personOptParamService.getAllRecords().size();
        int paramDefID = personOptParamService.create(paramDef);
        assertEquals(paramDefCountBefore + 1, personOptParamService.getAllRecords().size());
        assertEquals(paramDefID, paramDef.getPersonOptParamDefId());
    }

    @Test(groups = "unit")
    public void testCreateDefaultOptParamDef() {

        paramDef.setDefaultNumber(1);
        int paramDefCountBefore = personOptParamService.getAllRecords().size();
        personOptParamService.createDefaultRecord(paramDef);
        assertEquals(paramDefCountBefore + 1, personOptParamService.getAllRecords().size());

    }

    @Test(groups = "unit")
    public void testCreateOptParamGroupRel() {
        int paramCountBefore = personOptParamService.getAllRecords().size();
        int paramGroupBefore = personOptParamService.getRecordsByGroup(researchGroup.getResearchGroupId()).size();
        personOptParamService.createGroupRel(paramDef, researchGroup);
        int id = personOptParamService.create(paramDef);
        assertEquals(paramCountBefore + 1, personOptParamService.getAllRecords().size());

        List<PersonOptParamDef> list = personOptParamService.getRecordsByGroup(researchGroup.getResearchGroupId());
        assertEquals(paramGroupBefore + 1, list.size());
        assertTrue(personOptParamService.hasGroupRel(id));
    }



}
