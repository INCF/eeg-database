package cz.zcu.kiv.eegdatabase.logic.controller.validation;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.logic.commandobjects.AddPersonAdditionalParameterCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamVal;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamValId;

/**
 *
 * @author JiPER
 */
public class AddPersonAdditionalParamValueValidator implements Validator {

  private Log log = LogFactory.getLog(getClass());
  private GenericDao<PersonOptParamVal, PersonOptParamValId> personOptParamValDao;

  public boolean supports(Class clazz) {
    return clazz.equals(AddPersonAdditionalParameterCommand.class);
  }

  public void validate(Object command, Errors errors) {
    AddPersonAdditionalParameterCommand data = (AddPersonAdditionalParameterCommand) command;

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
