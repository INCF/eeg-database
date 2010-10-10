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
public class PeopleSearcherController extends SimpleFormController{

   Log log = LogFactory.getLog(getClass());
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
    ModelAndView mav = new ModelAndView(getSuccessView());
    PeopleSearcherCommand peopleSearcherCommand = (PeopleSearcherCommand) command;
    logger.debug("I have people searcher command: " + peopleSearcherCommand);
    List<String> source = new ArrayList<String>();
    List<String> condition = new ArrayList<String>();
    List<String> andOr = new ArrayList<String>();
    Enumeration enumer = request.getParameterNames();
    while (enumer.hasMoreElements()) {
      String param = (String) enumer.nextElement();
      if (param.startsWith("source")) {
        source.add(param);
      }
      else if (param.startsWith("condition")) {
        condition.add(param);
      }
      else {
        andOr.add(param);
      }
    }
    Collections.sort(andOr);
    Collections.sort(source);
    Collections.sort(condition);
    List<SearchRequest> requests = new ArrayList<SearchRequest>();
    requests.add(new SearchRequest(request.getParameter(condition.get(0)),
            (request.getParameter(source.get(0))), ""));
    for (int i = 1; i < condition.size(); i++) {
          requests.add(new SearchRequest(request.getParameter(condition.get(i)),
            (request.getParameter(source.get(i))),
            (request.getParameter(andOr.get(i-1)))));
    }
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
