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
 *   ExperimentsSearcherController.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.controller.search;

import cz.zcu.kiv.eegdatabase.data.dao.ExperimentDao;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.logic.util.SignalProcessingUtils;
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
public class ExperimentsSearcherController extends AbstractSearchController {

    private PersonDao personDao;
    private ExperimentDao experimentDao;
    private GenericDao<Scenario, Integer> scenarioDao;
    private GenericDao<Hardware, Integer> hardwareDao;

    public ExperimentsSearcherController() {
        setCommandClass(ExperimentsSearcherCommand.class);
        setCommandName("experimentsSearcherCommand");
    }


    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {
        Map map = new HashMap<String, Object>();
        return map;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        logger.debug("Search experiments controller");
        ModelAndView mav = super.onSubmit(request, response, command);
        try {
            List<Experiment> experimentResults = experimentDao.getExperimentSearchResults(requests, personDao.getLoggedPerson().getPersonId());
            SignalProcessingUtils.splitExperimentToView(mav, experimentResults);
            mav.addObject("resultsEmpty", experimentResults.isEmpty());

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

    public GenericDao<Scenario, Integer> getScenarioDao() {
        return scenarioDao;
    }

    public void setScenarioDao(GenericDao<Scenario, Integer> scenarioDao) {
        this.scenarioDao = scenarioDao;
    }

    public ExperimentDao getExperimentDao() {
        return experimentDao;
    }

    public void setExperimentDao(ExperimentDao experimentDao) {
        this.experimentDao = experimentDao;
    }

    public GenericDao<Hardware, Integer> getHardwareDao() {
        return hardwareDao;
    }

    public void setHardwareDao(GenericDao<Hardware, Integer> hardwareDao) {
        this.hardwareDao = hardwareDao;
    }
}
