package cz.zcu.kiv.eegdatabase.logic.controller.list.hearingimpairment;

import cz.zcu.kiv.eegdatabase.data.dao.HearingImpairmentDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author JiPER
 */
public class AddHearingImpairmentValidator implements Validator {

    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private HearingImpairmentDao hearingImpairmentDao;

    public boolean supports(Class clazz) {
        return clazz.equals(AddHearingImpairmentCommand.class);
    }

    public void validate(Object command, Errors errors) {
        AddHearingImpairmentCommand data = (AddHearingImpairmentCommand) command;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "required.field");

        if (!hearingImpairmentDao.canSaveDescription(data.getDescription(), data.getHearingImpairmentId())) {
            errors.rejectValue("description", "error.valueAlreadyInDatabase");
        }
    }
}
