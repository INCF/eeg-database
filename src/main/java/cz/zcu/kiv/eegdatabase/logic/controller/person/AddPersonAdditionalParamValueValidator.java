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
 *   AddPersonAdditionalParamValueValidator.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.person;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamVal;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamValId;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author JiPER
 */
public class AddPersonAdditionalParamValueValidator implements Validator {

    private Log log = LogFactory.getLog(getClass());
    private GenericDao<PersonOptParamVal, PersonOptParamValId> personOptParamValDao;

    public boolean supports(Class clazz) {
        return clazz.equals(AddPersonAdditionalParamValueCommand.class);
    }

    public void validate(Object command, Errors errors) {
        AddPersonAdditionalParamValueCommand data = (AddPersonAdditionalParamValueCommand) command;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "paramValue", "required.field");

        if (data.getParamId() < 0) {
            errors.rejectValue("paramId", "required.field");
        }

        PersonOptParamVal val = personOptParamValDao.read(new PersonOptParamValId(data.getPersonFormId(), data.getParamId()));
        if (val != null) {  // field already exists
            errors.rejectValue("paramId", "invalid.paramIdAlreadyInserted");
        }
    }

    public GenericDao<PersonOptParamVal, PersonOptParamValId> getPersonOptParamValDao() {
        return personOptParamValDao;
    }

    public void setPersonOptParamValDao(GenericDao<PersonOptParamVal, PersonOptParamValId> personOptParamValDao) {
        this.personOptParamValDao = personOptParamValDao;
    }
}
