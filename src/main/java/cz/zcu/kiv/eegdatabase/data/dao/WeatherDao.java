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
 *   WeatherDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import cz.zcu.kiv.eegdatabase.data.pojo.WeatherGroupRel;

import java.util.List;

public interface WeatherDao extends GenericDao<Weather, Integer> {

    public List<Weather> getItemsForList();

    public boolean canSaveDescription(String description, int groupId, int weatherId);

    public boolean canSaveDefaultDescription(String description, int weatherId);

    public boolean canDelete(int id);

    public boolean canSaveNewDescription(String description, int groupId);

    public void createDefaultRecord(Weather weather);

    public void createGroupRel(WeatherGroupRel weatherGroupRel);

    public void createGroupRel(Weather weather, ResearchGroup researchGroup);

    public List<Weather> getRecordsByGroup(int groupId);

    public List<Weather> getDefaultRecords();

    public boolean isDefault(int id);

    public void deleteGroupRel(WeatherGroupRel weatherGroupRel);

    public WeatherGroupRel getGroupRel(int weatherId, int researchGroupId);

    public boolean hasGroupRel(int id);

    public boolean canSaveTitle(String title, int groupId, int weatherId);

    public boolean canSaveNewTitle(String title, int groupId);

    public boolean canSaveDefaultTitle(String title, int weatherId);
}
