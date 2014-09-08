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
 *   PharmaceuticalDaoTest.java, 2014/07/07 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Pharmaceutical;
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
 * Date: 7.7.14
 */
public class PharmaceuticalDaoTest extends AbstractDataAccessTest {


    @Autowired
    private SimplePharmaceuticalDao pharmaceuticalDao;
    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private PersonDao personDao;
    private Pharmaceutical pharmaceutical;
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
        pharmaceutical = new Pharmaceutical();
        pharmaceutical.setTitle("New pharmaceutical");
        pharmaceutical.setDescription("This is new testing pharmaceutical");
    }


    @Test
    @Transactional
    public void testCreatePharmaceutical() {
        int pharmaceuticalCountBefore = pharmaceuticalDao.getAllRecords().size();
        int pharmaceuticalID = pharmaceuticalDao.create(pharmaceutical);
        assertEquals(pharmaceuticalCountBefore + 1, pharmaceuticalDao.getAllRecords().size());
        assertEquals(pharmaceuticalID, pharmaceutical.getPharmaceuticalId());
    }



    @Test
    @Transactional
    public void testCreateGroupPharmaceutical() {
        int pharmaceuticalCountBefore = pharmaceuticalDao.getAllRecords().size();
        int pharmaceuticalGroupBefore = pharmaceuticalDao.getRecordsByGroup(researchGroup.getResearchGroupId()).size();
        pharmaceuticalDao.createGroupRel(pharmaceutical, researchGroup);
        pharmaceuticalDao.create(pharmaceutical);
        assertEquals(pharmaceuticalCountBefore + 1, pharmaceuticalDao.getAllRecords().size());

        List<Pharmaceutical> list = pharmaceuticalDao.getRecordsByGroup(researchGroup.getResearchGroupId());
        assertEquals(pharmaceuticalGroupBefore + 1, list.size());
    }
}

