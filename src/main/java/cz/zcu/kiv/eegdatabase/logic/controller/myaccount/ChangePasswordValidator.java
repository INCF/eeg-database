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
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }
}
