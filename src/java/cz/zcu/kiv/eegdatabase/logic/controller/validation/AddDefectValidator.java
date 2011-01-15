package cz.zcu.kiv.eegdatabase.logic.controller.validation;

import cz.zcu.kiv.eegdatabase.logic.commandobjects.AddDefectCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 *
 * @author JiPER
 */
public class AddDefectValidator implements Validator {

    private Log log = LogFactory.getLog(getClass());

    public boolean supports(Class clazz) {
        return clazz.equals(AddDefectCommand.class);
    }

    public void validate(Object command, Errors errors) {
        AddDefectCommand data = (AddDefectCommand) command;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "required.field");
    }
}
