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
 *   CreateGroupValidator.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.group;

import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class CreateGroupValidator implements Validator {
    @Autowired
    private ResearchGroupDao researchGroupDao;

    public boolean supports(Class aClass) {
        return aClass.equals(CreateGroupCommand.class);
    }

    public void validate(Object command, Errors errors) {
        CreateGroupCommand data = (CreateGroupCommand) command;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "researchGroupTitle", "required.field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "researchGroupDescription", "required.field");

        if (!researchGroupDao.canSaveTitle(data.getResearchGroupTitle(), data.getId())) {
            errors.rejectValue("researchGroupTitle", "error.valueAlreadyInDatabase");
        }
        if (data.getResearchGroupTitle().length() > 100) {
            errors.rejectValue("researchGroupTitle", "invalid.maxGroupTitleLength");
        }
        if (data.getResearchGroupDescription().length() > 250) {
            errors.rejectValue("researchGroupDescription", "invalid.maxGroupDescriptionLength");
        }
    }
}
