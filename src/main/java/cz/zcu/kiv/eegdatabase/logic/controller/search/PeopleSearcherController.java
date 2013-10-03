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
 *   PeopleSearcherController.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.logic.controller.search;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author pbruha
 */
public class PeopleSearcherController extends AbstractSearchController {

    private PersonDao personDao;
    @Autowired
    private AuthorizationManager auth;

    public PeopleSearcherController() {
        setCommandClass(PeopleSearcherCommand.class);
        setCommandName("peopleSearcherCommand");
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        PeopleSearcherCommand search = (PeopleSearcherCommand) super.formBackingObject(request);
        return search;
    }

    @Override
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
        ModelAndView mav = super.showForm(request, response, errors);
        boolean userIsExperimenter = auth.userIsExperimenter();
        mav.addObject("userIsExperimenter", userIsExperimenter);

        return mav;
    }


    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        logger.debug("Search people controller");
        ModelAndView mav = super.onSubmit(request, response, command);

        try {
            List<Person> personResults = personDao.getPersonSearchResults(requests);
            mav.addObject("personResults", personResults);
            mav.addObject("resultsEmpty", personResults.isEmpty());
        } catch (NumberFormatException e) {
            mav.addObject("mistake", "Number error");
            mav.addObject("error", true);
        } catch (RuntimeException e) {
            mav.addObject("mistake", e.getMessage());
            mav.addObject("error", true);
        }
        boolean userIsExperimenter = auth.userIsExperimenter();
        mav.addObject("userIsExperimenter", userIsExperimenter);
        return mav;
    }


    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }


}
