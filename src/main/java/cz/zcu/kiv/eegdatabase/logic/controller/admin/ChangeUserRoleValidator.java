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
 *   ChangeUserRoleValidator.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.admin;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.logic.Util;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author Jindra
 */
public class ChangeUserRoleValidator implements Validator {

    private PersonDao personDao;

    public boolean supports(Class clazz) {
        return clazz.equals(ChangeUserRoleCommand.class);
    }

    public void validate(Object command, Errors errors) {
        ChangeUserRoleCommand data = (ChangeUserRoleCommand) command;

        String userName = data.getUserName();
        if (userName.trim().isEmpty()) {
            errors.rejectValue("userName", "required.field");
        } else if (!personDao.usernameExists(userName)) {
            errors.rejectValue("userName", "userNameDoesNotExist");
        }

        if (data.getUserRole().equals("-1")) {
            errors.rejectValue("userRole", "required.userRole");
        } else if (!((data.getUserRole().equals(Util.ROLE_ADMIN)) || (data.getUserRole().equals(Util.ROLE_USER)))) {
            errors.rejectValue("userRole", "invalid.userRole");
        }

    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }
}
