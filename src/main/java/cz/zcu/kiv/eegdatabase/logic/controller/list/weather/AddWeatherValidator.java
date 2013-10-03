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
 *   AddWeatherValidator.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.list.weather;

import cz.zcu.kiv.eegdatabase.data.dao.WeatherDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author JiPER
 */
public class AddWeatherValidator implements Validator {

    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private WeatherDao weatherDao;
    private final int DEFAULT_ID = -1;

    public boolean supports(Class clazz) {
        return clazz.equals(AddWeatherCommand.class);
    }

    public void validate(Object command, Errors errors) {
        AddWeatherCommand data = (AddWeatherCommand) command;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "required.field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "required.field");

        if (data.getTitle().length() > 30) {
            errors.rejectValue("title", "invalid.fieldLength30");
        }
        if (data.getDescription().length() > 30) {
                    errors.rejectValue("description", "invalid.fieldLength30");
                }

        if(data.getResearchGroupId()==DEFAULT_ID){
            if (!weatherDao.canSaveDefaultTitle(data.getTitle(),data.getId())) {
                errors.rejectValue("title", "error.valueAlreadyInDatabase");
            }
            if (!weatherDao.canSaveDefaultDescription(data.getDescription(),data.getId())){
                errors.rejectValue("description", "error.valueAlreadyInDatabase");
            }
        }else{
            if (!weatherDao.canSaveTitle(data.getTitle(), data.getResearchGroupId(),data.getId())) {
                errors.rejectValue("title", "error.valueAlreadyInDatabase");
            }
            if (!weatherDao.canSaveDescription(data.getTitle(), data.getResearchGroupId(),data.getId())) {
                errors.rejectValue("description", "error.valueAlreadyInDatabase");
            }
        }

    }
}
