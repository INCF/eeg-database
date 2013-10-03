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
 *   ResearchGroupService.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.group;

import java.util.List;

import org.springframework.mail.MailException;

import cz.zcu.kiv.eegdatabase.data.pojo.GroupPermissionRequest;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroupMembership;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroupMembershipId;
import cz.zcu.kiv.eegdatabase.wui.core.GenericService;

public interface ResearchGroupService extends GenericService<ResearchGroup, Integer> {

    ResearchGroup getResearchGroupById(int id);

    List<ResearchGroup> getResearchGroupsWhereMember(Person person);

    List<ResearchGroup> getResearchGroupsWhereMember(Person person, int limit);

    List<ResearchGroup> getResearchGroupsWhereOwner(Person person);

    List<ResearchGroup> getResearchGroupsWhereAbleToWriteInto(Person person);

    String getResearchGroupTitle(int groupId);

    List<ResearchGroupAccountInfo> getGroupDataForAccountOverview(Person person);

    List getListOfGroupMembers(int groupId);

    List<ResearchGroup> getResearchGroupsWhereUserIsGroupAdmin(Person person);

    boolean canSaveTitle(String title, int id);

    int getCountForList();

    List<ResearchGroup> getGroupsForList(int start, int limit);

    // MEMBERSHIP
    ResearchGroupMembershipId createMemberhip(ResearchGroupMembership newInstance);

    ResearchGroupMembership readMemberhip(ResearchGroupMembershipId id);

    List<ResearchGroupMembership> readMemberhipByParameter(String parameterName, int parameterValue);

    List<ResearchGroupMembership> readMemberhipByParameter(String parameterName, String parameterValue);

    void updateMemberhip(ResearchGroupMembership transientObject);

    void deleteMemberhip(ResearchGroupMembership persistentObject);

    List<ResearchGroupMembership> getAllMemberhipRecords();

    List<ResearchGroupMembership> getMemberhipRecordsAtSides(int first, int max);

    int getCountMemberhipRecords();

    // GROUP ROLE REQUEST
    Integer createPermissionRequest(GroupPermissionRequest newInstance) throws MailException;

    GroupPermissionRequest readPermissionRequest(int id);

    List<GroupPermissionRequest> readPermissionRequestByParameter(String parameterName, int parameterValue);

    List<GroupPermissionRequest> readPermissionRequestByParameter(String parameterName, String parameterValue);

    void updatePermissionRequest(GroupPermissionRequest transientObject);

    void deletePermissionRequest(GroupPermissionRequest persistentObject);

    List<GroupPermissionRequest> getAllPermissionRequestRecords();

    List<GroupPermissionRequest> getPermissionRequestRecordsAtSides(int first, int max);

    int getCountPermissionRequestRecords();

    void grantGroupPermisson(GroupPermissionRequest request);

}
