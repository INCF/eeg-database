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
 *   ResearchGroupServiceController.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.webservices.rest.groups;

import cz.zcu.kiv.eegdatabase.webservices.rest.groups.wrappers.ResearchGroupDataList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling requests upon Research group service.
 *
 * @author Petr Miko
 */
@Secured("IS_AUTHENTICATED_FULLY")
@Controller
@RequestMapping("groups")
public class ResearchGroupServiceController {

    @Autowired
    private ResearchGroupService groupService;

    /**
     * Getter of all research groups.
     *
     * @return list container of research groups
     */
    @RequestMapping(value = "/all")
    public ResearchGroupDataList getAllGroups() {
        return new ResearchGroupDataList(groupService.getAllGroups());
    }

    /**
     * Getter of user's research groups.
     *
     * @return list container of research groups
     */
    @RequestMapping(value = "/mine")
    public ResearchGroupDataList getMyGroups() {
        return new ResearchGroupDataList(groupService.getMyGroups());
    }
}
