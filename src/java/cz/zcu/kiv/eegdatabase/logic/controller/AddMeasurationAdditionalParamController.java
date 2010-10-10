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
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDef;

public class AddMeasurationAdditionalParamController extends SimpleFormController {

  private Log log = LogFactory.getLog(getClass());
  private AuthorizationManager auth;
  private GenericDao<ExperimentOptParamDef, Integer> experimentOptParamDefDao;

  public AddMeasurationAdditionalParamController() {
    setCommandClass(AddParameterCommand.class);
    setCommandName("addMeasurationAdditionalParam");
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
    ExperimentOptParamDef param = new ExperimentOptParamDef();
    param.setParamName(data.getParamName());
    param.setParamDataType(data.getParamDataType());

    log.debug("Saving data to database");
    experimentOptParamDefDao.create(param);

    log.debug("Returning MAV");
    return mav;
  }

  public GenericDao<ExperimentOptParamDef, Integer> getExperimentOptParamDefDao() {
    return experimentOptParamDefDao;
  }

  public void setExperimentOptParamDefDao(GenericDao<ExperimentOptParamDef, Integer> experimentOptParamDefDao) {
    this.experimentOptParamDefDao = experimentOptParamDefDao;
  }

  public AuthorizationManager getAuth() {
    return auth;
  }

  public void setAuth(AuthorizationManager auth) {
    this.auth = auth;
  }
}
