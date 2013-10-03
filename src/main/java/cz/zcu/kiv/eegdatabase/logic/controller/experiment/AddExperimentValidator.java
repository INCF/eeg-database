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
 *   AddExperimentValidator.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.experiment;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamVal;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamValId;
import cz.zcu.kiv.eegdatabase.logic.controller.person.AddPersonAdditionalParamValueCommand;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.text.ParseException;

/**
 * @author František Liška
 */
public class AddExperimentValidator implements Validator {
    @Autowired
    private AuthorizationManager auth;
    private Log log = LogFactory.getLog(getClass());

    public boolean supports(Class clazz) {
        return clazz.equals(AddExperimentCommand.class);
    }

    public void validate(Object command, Errors errors) {
        AddExperimentCommand data = (AddExperimentCommand) command;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "startDate", "required.date");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "startTime", "required.time");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "endDate", "required.date");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "endTime", "required.time");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "temperature", "required.field");

        if (data.getMeasurationId() > 0) {
            // Edit of existing experiment
            // No special actions yet
        } else {
            // Creating new experiment
            if (data.getResearchGroup() == -1) {
                // research group not chosen
                errors.rejectValue("researchGroup", "required.researchGroup");
            } else if (!auth.personAbleToWriteIntoGroup(data.getResearchGroup())) {
                errors.rejectValue("researchGroup", "invalid.notAbleToAddExperimentInGroup");
            }
        }

        if (data.getSubjectPerson() == -1) {  // measured person not chosen
            errors.rejectValue("subjectPerson", "required.subjectPerson");
        }

        if (data.getScenario() == -1) {  // scenario not selected
            errors.rejectValue("scenario", "required.scenario");
        }

        if (data.getHardware().length == 0) {  // no hardware selected
            errors.rejectValue("hardware", "required.hardware");
        }

        if (data.getWeather() == -1) {  // weather not selected
            errors.rejectValue("weather", "required.weather");
        }

        try {
            ControllerUtils.getDateFormat().parse(data.getStartDate());
        } catch (ParseException ex) {
            errors.rejectValue("startDate", "invalid.date");
        }

        try {
            ControllerUtils.getDateFormat().parse(data.getEndDate());
        } catch (ParseException ex) {
            errors.rejectValue("endDate", "invalid.date");
        }

        try {
            ControllerUtils.getTimeFormat().parse(data.getStartTime());
        } catch (ParseException ex) {
            errors.rejectValue("startTime", "invalid.time");
        }

        try {
            ControllerUtils.getTimeFormat().parse(data.getEndTime());
        } catch (ParseException ex) {
            errors.rejectValue("endTime", "invalid.time");
        }

        try {
            int temp = Integer.parseInt(data.getTemperature());
            if (temp < -273) {
                errors.rejectValue("temperature", "invalid.minTemp");
            }
        } catch (NumberFormatException e) {
            errors.rejectValue("temperature", "invalid.temperature");
        }

    }

    public AuthorizationManager getAuth() {
        return auth;
    }

    public void setAuth(AuthorizationManager auth) {
        this.auth = auth;
    }
}
