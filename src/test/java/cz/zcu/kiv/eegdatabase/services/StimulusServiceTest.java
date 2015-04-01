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
 * StimulusServiceTest.java, 2014/08/12 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.services;

import cz.zcu.kiv.eegdatabase.data.pojo.Stimulus;
import cz.zcu.kiv.eegdatabase.wui.core.common.StimulusService;
import org.testng.annotations.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

/**
 * Created by Honza on 12.8.14.
 */
public class StimulusServiceTest extends AbstractServicesTest {

    @Autowired
    private StimulusService stimulusService;

    private Stimulus stimulus;

    @BeforeMethod(groups = "unit")
    public void setUp() throws Exception {

        stimulus = new Stimulus();
        stimulus.setDescription("test description");
    }

    @Test(groups = "unit")
    public void testCreateStimulus() {
        int stimulusCountBefore = stimulusService.getAllRecords().size();
        int id = stimulusService.create(stimulus);
        assertEquals(stimulusCountBefore + 1, stimulusService.getAllRecords().size());
        assertEquals(id, stimulus.getStimulusId());
    }

    @Test(groups = "unit")
    public void testCanSaveDescription() {
        stimulusService.create(stimulus);
        int id = stimulusService.create(stimulus);
        assertFalse(stimulusService.canSaveDescription(stimulus.getDescription()));
    }

}

