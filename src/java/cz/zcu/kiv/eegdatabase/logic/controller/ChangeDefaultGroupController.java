package cz.zcu.kiv.eegdatabase.logic.controller;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import cz.zcu.kiv.eegdatabase.logic.controller.util.ControllerUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.commandobjects.ChangeDefaultGroupCommand;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeDefaultGroupController extends SimpleFormController {
 
  private Log log = LogFactory.getLog(getClass());
  private PersonDao personDao;
  private ResearchGroupDao researchGroupDao;



  public ChangeDefaultGroupController() {
    setCommandClass(ChangeDefaultGroupCommand.class);
    setCommandName("changeDefaultGroup");
  }

  @Override
  protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindException) throws Exception {
    ChangeDefaultGroupCommand changeDefaultGroupCommand = (ChangeDefaultGroupCommand) command;

    log.debug("Changing default group for user");
    Person user = personDao.getPerson(ControllerUtils.getLoggedUserName());
    user.setDefaultGroup(researchGroupDao.read(changeDefaultGroupCommand.getDefaultGroup()));
    personDao.update(user);
    //personDao.update(user);

    log.debug("Returning MAV");
    ModelAndView mav = new ModelAndView(getSuccessView());
    return mav;
  }

  @Override
  protected Map referenceData(HttpServletRequest request) throws Exception {
    Map map = new HashMap<String, Object>();
    List<ResearchGroup> researchGroupList =
            researchGroupDao.getResearchGroupsWhereAbleToWriteInto(personDao.getLoggedPerson());
    ResearchGroup defaultGroup = personDao.getLoggedPerson().getDefaultGroup();
    int defaultGroupId = (defaultGroup != null) ? defaultGroup.getResearchGroupId() : 0;
    map.put("defaultGroupId",defaultGroupId);
    map.put("researchGroupList", researchGroupList);
    
    return map;
  }

  public PersonDao getPersonDao() {
    return personDao;
  }

  public void setPersonDao(PersonDao personDao) {
    this.personDao = personDao;
  }

  public ResearchGroupDao getResearchGroupDao() {
    return researchGroupDao;
  }

  public void setResearchGroupDao(ResearchGroupDao researchGroupDao) {
    this.researchGroupDao = researchGroupDao;
  }

}
