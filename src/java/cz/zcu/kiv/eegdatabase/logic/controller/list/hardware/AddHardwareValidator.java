package cz.zcu.kiv.eegdatabase.logic.controller.list.hardware;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author JiPER
 */
public class AddHardwareValidator implements Validator {

    private Log log = LogFactory.getLog(getClass());

    public boolean supports(Class clazz) {
        return clazz.equals(AddHardwareCommand.class);
    }

    public void validate(Object command, Errors errors) {
        AddHardwareCommand data = (AddHardwareCommand) command;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "required.field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "type", "required.field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "required.field");
    }
}
