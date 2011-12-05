package cz.zcu.kiv.eegdatabase.logic.controller.list.visualimpairment;

import cz.zcu.kiv.eegdatabase.data.dao.VisualImpairmentDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author JiPER
 */
public class AddVisualImpairmentValidator implements Validator {

    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private VisualImpairmentDao visualImpairmentDao;
    private final int DEFAULT_ID = -1;

    public boolean supports(Class clazz) {
        return clazz.equals(AddVisualImpairmentCommand.class);
    }

    public void validate(Object command, Errors errors) {
        AddVisualImpairmentCommand data = (AddVisualImpairmentCommand) command;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "required.field");

        if(data.getResearchGroupId()==DEFAULT_ID){
            if (!visualImpairmentDao.canSaveDefaultDescription(data.getDescription(), data.getVisualImpairmentId())) {
                errors.rejectValue("description", "error.valueAlreadyInDatabase");
            }
        }else{
            if (!visualImpairmentDao.canSaveDescription(data.getDescription(),data.getResearchGroupId(), data.getVisualImpairmentId())) {
                errors.rejectValue("description", "error.valueAlreadyInDatabase");
            }
        }
    }
}
