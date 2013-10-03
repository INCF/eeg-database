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
 *   LoggedUserController.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.view.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.tiles.ComponentContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.view.tiles.ComponentControllerSupport;

/**
 *
 * @author JiPER
 */
public class LoggedUserController extends ComponentControllerSupport {

    @Override
    protected void doPerform(ComponentContext context, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = null;
        if (o instanceof UserDetails) {
            userName = ((UserDetails) o).getUsername();
        } else {
            userName = o.toString();
        }
        context.putAttribute("loggedUserName", userName);
    }

}
