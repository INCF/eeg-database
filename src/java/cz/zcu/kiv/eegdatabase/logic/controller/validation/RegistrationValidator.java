package cz.zcu.kiv.eegdatabase.logic.controller.validation;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import java.text.ParseException;
import java.util.regex.Pattern;
import cz.zcu.kiv.eegdatabase.logic.commandobjects.RegistrationCommand;
import cz.zcu.kiv.eegdatabase.logic.controller.util.ControllerUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;

/**
 *
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
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "required.field");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "required.field");

    try {
      ControllerUtils.getDateFormat().parse(registrationCommand.getDateOfBirth());
    } catch (ParseException e) {
      errors.rejectValue("dateOfBirth", "invalid.dateOfBirth");
    }

    if (!registrationCommand.getPassword().equals(registrationCommand.getPassword2())) {
      errors.rejectValue("password2", "invalid.passwordMatch");
    }

    if (!Pattern.matches("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$", registrationCommand.getEmail())) {
      errors.rejectValue("email", "invalid.email");
    }

    if (!Pattern.matches("^[a-z][a-z0-9.-]+$", registrationCommand.getUsername())) {
      errors.rejectValue("username", "invalid.username");
    }

    if (personDao.usernameExists(registrationCommand.getUsername())) {
      errors.rejectValue("username", "inUse.username");
    }


    


  }

  public PersonDao getPersonDao() {
    return personDao;
  }

  public void setPersonDao(PersonDao personDao) {
    this.personDao = personDao;
  }
}
