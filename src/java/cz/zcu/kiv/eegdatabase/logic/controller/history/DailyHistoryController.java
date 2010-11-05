/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.controller.history;

import cz.zcu.kiv.eegdatabase.logic.commandobjects.ChangeDefaultGroupCommand;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * Sets reference data for form view and prepared ModelAndView for action "on submit"(for choice daily history )
 * using object change default group command for saving select group id
 * @author pbruha
 */
public class DailyHistoryController extends AbstractHistoryController {

  public DailyHistoryController() {
    setCommandClass(ChangeDefaultGroupCommand.class);
    setCommandName("changeDefaultGroup");
  }

  @Override
  protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindException) throws Exception {
    log.debug("Processing daily download history");
    ModelAndView mav = new ModelAndView(getSuccessView());
    ChangeDefaultGroupCommand changeDefaultGroupCommand = (ChangeDefaultGroupCommand) command;
    mav=super.onSubmit(ChoiceHistory.DAILY,changeDefaultGroupCommand, mav);
    log.debug("Returning MAV");
    return mav;
  }

  @Override
  protected Map referenceData(HttpServletRequest request) throws Exception {
    log.debug("Processing daily download history");
    Map map = new HashMap<String, Object>();
    map = super.setReferenceData(map, ChoiceHistory.DAILY);
    return map;
  }
}
