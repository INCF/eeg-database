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
 *   HardwareFacade.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.common;

import java.util.List;

import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.HardwareGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacade;

public interface HardwareFacade extends GenericFacade<Hardware, Integer>{
    
    void createDefaultRecord(Hardware hardware);

    void createGroupRel(HardwareGroupRel hardwareGroupRel);

    void createGroupRel(Hardware hardware, ResearchGroup researchGroup);

    List<Hardware> getItemsForList();

    List<Hardware> getRecordsNewerThan(long oracleScn);

    List<Hardware> getRecordsByGroup(int groupId);

    List<Hardware> getDefaultRecords();

    boolean canDelete(int id);

    boolean isDefault(int id);

    void deleteGroupRel(HardwareGroupRel hardwareGroupRel);

    HardwareGroupRel getGroupRel(int hardwareId, int researchGroupId);

    boolean hasGroupRel(int id);

    boolean canSaveTitle(String title, int groupId, int hwId);

    boolean canSaveNewTitle(String title, int groupId);

    boolean canSaveDefaultTitle(String title, int hwId);
}
