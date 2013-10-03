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
 *   ScenarioSearcherController.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.logic.controller.search;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ScenarioDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pbruha
 */
public class ScenarioSearcherController extends AbstractSearchController {
    private PersonDao personDao;
    private ScenarioDao scenarioDao;

    public ScenarioSearcherController() {
        setCommandClass(ScenarioSearcherCommand.class);
        setCommandName("scenarioSearcherCommand");
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        ScenarioSearcherCommand search = (ScenarioSearcherCommand) super.formBackingObject(request);
        return search;
    }

    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {
        Map map = new HashMap<String, Object>();
        return map;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        ModelAndView mav = null;
        try {
            mav = super.onSubmit(request, response, command);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {

            List<Scenario> scenarioResults = scenarioDao.getScenarioSearchResults
                    (requests, personDao.getLoggedPerson().getPersonId());
            mav.addObject("scenarioResults", scenarioResults);
            mav.addObject("resultsEmpty", scenarioResults.isEmpty());
        } catch (NumberFormatException e) {
            mav.addObject("mistake", "Number error");
            mav.addObject("error", true);
        } catch (RuntimeException e) {
            mav.addObject("mistake", e.getMessage());
            mav.addObject("error", true);
        }

        return mav;
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    public ScenarioDao getScenarioDao() {
        return scenarioDao;
    }

    public void setScenarioDao(ScenarioDao scenarioDao) {
        this.scenarioDao = scenarioDao;
    }

}
