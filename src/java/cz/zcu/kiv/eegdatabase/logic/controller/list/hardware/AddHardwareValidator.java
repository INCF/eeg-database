package cz.zcu.kiv.eegdatabase.logic.controller.list.hardware;

import cz.zcu.kiv.eegdatabase.data.dao.HardwareDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author JiPER
 */
public class AddHardwareValidator implements Validator {

    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private HardwareDao hardwareDao;

    public boolean supports(Class clazz) {
        return clazz.equals(AddHardwareCommand.class);
    }

    public void validate(Object command, Errors errors) {
        AddHardwareCommand data = (AddHardwareCommand) command;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "required.field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "type", "required.field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "required.field");

        if (!hardwareDao.canSaveTitle(data.getTitle(), data.getId())) {
            errors.rejectValue("title", "error.valueAlreadyInDatabase");
        }
    }
}
