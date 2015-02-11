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
 *   ExperimentPackageDaoTest.java, 2015/02/11 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.logic.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static junit.framework.Assert.assertEquals;

/**
 * Created by stebjan on 11.2.2015.
 */
public class ExperimentPackageDaoTest extends AbstractDataAccessTest {

    @Autowired
    private ExperimentPackageDao experimentPackageDao;

    private ExperimentPackage experimentPackage;

    @Autowired
    private ResearchGroupDao researchGroupDao;

    @Autowired
    private PersonDao personDao;

    private Person person;

    @BeforeMethod(groups = "unit")
    public void setUp() {
        person = TestUtils.createPersonForTesting("test@test.com", Util.ROLE_ADMIN);

        personDao.create(person);

        ResearchGroup researchGroup = new ResearchGroup();
        researchGroup.setDescription("test-description");
        researchGroup.setTitle("test-title");
        researchGroup.setPerson(person);
        researchGroup.setPaidAccount(true);
        researchGroupDao.create(researchGroup);

        experimentPackage = new ExperimentPackage();
        experimentPackage.setName("test name");
        experimentPackage.setResearchGroup(researchGroup);
    }

    @Test(groups = "unit")
    public void testCreateExperimentPackage() {

        int countBefore = experimentPackageDao.getCountRecords();

        experimentPackageDao.create(experimentPackage);
        assertEquals(countBefore + 1, experimentPackageDao.getCountRecords());
    }

    @Test(groups = "unit")
    public void testListVisiblePackages() {

        experimentPackageDao.create(experimentPackage);

        assertEquals(0, experimentPackageDao.listVisiblePackages(person).size());
    }
}
