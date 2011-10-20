package cz.zcu.kiv.eegdatabase.logic.controller.list.filemetadata;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author JiPER
 */
public class AddFileMetadataParamDefValidator implements Validator {

    private Log log = LogFactory.getLog(getClass());

    public boolean supports(Class clazz) {
        return clazz.equals(AddFileMetadataParamDefCommand.class);
    }

    public void validate(Object command, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "paramName", "required.field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "paramDataType", "required.field");
    }
}
