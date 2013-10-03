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
 *   AuthorizationManager.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;


/**
 * DAO for checking permissions of users.
 *
 * @author Jindra
 */
public interface AuthorizationManager {

    /**
     * Checks whether the logged user has permission to add experiment into
     * the research group defined by parameter.
     *
     * @param researchGroupId ID of the research group
     * @return <code>true</code> if user has permission, else <code>false</code>
     */
    public boolean personAbleToWriteIntoGroup(int researchGroupId);

    /**
     * Checks whether the logged user is member with GROUP_EXPERIMENTER or
     * GROUP_ADMIN role at least in one group.
     *
     * @return <code>true</code> when able to write data in some group, else
     *         <code>false</code>
     */
    public boolean userIsExperimenter();

    public boolean isAdmin();

    /**
     * Checks whether the logged user is member with
     * GROUP_ADMIN role at least in one group.
     *
     * @return <code>true</code> when able to write data in some group, else
     *         <code>false</code>
     */
    public boolean userIsGroupAdmin();

    /**
     * Checks whether the logged user is owner of or co-experimenter on experiment
     * specified by the experiment id.
     *
     * @param experimentId ID of experiment
     * @return
     */
    public boolean userIsOwnerOrCoexperimenter(int experimentId);

    /**
     * Checks whether the logged person is in the same group as the person specified
     * by person ID and whether the logged user is at least experimenter in that group.
     *
     * @param personId
     * @return
     */
    public boolean userCanViewPersonDetails(int personId);

    /**
     * Checks whether the logged person is owner of the scenario specified by scenarioId.
     *
     * @param scenarioId
     * @return
     */
    boolean userIsOwnerOfScenario(int scenarioId);

    public boolean userIsOwnerOrCoexpOfCorrespExperiment(int fileId);

    public boolean userIsExperimenterInGroup(int groupId);

    public boolean userIsAdminInGroup(int groupId);

    boolean userIsMemberInGroup(int groupId);

    boolean userCanEditPerson(int personId);
    
    boolean isAuthorizedToRequestGroupRole();
}
