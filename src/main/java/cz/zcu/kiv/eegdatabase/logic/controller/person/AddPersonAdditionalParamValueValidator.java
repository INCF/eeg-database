package cz.zcu.kiv.eegdatabase.logic.controller.person;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamVal;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamValId;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author JiPER
 */
public class AddPersonAdditionalParamValueValidator implements Validator {

    private Log log = LogFactory.getLog(getClass());
    private GenericDao<PersonOptParamVal, PersonOptParamValId> personOptParamValDao;

    public boolean supports(Class clazz) {
        return clazz.equals(AddPersonAdditionalParamValueCommand.class);
    }

    public void validate(Object command, Errors errors) {
        AddPersonAdditionalParamValueCommand data = (AddPersonAdditionalParamValueCommand) command;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "paramValue", "required.field");

        if (data.getParamId() < 0) {
            errors.rejectValue("paramId", "required.field");
        }

        PersonOptParamVal val = personOptParamValDao.read(new PersonOptParamValId(data.getPersonFormId(), data.getParamId()));
        if (val != null) {  // field already exists
            errors.rejectValue("paramId", "invalid.paramIdAlreadyInserted");
        }
    }

    public GenericDao<PersonOptParamVal, PersonOptParamValId> getPersonOptParamValDao() {
        return personOptParamValDao;
    }

    public void setPersonOptParamValDao(GenericDao<PersonOptParamVal, PersonOptParamValId> personOptParamValDao) {
        this.personOptParamValDao = personOptParamValDao;
    }
}
