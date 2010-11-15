/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.logic.controller.search;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.HearingImpairment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.VisualImpairment;
import cz.zcu.kiv.eegdatabase.logic.commandobjects.PeopleSearcherCommand;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 *
 * @author pbruha
 */
public class PeopleSearcherController extends AbstractSearchController {

  private PersonDao personDao;
  private GenericDao<VisualImpairment, Integer> eyesDefectDao;
  private GenericDao<HearingImpairment, Integer> hearingImpairmentDao;

  public PeopleSearcherController() {
    setCommandClass(PeopleSearcherCommand.class);
    setCommandName("peopleSearcherCommand");
  }

  @Override
  protected Object formBackingObject(HttpServletRequest request) throws Exception {
    PeopleSearcherCommand search = (PeopleSearcherCommand) super.formBackingObject(request);
    return search;
  }



  @Override
  protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
    logger.debug("Search people controller");
    ModelAndView mav = super.onSubmit(request, response, command);

    try {
    List<Person> personResults = personDao.getPersonSearchResults(requests);
    mav.addObject("personResults", personResults);
    mav.addObject("resultsEmpty", personResults.isEmpty());
    }
     catch (NumberFormatException e) {
      mav.addObject("mistake", "Number error");
      mav.addObject("error", true);
    }
    catch (RuntimeException e) {
      mav.addObject("mistake", e.getMessage());
      mav.addObject("error", true);
    }
    return mav;
  }

  public GenericDao<HearingImpairment, Integer> getHearingImpairmentDao() {
    return hearingImpairmentDao;
  }

  public void setHearingImpairmentDao(GenericDao<HearingImpairment, Integer> hearingImpairmentDao) {
    this.hearingImpairmentDao = hearingImpairmentDao;
  }

  public PersonDao getPersonDao() {
    return personDao;
  }

  public void setPersonDao(PersonDao personDao) {
    this.personDao = personDao;
  }

  public GenericDao<VisualImpairment, Integer> getEyesDefectDao() {
    return eyesDefectDao;
  }

  public void setEyesDefectDao(GenericDao<VisualImpairment, Integer> eyesDefectDao) {
    this.eyesDefectDao = eyesDefectDao;
  }

  


}
