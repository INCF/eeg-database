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
 *   WeatherFacade.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.common;

import java.util.List;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import cz.zcu.kiv.eegdatabase.data.pojo.WeatherGroupRel;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacade;

public interface WeatherFacade extends GenericFacade<Weather, Integer> {

    List<Weather> getItemsForList();

    boolean canSaveDescription(String description, int groupId, int weatherId);

    boolean canSaveDefaultDescription(String description, int weatherId);

    boolean canDelete(int id);

    boolean canSaveNewDescription(String description, int groupId);

    void createDefaultRecord(Weather weather);

    void createGroupRel(WeatherGroupRel weatherGroupRel);

    void createGroupRel(Weather weather, ResearchGroup researchGroup);

    List<Weather> getRecordsByGroup(int groupId);

    List<Weather> getDefaultRecords();

    boolean isDefault(int id);

    void deleteGroupRel(WeatherGroupRel weatherGroupRel);

    WeatherGroupRel getGroupRel(int weatherId, int researchGroupId);

    boolean hasGroupRel(int id);

    boolean canSaveTitle(String title, int groupId, int weatherId);

    boolean canSaveNewTitle(String title, int groupId);

    boolean canSaveDefaultTitle(String title, int weatherId);
}
