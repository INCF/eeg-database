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
 *   AddScenarioValidator.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.scenario;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.ScenarioDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author JiPER
 */
public class AddScenarioValidator implements Validator {

    private Log log = LogFactory.getLog(getClass());
    private AuthorizationManager auth;
    @Autowired
    private ScenarioDao scenarioDao;

    public boolean supports(Class clazz) {
        return clazz.equals(AddScenarioCommand.class);
    }

    public void validate(Object command, Errors errors) {
        log.debug("Validating scenario form");
        AddScenarioCommand data = (AddScenarioCommand) command;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "required.field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "length", "required.field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "required.field");

        if (data.getResearchGroup() == -1) {
            // research group not chosen
            errors.rejectValue("researchGroup", "required.researchGroup");
        }
// else if (!auth.personAbleToWriteIntoGroup(data.getResearchGroup())) {
//            errors.rejectValue("researchGroup", "invalid.notAbleToAddExperimentInGroup");
//        }

        try {
            int len = Integer.parseInt(data.getLength());
            if (len <= 0) {
                errors.rejectValue("length", "invalid.lengthValue");
            }
        } catch (NumberFormatException ex) {
            errors.rejectValue("length", "invalid.scenarioLength");
            log.debug("Scenario length is not in parseable format!");
        }
        int len = data.getDescription().length();
        if (len > 255) {
            errors.rejectValue("description", "invalid.maxScenLen");
        }

        if (!scenarioDao.canSaveTitle(data.getTitle(), data.getId())) {
            errors.rejectValue("title", "error.valueAlreadyInDatabase");
        }

        if ((!(data.getId() > 0)) && (data.isDataFileAvailable())
                && ((!data.isXmlFileCheckBox()))) {
            if (data.getDataFile().isEmpty()) {
            // Creating new scenario and no file was uploaded
            errors.rejectValue("dataFile", "required.dataFile");
            log.debug("No data file was inserted!");
            }
        }

        if ((!(data.getId() > 0)) && (data.isDataFileAvailable())
                && (data.isXmlFileCheckBox())) {
            if (data.getDataFileXml().isEmpty()) {
            // Creating new scenario and no file was uploaded
            errors.rejectValue("dataFileXml", "required.dataFileXml");
            log.debug("No XML data file was inserted!");
            }
        }

        if (data.isXmlFileCheckBox()) {
            if (data.getScenarioSchema() == 0 && (data.getScenarioOption().equals("fromList"))) {
                //scenario schema name is not chosen
                errors.rejectValue("scenarioSchema", "required.scenarioSchema");
                log.debug("No scenario schema was selected!");
            }
        }
    }

    public AuthorizationManager getAuth() {
        return auth;
    }

    public void setAuth(AuthorizationManager auth) {
        this.auth = auth;
    }
}
