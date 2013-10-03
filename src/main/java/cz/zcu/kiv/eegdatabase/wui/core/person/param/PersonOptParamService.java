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
 *   PersonOptParamService.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.person.param;

import java.util.List;

import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamDefGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamVal;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamValId;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.core.GenericService;

public interface PersonOptParamService extends GenericService<PersonOptParamDef, Integer>{
    
    List<PersonOptParamDef> getItemsForList();

    boolean canDelete(int id);

    List<PersonOptParamDef> getRecordsByGroup(int groupId);

    void createDefaultRecord(PersonOptParamDef personOptParamDef);

    List<PersonOptParamDef> getDefaultRecords();

    boolean hasGroupRel(int id);

    void deleteGroupRel(PersonOptParamDefGroupRel personOptParamDefGroupRel);

    PersonOptParamDefGroupRel getGroupRel(int personOptParamDefId, int researchGroupId);

    void createGroupRel(PersonOptParamDefGroupRel personOptParamDefGroupRel);

    void createGroupRel(PersonOptParamDef personOptParamDef, ResearchGroup researchGroup);

    boolean isDefault(int id);

    PersonOptParamValId create(PersonOptParamVal newInstance);

    PersonOptParamVal read(PersonOptParamValId id);

    List<PersonOptParamVal> readValueByParameter(String parameterName, int parameterValue);

    List<PersonOptParamVal> readValueByParameter(String parameterName, String parameterValue);

    void update(PersonOptParamVal transientObject);

    void delete(PersonOptParamVal persistentObject);

    List<PersonOptParamVal> getAllValueRecords();

    List<PersonOptParamVal> getValueRecordsAtSides(int first, int max);

    int getValueCountRecords();
}
