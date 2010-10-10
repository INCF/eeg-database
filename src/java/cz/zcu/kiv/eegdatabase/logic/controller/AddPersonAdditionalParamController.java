package cz.zcu.kiv.eegdatabase.logic.controller;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import cz.zcu.kiv.eegdatabase.logic.commandobjects.AddParameterCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamDef;

public class AddPersonAdditionalParamController extends SimpleFormController {

  private Log log = LogFactory.getLog(getClass());
  private AuthorizationManager auth;
  private GenericDao<PersonOptParamDef, Integer> personOptParamDefDao;

  public AddPersonAdditionalParamController() {
    setCommandClass(AddParameterCommand.class);
    setCommandName("addPersonAdditionalParam");
  }

  @Override
  protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindException) throws Exception {
    ModelAndView mav = new ModelAndView(getSuccessView());

    log.debug("Processing form data.");
    AddParameterCommand data = (AddParameterCommand) command;

    if (!auth.userIsExperimenter()) {
      mav.setViewName("lists/userNotExperimenter");
    }

    log.debug("Creating new object");
    PersonOptParamDef param = new PersonOptParamDef();
    param.setParamName(data.getParamName());
    param.setParamDataType(data.getParamDataType());

    log.debug("Saving data to database");
    personOptParamDefDao.create(param);

    log.debug("Returning MAV");
    return mav;
  }

  public GenericDao<PersonOptParamDef, Integer> getPersonOptParamDefDao() {
    return personOptParamDefDao;
  }

  public void setPersonOptParamDefDao(GenericDao<PersonOptParamDef, Integer> personOptParamDefDao) {
    this.personOptParamDefDao = personOptParamDefDao;
  }

  public AuthorizationManager getAuth() {
    return auth;
  }

  public void setAuth(AuthorizationManager auth) {
    this.auth = auth;
  }
}
