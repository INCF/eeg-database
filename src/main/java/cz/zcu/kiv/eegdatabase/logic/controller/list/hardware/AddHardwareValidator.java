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
    private final int DEFAULT_ID = -1;

    public boolean supports(Class clazz) {
        return clazz.equals(AddHardwareCommand.class);
    }

    public void validate(Object command, Errors errors) {
        AddHardwareCommand data = (AddHardwareCommand) command;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "required.field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "type", "required.field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "required.field");

        if(data.getResearchGroupId()==DEFAULT_ID){
            if (!hardwareDao.canSaveDefaultTitle(data.getTitle(),data.getId())) {
                errors.rejectValue("title", "error.valueAlreadyInDatabase");
            }
        }else{
            if (!hardwareDao.canSaveTitle(data.getTitle(), data.getResearchGroupId(),data.getId())) {
                errors.rejectValue("title", "error.valueAlreadyInDatabase");
            }
        }
    }
}
