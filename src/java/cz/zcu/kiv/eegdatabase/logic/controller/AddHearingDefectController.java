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
import cz.zcu.kiv.eegdatabase.data.pojo.HearingImpairment;

public class AddHearingDefectController extends SimpleFormController {

  private Log log = LogFactory.getLog(getClass());
  private AuthorizationManager auth;
  private GenericDao<HearingImpairment, Integer> hearingImpairmentDao;

  public AddHearingDefectController() {
    setCommandClass(AddDefectCommand.class);
    setCommandName("addHearingDefect");
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
    HearingImpairment defect = new HearingImpairment();
    defect.setDescription(data.getDescription());

    log.debug("Saving data to database");
    hearingImpairmentDao.create(defect);

    log.debug("Returning MAV");
    return mav;
  }

  public GenericDao<HearingImpairment, Integer> getHearingImpairmentDao() {
    return hearingImpairmentDao;
  }

  public void setHearingImpairmentDao(GenericDao<HearingImpairment, Integer> hearingImpairmentDao) {
    this.hearingImpairmentDao = hearingImpairmentDao;
  }

  public AuthorizationManager getAuth() {
    return auth;
  }

  public void setAuth(AuthorizationManager auth) {
    this.auth = auth;
  }
}
