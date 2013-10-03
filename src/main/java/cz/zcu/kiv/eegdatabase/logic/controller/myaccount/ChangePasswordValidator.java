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
 *   ChangePasswordValidator.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.myaccount;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author JiPER
 */
public class ChangePasswordValidator implements Validator {

    private Log log = LogFactory.getLog(getClass());
    private PersonDao personDao;
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public boolean supports(Class clazz) {
        return clazz.equals(ChangePasswordCommand.class);
    }

    public void validate(Object command, Errors errors) {
        log.debug("Started validation of My account form");
        ChangePasswordCommand changePasswordCommand = (ChangePasswordCommand) command;

        Person user = personDao.getPerson(ControllerUtils.getLoggedUserName());
        log.debug("Matching inserted old password and actual password in database [" + user.getPassword() + "]");
        if (!encoder.matches(changePasswordCommand.getOldPassword(),user.getPassword())) {
            log.debug("Inserted password REJECTED");
            errors.rejectValue("oldPassword", "invalid.oldPassword");
        }

        log.debug("Validation whether the new password differs from the old one");
        if (changePasswordCommand.getNewPassword().equals(changePasswordCommand.getOldPassword())) {
            log.debug("New and old passwords are the same");
            errors.rejectValue("newPassword", "invalid.newAndOldPasswordsAreTheSame");
        }

        log.debug("Validating new password if it is not empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPassword", "required.field");

        log.debug("Validating twice inserted new password");
        if (!changePasswordCommand.getNewPassword().equals(changePasswordCommand.getNewPassword2())) {
            log.debug("New passwords don't match");
            errors.rejectValue("newPassword2", "invalid.passwordMatch");
        }

        if (changePasswordCommand.getNewPassword().length()< ControllerUtils.MINIMUM_PASSWORD_LENGTH) {
            errors.rejectValue("newPassword", "invalid.minimumPasswordLength6");
        }
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }
}
