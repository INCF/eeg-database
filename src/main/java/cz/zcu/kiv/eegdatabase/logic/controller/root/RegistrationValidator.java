package cz.zcu.kiv.eegdatabase.logic.controller.root;

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
public class RegistrationValidator implements Validator {

    private Log log = LogFactory.getLog(getClass());
    private PersonDao personDao;

    public boolean supports(Class clazz) {
        return clazz.equals(RegistrationCommand.class);
    }

    public void validate(Object command, Errors errors) {
        RegistrationCommand registrationCommand = (RegistrationCommand) command;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "givenname", "required.field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "surname", "required.field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dateOfBirth", "required.field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gender", "required.field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "required.field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "required.field");

        try {
            Date d = ControllerUtils.getDateFormat().parse(registrationCommand.getDateOfBirth());
            if (d.getTime() >= System.currentTimeMillis()) {
                errors.rejectValue("dateOfBirth", "invalid.dateOfBirth");
            }
        } catch (ParseException e) {
            errors.rejectValue("dateOfBirth", "invalid.dateOfBirth");
        }

        if (!registrationCommand.getPassword().equals(registrationCommand.getPassword2())) {
            errors.rejectValue("password2", "invalid.passwordMatch");
        }

        if (!Pattern.matches("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$", registrationCommand.getEmail())) {
            errors.rejectValue("email", "invalid.email");
        }

        if (personDao.usernameExists(registrationCommand.getEmail())) {
            errors.rejectValue("email", "inUse.email");
        }
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }
}
