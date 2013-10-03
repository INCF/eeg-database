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
 *   ExperimentOptParamDefDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDefGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

import java.util.List;

public interface ExperimentOptParamDefDao extends GenericDao<ExperimentOptParamDef, Integer> {
    public List<ExperimentOptParamDef> getItemsForList();

    public boolean canDelete(int id);

     public List<ExperimentOptParamDef> getRecordsByGroup(int groupId);

    public void createDefaultRecord(ExperimentOptParamDef experimentOptParamDef);

    public List<ExperimentOptParamDef> getDefaultRecords();

    public boolean hasGroupRel(int id);

    public void deleteGroupRel(ExperimentOptParamDefGroupRel experimentOptParamDefGroupRel);

    public ExperimentOptParamDefGroupRel getGroupRel(int experimentOptParamDefId, int researchGroupId);

    public void createGroupRel(ExperimentOptParamDefGroupRel experimentOptParamDefGroupRel);

    public void createGroupRel(ExperimentOptParamDef experimentOptParamDef, ResearchGroup researchGroup);

    public boolean isDefault(int id);
}
