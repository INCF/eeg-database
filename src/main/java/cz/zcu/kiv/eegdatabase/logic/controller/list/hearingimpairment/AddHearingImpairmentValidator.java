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
    private final int DEFAULT_ID = -1;

    public boolean supports(Class clazz) {
        return clazz.equals(AddHearingImpairmentCommand.class);
    }

    public void validate(Object command, Errors errors) {
        AddHearingImpairmentCommand data = (AddHearingImpairmentCommand) command;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "required.field");

        if(data.getResearchGroupId()==DEFAULT_ID){
            if (!hearingImpairmentDao.canSaveDefaultDescription(data.getDescription(), data.getHearingImpairmentId())) {
                errors.rejectValue("description", "error.valueAlreadyInDatabase");
            }
        }else{
            if (!hearingImpairmentDao.canSaveDescription(data.getDescription(),data.getResearchGroupId(), data.getHearingImpairmentId())) {
                errors.rejectValue("description", "error.valueAlreadyInDatabase");
            }
        }
    }
}
