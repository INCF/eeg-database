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
 *   SearchHistoryController.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.controller.history;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.SimpleHistoryDao;
import cz.zcu.kiv.eegdatabase.data.pojo.History;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroupMembership;
import cz.zcu.kiv.eegdatabase.logic.controller.search.AbstractSearchController;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Prepared search results
 * using history searcher command for saving searching scenario(by scenario title), date interval (from date of download, to date of download)
 *
 * @author pbruha
 */
public class SearchHistoryController extends AbstractSearchController {

    private AuthorizationManager auth;
    private PersonDao personDao;
    private SimpleHistoryDao historyDao;

    public SearchHistoryController() {
        setCommandClass(HistorySearcherCommand.class);
        setCommandName("historySearcherCommand");
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        HistorySearcherCommand search = (HistorySearcherCommand) super.formBackingObject(request);
        return search;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        log.debug("Processing advanced search download history");
        ModelAndView mav = super.onSubmit(request, response, command);
        Person user = null;
        String authority = null;
        String roleAdmin = "ROLE_ADMIN";
        boolean isGroupAdmin;

        user = personDao.getPerson(ControllerUtils.getLoggedUserName());
        authority = user.getAuthority();
        isGroupAdmin = auth.userIsGroupAdmin();
        if (authority.equals(roleAdmin) || isGroupAdmin) {
            if (authority.equals(roleAdmin)) {
                isGroupAdmin = false;
            }
            Set<ResearchGroupMembership> rgm = user.getResearchGroupMemberships();
            List<Integer> groupsId = new ArrayList<Integer>();

            for (ResearchGroupMembership member : rgm) {
                if (member.getAuthority().equals("GROUP_ADMIN")) {
                    groupsId.add(member.getResearchGroup().getResearchGroupId());
                }
            }


            try {
                List<History> historyResults = historyDao.getHistorySearchResults(requests, isGroupAdmin, groupsId);
                mav.addObject("historyResults", historyResults);
                mav.addObject("resultsEmpty", historyResults.isEmpty());
            } catch (NumberFormatException e) {
                mav.addObject("mistake", "Number error");
                mav.addObject("error", true);
            } catch (RuntimeException e) {
                mav.addObject("mistake", e.getMessage());
                mav.addObject("error", true);
            }
            return mav;
        }
        mav.setViewName("system/accessDeniedNotAdmin");
        return mav;
    }

    public SimpleHistoryDao getHistoryDao() {
        return historyDao;
    }

    public void setHistoryDao(SimpleHistoryDao historyDao) {
        this.historyDao = historyDao;
    }

    public AuthorizationManager getAuth() {
        return auth;
    }

    public void setAuth(AuthorizationManager auth) {
        this.auth = auth;
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }
}
