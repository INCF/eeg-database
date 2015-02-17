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
 *   SecurityFacade.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.security;

public interface SecurityFacade {

    boolean personAbleToWriteIntoGroup(int researchGroupId);

    boolean userIsExperimenter();

    boolean isAdmin();

    boolean userIsGroupAdmin();

    boolean userIsOwnerOrCoexperimenter(int experimentId);

    boolean userCanViewPersonDetails(int personId);

    boolean userIsOwnerOfScenario(int scenarioId);

    boolean userIsOwnerOrCoexpOfCorrespExperiment(int fileId);

    boolean userIsExperimenterInGroup(int groupId);

    boolean userIsAdminInGroup(int groupId);

    boolean userIsMemberInGroup(int groupId);

    boolean userCanEditPerson(int personId);

    boolean isAuthorizedToRequestGroupRole();

}
