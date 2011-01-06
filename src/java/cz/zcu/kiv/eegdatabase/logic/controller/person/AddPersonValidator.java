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

    private Log log = LogFactory.getLog(getClass());
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
        if (!Pattern.matches("[a-zA-Zá-žÁ-Ž][a-zA-Zá-žÁ-Ž\\s]*", apc.getGivenname())) {
            errors.rejectValue("givenname", "invalid.givenname");
        }
        if (!Pattern.matches("[a-zA-Zá-žÁ-Ž][a-zA-Zá-žÁ-Ž\\s]*", apc.getSurname())) {
            errors.rejectValue("surname", "invalid.surname");
        }

        if (!Pattern.matches("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$", apc.getEmail())) {
            errors.rejectValue("email", "invalid.email");
        }
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }
}
