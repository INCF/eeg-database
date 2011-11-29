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

    public boolean supports(Class clazz) {
        return clazz.equals(AddWeatherCommand.class);
    }

    public void validate(Object command, Errors errors) {
        AddWeatherCommand data = (AddWeatherCommand) command;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "required.field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "required.field");

        //TODO dodelat save
        //if (!weatherDao.canSaveTitle(data.getTitle(), data.getId())) {
        //    errors.rejectValue("title", "error.valueAlreadyInDatabase");
        //}
        //if (!weatherDao.canSaveDescription(data.getDescription(), data.getId())) {
        //    errors.rejectValue("description", "error.valueAlreadyInDatabase");
        //}
    }
}
