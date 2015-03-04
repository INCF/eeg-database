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
 *   DigitizationService.java, 2015/03/01 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.common;

import cz.zcu.kiv.eegdatabase.data.pojo.Digitization;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.core.GenericService;

import java.util.List;

/**
 * Created by Honza on 1.3.15.
 */
public interface DigitizationService extends GenericService<Digitization, Integer> {


    void createGroupRel(Digitization persistent, ResearchGroup researchGroup);

    List<Digitization> getItemsForList();

    List<Digitization> getRecordsByGroup(int groupId);

    boolean canDelete(int id);

    boolean hasGroupRel(int id);

    void deleteGroupRel(Digitization persistent, ResearchGroup researchGroup);

}
