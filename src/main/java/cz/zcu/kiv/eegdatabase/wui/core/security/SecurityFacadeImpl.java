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
 *   SecurityFacadeImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Security facade for working with some security and access policy. Login and logout action above session.
 * 
 * @author Jakub Rinkes
 *
 */
public class SecurityFacadeImpl implements SecurityFacade {
    
    protected Log log = LogFactory.getLog(getClass());

    SecurityService service;

    @Required
    public void setService(SecurityService service) {
        this.service = service;
    }

    @Override
    public boolean personAbleToWriteIntoGroup(int researchGroupId) {
        return service.personAbleToWriteIntoGroup(researchGroupId);
    }

    @Override
    public boolean userIsExperimenter() {
        return service.userIsExperimenter();
    }

    @Override
    public boolean isAdmin() {
        return service.isAdmin();
    }

    @Override
    public boolean userIsGroupAdmin() {
        return service.userIsGroupAdmin();
    }

    @Override
    public boolean userIsOwnerOrCoexperimenter(int experimentId) {
        return service.userIsOwnerOrCoexperimenter(experimentId);
    }

    @Override
    public boolean userCanViewPersonDetails(int personId) {
        return service.userCanViewPersonDetails(personId);
    }

    @Override
    public boolean userIsOwnerOfScenario(int scenarioId) {
        return service.userIsOwnerOfScenario(scenarioId);
    }

    @Override
    public boolean userIsOwnerOrCoexpOfCorrespExperiment(int fileId) {
        return service.userIsOwnerOrCoexpOfCorrespExperiment(fileId);
    }

    @Override
    public boolean userIsExperimenterInGroup(int groupId) {
        return service.userIsExperimenterInGroup(groupId);
    }

    @Override
    public boolean userIsAdminInGroup(int groupId) {
        return service.userIsAdminInGroup(groupId);
    }

    @Override
    public boolean userIsMemberInGroup(int groupId) {
        return service.userIsMemberInGroup(groupId);
    }

    @Override
    public boolean userCanEditPerson(int personId) {
        return service.userCanEditPerson(personId);
    }

    @Override
    public boolean isAuthorizedToRequestGroupRole() {
        return service.isAuthorizedToRequestGroupRole();
    }

}
