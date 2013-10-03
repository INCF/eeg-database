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
 *   AddHardwareValidator.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.list.hardware;

import cz.zcu.kiv.eegdatabase.data.dao.HardwareDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author JiPER
 */
public class AddHardwareValidator implements Validator {

    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private HardwareDao hardwareDao;
    private final int DEFAULT_ID = -1;

    public boolean supports(Class clazz) {
        return clazz.equals(AddHardwareCommand.class);
    }

    public void validate(Object command, Errors errors) {
        AddHardwareCommand data = (AddHardwareCommand) command;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "required.field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "type", "required.field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "required.field");

        if(data.getResearchGroupId()==DEFAULT_ID){
            if (!hardwareDao.canSaveDefaultTitle(data.getTitle(),data.getId())) {
                errors.rejectValue("title", "error.valueAlreadyInDatabase");
            }
        }else{
            if (!hardwareDao.canSaveTitle(data.getTitle(), data.getResearchGroupId(),data.getId())) {
                errors.rejectValue("title", "error.valueAlreadyInDatabase");
            }
        }
    }
}
