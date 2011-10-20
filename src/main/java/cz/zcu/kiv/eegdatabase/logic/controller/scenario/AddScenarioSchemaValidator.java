package cz.zcu.kiv.eegdatabase.logic.controller.scenario;

import cz.zcu.kiv.eegdatabase.data.dao.ScenarioSchemasDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by IntelliJ IDEA.
 * User: Toshiba
 * Date: 8.6.11
 * Time: 12:55
 * To change this template use File | Settings | File Templates.
 */
public class AddScenarioSchemaValidator implements Validator {

    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private ScenarioSchemasDao scenarioSchemasDao;

    public boolean supports(Class clazz) {
        return clazz.equals(AddScenarioSchemaCommand.class);
    }

    public void validate(Object command, Errors errors) {
        log.debug("Validating scenario schema form");
        AddScenarioSchemaCommand data = (AddScenarioSchemaCommand) command;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "schemaDescription", "required.field");

        int len = data.getSchemaDescription().length();
        if (len > 2000) {
            errors.rejectValue("schemaDescription", "invalid.maxScenSchemaLen");
        }

        if ((!(data.getId() > 0)) && (data.getSchemaFile().isEmpty())
                && (data.getSchemaFile().isEmpty())) {
            // Creating new scenario and no file was uploaded
            errors.rejectValue("schemaFile", "required.dataFileXsd");
            log.debug("No data file was inserted!");
        }
    }
}
