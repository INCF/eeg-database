package cz.zcu.kiv.eegdatabase.logic.controller.experiment;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamVal;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamValId;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDef;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class AddExperimentOptParamValController
        extends SimpleFormController
        implements Validator {

  private Log log = LogFactory.getLog(getClass());
  private AuthorizationManager auth;
  private GenericDao<Experiment, Integer> experimentDao;
  private GenericDao<ExperimentOptParamVal, ExperimentOptParamValId> experimentOptParamValDao;
  private GenericDao<ExperimentOptParamDef, Integer> experimentOptParamDefDao;

  public AddExperimentOptParamValController() {
    setCommandClass(AddExperimentOptParamValCommand.class);
    setCommandName("addMeasurationAdditionalParameter");
  }

  @Override
  protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
    ModelAndView mav = super.showForm(request, response, errors);

    mav.addObject("userIsExperimenter", auth.userIsExperimenter());
    return mav;
  }

  @Override
  protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
    log.debug("Preparing data for form");
    Map map = new HashMap<String, Object>();

    log.debug("Loading measuration info");
    int measurationId = Integer.parseInt(request.getParameter("experimentId"));
    Experiment measuration = experimentDao.read(measurationId);
    map.put("measurationDetail", measuration);

    log.debug("Loading parameter list for select box");
    List<ExperimentOptParamDef> list = experimentOptParamDefDao.getAllRecords();
    map.put("measurationAdditionalParams", list);

    log.debug("Returning map object");
    return map;
  }

  @Override
  protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindException) throws Exception {
    log.debug("Processing form data.");
    AddExperimentOptParamValCommand data = (AddExperimentOptParamValCommand) command;

    log.debug("Creating new object");
    ExperimentOptParamVal val = new ExperimentOptParamVal();
    val.setId(new ExperimentOptParamValId(data.getMeasurationFormId(), data.getParamId()));
    val.setParamValue(data.getParamValue());

    log.debug("Saving object to database");
    experimentOptParamValDao.create(val);

    log.debug("Returning MAV");
    ModelAndView mav = new ModelAndView(getSuccessView() + data.getMeasurationFormId());
    return mav;
  }

  public boolean supports(Class clazz) {
    return clazz.equals(AddExperimentOptParamValCommand.class);
  }

  public void validate(Object command, Errors errors) {
    AddExperimentOptParamValCommand data = (AddExperimentOptParamValCommand) command;

    if (!auth.userIsOwnerOrCoexperimenter(data.getMeasurationFormId())) {
      // First check whether the user has permission to add data
      errors.reject("error.mustBeOwnerOfExperimentOrCoexperimenter");
    } else {

      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "paramValue", "required.field");

      if (data.getParamId() < 0) {
        errors.rejectValue("paramId", "required.field");
      }

      ExperimentOptParamVal val = experimentOptParamValDao.read(new ExperimentOptParamValId(data.getMeasurationFormId(), data.getParamId()));
      if (val != null) {  // field already exists
        errors.rejectValue("paramId", "invalid.paramIdAlreadyInserted");
      }

    }

  }

  public GenericDao<Experiment, Integer> getExperimentDao() {
    return experimentDao;
  }

  public void setExperimentDao(GenericDao<Experiment, Integer> experimentDao) {
    this.experimentDao = experimentDao;
  }

  public GenericDao<ExperimentOptParamVal, ExperimentOptParamValId> getExperimentOptParamValDao() {
    return experimentOptParamValDao;
  }

  public void setExperimentOptParamValDao(GenericDao<ExperimentOptParamVal, ExperimentOptParamValId> experimentOptParamValDao) {
    this.experimentOptParamValDao = experimentOptParamValDao;
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
