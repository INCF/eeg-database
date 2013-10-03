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
 *   AddPersonValidator.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.person;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.text.ParseException;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * @author JiPER
 */
public class AddPersonValidator implements Validator {

    private Log log = LogFactory.getLog(this.getClass());
    private PersonDao personDao;

    public boolean supports(Class clazz) {
        return clazz.equals(AddPersonCommand.class);
    }

    public void validate(Object command, Errors errors) {
        AddPersonCommand apc = (AddPersonCommand) command;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "givenname", "required.field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "surname", "required.field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dateOfBirth", "required.field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gender", "required.field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "required.field");

        try {
            Date d = ControllerUtils.getDateFormat().parse(apc.getDateOfBirth());
            if (d.getTime() >= System.currentTimeMillis()) {
                errors.rejectValue("dateOfBirth", "invalid.dateOfBirth");
            }
        } catch (ParseException e) {
            errors.rejectValue("dateOfBirth", "invalid.dateOfBirth");
        }
        if (apc.getNote().length() > 255) {
            errors.rejectValue("note", "invalid.maxLength");
        }
        if (!apc.getPhoneNumber().isEmpty()) {
            try {
                if (apc.getPhoneNumber().charAt(0) == '+') {
                    Long.parseLong(apc.getPhoneNumber().substring(1));
                } else {
                    Long.parseLong(apc.getPhoneNumber());
                }

            } catch (NumberFormatException ex) {
                errors.rejectValue("phoneNumber", "invalid.phoneNumber");
            }

        }
        if (!Pattern.matches("[a-zA-Z][a-zA-Z\\s]*", apc.getGivenname())) {
            errors.rejectValue("givenname", "invalid.givenname");
        }
        if (!Pattern.matches("[a-zA-Z][a-zA-Z\\s]*", apc.getSurname())) {
            errors.rejectValue("surname", "invalid.surname");
        }

        if (!Pattern.matches("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$", apc.getEmail())) {
            errors.rejectValue("email", "invalid.email");
        }
        if (!apc.getEmail().equals(apc.getOldEmail())) {
            if (personDao.usernameExists(apc.getEmail())) {
                errors.rejectValue("email", "inUse.email");
            }
        }
        apc.setOldEmail("");
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }
}
