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
 *   ExperimentsOptParamFacade.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.experiments.param;

import java.util.List;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDefGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacade;

public interface ExperimentsOptParamFacade extends GenericFacade<ExperimentOptParamDef, Integer> {

    List<ExperimentOptParamDef> getItemsForList();

    boolean canDelete(int id);

    List<ExperimentOptParamDef> getRecordsByGroup(int groupId);

    void createDefaultRecord(ExperimentOptParamDef experimentOptParamDef);

    List<ExperimentOptParamDef> getDefaultRecords();

    boolean hasGroupRel(int id);

    void deleteGroupRel(ExperimentOptParamDefGroupRel experimentOptParamDefGroupRel);

    ExperimentOptParamDefGroupRel getGroupRel(int experimentOptParamDefId, int researchGroupId);

    void createGroupRel(ExperimentOptParamDefGroupRel experimentOptParamDefGroupRel);

    void createGroupRel(ExperimentOptParamDef experimentOptParamDef, ResearchGroup researchGroup);

    boolean isDefault(int id);
}
