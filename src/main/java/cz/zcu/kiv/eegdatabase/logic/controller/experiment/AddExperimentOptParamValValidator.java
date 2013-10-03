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
 *   AddExperimentOptParamValValidator.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.experiment;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.logic.controller.person.AddPersonAdditionalParamValueCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author František Liška
 */
public class AddExperimentOptParamValValidator implements Validator {
    @Autowired
    private AuthorizationManager auth;
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    @Qualifier("experimentOptParamValDao")
    private GenericDao<ExperimentOptParamVal, ExperimentOptParamValId> experimentOptParamValDao;

    public boolean supports(Class clazz) {
        return clazz.equals(AddExperimentOptParamValCommand.class);
    }

    public void validate(Object command, Errors errors) {
        AddExperimentOptParamValCommand data = (AddExperimentOptParamValCommand) command;

        if ((!auth.userIsOwnerOrCoexperimenter(data.getMeasurationFormId()))&&(!auth.isAdmin())) {
            // First check whether the user has permission to add data
            errors.reject("error.mustBeOwnerOfExperimentOrCoexperimenter");
        } else {

            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "paramValue", "required.field");

            if (data.getParamId() < 0) {
                errors.rejectValue("paramId", "required.field");
            }

            ExperimentOptParamVal val = experimentOptParamValDao.read(new ExperimentOptParamValId(data.getMeasurationFormId(), data.getParamId()));
            if (val != null) {  // field already exists
                errors.rejectValue("paramId", "invalid.paramIdAlreadyInserted");
            }

        }

    }

    public GenericDao<ExperimentOptParamVal, ExperimentOptParamValId> getExperimentOptParamValDao() {
        return experimentOptParamValDao;
    }

    public void setExperimentOptParamValDao(GenericDao<ExperimentOptParamVal, ExperimentOptParamValId> experimentOptParamValDao) {
        this.experimentOptParamValDao = experimentOptParamValDao;
    }

    public AuthorizationManager getAuth() {
        return auth;
    }

    public void setAuth(AuthorizationManager auth) {
        this.auth = auth;
    }
}
