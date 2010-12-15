package cz.zcu.kiv.eegdatabase.logic.controller.list.visualimpairment;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author JiPER
 */
public class AddVisualImpairmentValidator implements Validator {

    private Log log = LogFactory.getLog(getClass());

    public boolean supports(Class clazz) {
        return clazz.equals(AddVisualImpairmentCommand.class);
    }

    public void validate(Object command, Errors errors) {
        AddVisualImpairmentCommand data = (AddVisualImpairmentCommand) command;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "required.field");
    }
}
