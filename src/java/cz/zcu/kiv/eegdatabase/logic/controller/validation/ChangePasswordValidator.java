package cz.zcu.kiv.eegdatabase.logic.controller.validation;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.logic.commandobjects.MyAccountCommand;
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
public class ChangePasswordValidator implements Validator {

  private Log log = LogFactory.getLog(getClass());
  private PersonDao personDao;

  public boolean supports(Class clazz) {
    return clazz.equals(MyAccountCommand.class);
  }

  public void validate(Object command, Errors errors) {
    log.debug("Started validation of My account form");
    MyAccountCommand myAccountCommand = (MyAccountCommand) command;

    log.debug("Validating old password");
    String oldPasswordHash = ControllerUtils.getMD5String(myAccountCommand.getOldPassword());
    Person user = personDao.getPerson(ControllerUtils.getLoggedUserName());
    log.debug("Matching inserted old password [" + oldPasswordHash + "] and actual password in database [" + user.getPassword() + "]");
    if (!user.getPassword().equals(oldPasswordHash)) {
      log.debug("Inserted password REJECTED");
      errors.rejectValue("oldPassword", "invalid.oldPassword");
    }

    log.debug("Validation whether the new password differs from the old one");
    if (myAccountCommand.getNewPassword().equals(myAccountCommand.getOldPassword())) {
      log.debug("New and old passwords are the same");
      errors.rejectValue("newPassword", "invalid.newAndOldPasswordsAreTheSame");
    }

    log.debug("Validating new password if it is not empty");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPassword", "required.field");

    log.debug("Validating twice inserted new password");
    if (!myAccountCommand.getNewPassword().equals(myAccountCommand.getNewPassword2())) {
      log.debug("New passwords don't match");
      errors.rejectValue("newPassword2", "invalid.passwordMatch");
    }
  }

  public PersonDao getPersonDao() {
    return personDao;
  }

  public void setPersonDao(PersonDao personDao) {
    this.personDao = personDao;
  }
}
