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
 *   SecurityServiceImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;

public class SecurityServiceImpl implements SecurityService {
    
    protected Log log = LogFactory.getLog(getClass());

    AuthorizationManager manager;

    @Required
    public void setManager(AuthorizationManager manager) {
        this.manager = manager;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean personAbleToWriteIntoGroup(int researchGroupId) {
        return manager.personAbleToWriteIntoGroup(researchGroupId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userIsExperimenter() {
        return manager.userIsExperimenter();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isAdmin() {
        return manager.isAdmin();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userIsGroupAdmin() {
        return manager.userIsGroupAdmin();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userIsOwnerOrCoexperimenter(int experimentId) {
        return manager.userIsOwnerOrCoexperimenter(experimentId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userCanViewPersonDetails(int personId) {
        return manager.userCanViewPersonDetails(personId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userIsOwnerOfScenario(int scenarioId) {
        return manager.userIsOwnerOfScenario(scenarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userIsOwnerOrCoexpOfCorrespExperiment(int fileId) {
        return manager.userIsOwnerOrCoexpOfCorrespExperiment(fileId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userIsExperimenterInGroup(int groupId) {
        return manager.userIsExperimenterInGroup(groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userIsAdminInGroup(int groupId) {
        return manager.userIsAdminInGroup(groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userIsMemberInGroup(int groupId) {
        return manager.userIsMemberInGroup(groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userCanEditPerson(int personId) {
        return manager.userCanEditPerson(personId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isAuthorizedToRequestGroupRole() {
        return manager.isAuthorizedToRequestGroupRole();
    }

}
