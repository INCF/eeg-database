package cz.zcu.kiv.eegdatabase.logic.controller.group;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.service.DataService;
import cz.zcu.kiv.eegdatabase.logic.controller.util.ControllerUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 *
 * @author Jindrich Pergler
 */
public class CreateGroupController extends SimpleFormController {

  private Log log = LogFactory.getLog(getClass());
  @Autowired
  private DataService dataService;

  public CreateGroupController() {
    setCommandClass(CreateGroupCommand.class);
    setCommandName("createGroup");
  }

  @Override
  protected ModelAndView onSubmit(Object command) throws Exception {
    log.debug("Processing form data with CreateGroupController.");
    ModelAndView mav = new ModelAndView();

    CreateGroupCommand data = (CreateGroupCommand) command;

    ResearchGroup group = dataService.createResearchGroup(data.getResearchGroupTitle(),
            data.getResearchGroupDescription(),
            ControllerUtils.getLoggedUserName());

    mav.setViewName("redirect:/groups/detail.html?groupId=" + group.getResearchGroupId());

    log.debug("Returning MAV.");
    return mav;
  }
}

