package cz.zcu.kiv.eegdatabase.logic.controller.validation;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.logic.commandobjects.ChangeUserRoleCommand;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
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
