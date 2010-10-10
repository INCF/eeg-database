package cz.zcu.kiv.eegdatabase.logic.controller;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import cz.zcu.kiv.eegdatabase.logic.commandobjects.AddDefectCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import cz.zcu.kiv.eegdatabase.data.pojo.VisualImpairment;

public class AddEyesDefectController extends SimpleFormController {

  private Log log = LogFactory.getLog(getClass());
  private AuthorizationManager auth;
  private GenericDao<VisualImpairment, Integer> eyesDefectDao;

  public AddEyesDefectController() {
    setCommandClass(AddDefectCommand.class);
    setCommandName("addEyesDefect");
  }

  @Override
  protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindException) throws Exception {
    ModelAndView mav = new ModelAndView(getSuccessView());

    log.debug("Processing form data.");

    AddDefectCommand data = (AddDefectCommand) command;

    if (!auth.userIsExperimenter()) {
      mav.setViewName("lists/userNotExperimenter");
    }

    log.debug("Creating new object");
    VisualImpairment defect = new VisualImpairment();
    defect.setDescription(data.getDescription());

    log.debug("Saving data to database");
    eyesDefectDao.create(defect);

    log.debug("Returning MAV");
    return mav;
  }

  public GenericDao<VisualImpairment, Integer> getEyesDefectDao() {
    return eyesDefectDao;
  }

  public void setEyesDefectDao(GenericDao<VisualImpairment, Integer> eyesDefectDao) {
    this.eyesDefectDao = eyesDefectDao;
  }

  public AuthorizationManager getAuth() {
    return auth;
  }

  public void setAuth(AuthorizationManager auth) {
    this.auth = auth;
  }
}
