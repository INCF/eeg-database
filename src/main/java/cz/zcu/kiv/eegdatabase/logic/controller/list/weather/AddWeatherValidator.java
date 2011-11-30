package cz.zcu.kiv.eegdatabase.logic.controller.list.weather;

import cz.zcu.kiv.eegdatabase.data.dao.WeatherDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author JiPER
 */
public class AddWeatherValidator implements Validator {

    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private WeatherDao weatherDao;
    private final int DEFAULT_ID = -1;

    public boolean supports(Class clazz) {
        return clazz.equals(AddWeatherCommand.class);
    }

    public void validate(Object command, Errors errors) {
        AddWeatherCommand data = (AddWeatherCommand) command;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "required.field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "required.field");

        if(data.getResearchGroupId()==DEFAULT_ID){
            if (!weatherDao.canSaveDefaultTitle(data.getTitle(),data.getId())) {
                errors.rejectValue("title", "error.valueAlreadyInDatabase");
            }
            if (!weatherDao.canSaveDefaultDescription(data.getDescription(),data.getId())){
                errors.rejectValue("description", "error.valueAlreadyInDatabase");
            }
        }else{
            if (!weatherDao.canSaveTitle(data.getTitle(), data.getResearchGroupId(),data.getId())) {
                errors.rejectValue("title", "error.valueAlreadyInDatabase");
            }
            if (!weatherDao.canSaveDescription(data.getTitle(), data.getResearchGroupId(),data.getId())) {
                errors.rejectValue("description", "error.valueAlreadyInDatabase");
            }
        }

    }
}
