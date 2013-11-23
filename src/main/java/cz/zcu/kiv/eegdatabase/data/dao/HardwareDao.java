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
 *   HardwareDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.HardwareGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

import java.util.List;

public interface HardwareDao extends GenericDao<Hardware, Integer> {
    public void createDefaultRecord(Hardware hardware);

    public void createGroupRel(HardwareGroupRel hardwareGroupRel);

    public void createGroupRel(Hardware hardware, ResearchGroup researchGroup);

    public List<Hardware> getItemsForList();

    public List<Hardware> getRecordsByGroup(int groupId);

    public List<Hardware> getDefaultRecords();

    public boolean canDelete(int id);

    public boolean isDefault(int id);

    public void deleteGroupRel(HardwareGroupRel hardwareGroupRel);

    public HardwareGroupRel getGroupRel(int hardwareId, int researchGroupId);

    public boolean hasGroupRel(int id);

    public boolean canSaveTitle(String title, int groupId, int hwId);

    public boolean canSaveNewTitle(String title, int groupId);

    public boolean canSaveDefaultTitle(String title, int hwId);

}
