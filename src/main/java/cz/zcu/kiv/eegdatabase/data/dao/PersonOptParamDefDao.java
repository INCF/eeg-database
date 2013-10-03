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
 *   PersonOptParamDefDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamDefGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

import java.util.List;

public interface PersonOptParamDefDao extends GenericDao<PersonOptParamDef, Integer> {
    public List<PersonOptParamDef> getItemsForList();

    public boolean canDelete(int id);
    
     public List<PersonOptParamDef> getRecordsByGroup(int groupId);

    public void createDefaultRecord(PersonOptParamDef personOptParamDef);

    public List<PersonOptParamDef> getDefaultRecords();

    public boolean hasGroupRel(int id);

    public void deleteGroupRel(PersonOptParamDefGroupRel personOptParamDefGroupRel);

    public PersonOptParamDefGroupRel getGroupRel(int personOptParamDefId, int researchGroupId);

    public void createGroupRel(PersonOptParamDefGroupRel personOptParamDefGroupRel);

    public void createGroupRel(PersonOptParamDef personOptParamDef, ResearchGroup researchGroup);

    public boolean isDefault(int id);
}
