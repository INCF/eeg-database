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
 *   ResearchGroupDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupAccountInfo;

import java.util.List;

/**
 * Data Access Object for accessing ResearchGroup entities. The interface will use
 * only one specific couple of classes, so it doesn't need to be generic.
 *
 * @author Jindrich Pergler
 */
public interface ResearchGroupDao extends GenericDao<ResearchGroup, Integer> {

    public List<ResearchGroup> getResearchGroupsWhereMember(Person person);

    public List<ResearchGroup> getResearchGroupsWhereMember(Person person, int limit);

    public List<ResearchGroup> getResearchGroupsWhereOwner(Person person);

    public List<ResearchGroup> getResearchGroupsWhereAbleToWriteInto(Person person);

    public String getResearchGroupTitle(int groupId);

    public List<ResearchGroupAccountInfo> getGroupDataForAccountOverview(Person person);

    public List getListOfGroupMembers(int groupId);

    public List<ResearchGroup> getResearchGroupsWhereUserIsGroupAdmin(Person person);

    public boolean canSaveTitle(String title, int id);

    public int getCountForList();

    List<ResearchGroup> getGroupsForList(int start, int limit);
    
    ResearchGroup getResearchGroupById(int id);
}
