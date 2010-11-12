/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.logic.controller.search;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ScenarioDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.logic.commandobjects.ScenarioSearcherCommand;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pbruha
 */
public class ScenarioSearcherController extends SimpleFormController{
Log log = LogFactory.getLog(getClass());
  private PersonDao personDao;
  private ScenarioDao scenarioDao;

  public ScenarioSearcherController() {
    setCommandClass(ScenarioSearcherCommand.class);
    setCommandName("scenarioSearcherCommand");
  }

  @Override
  protected Object formBackingObject(HttpServletRequest request) throws Exception {
    ScenarioSearcherCommand search = (ScenarioSearcherCommand) super.formBackingObject(request);
    return search;
  }

   @Override
  protected Map referenceData(HttpServletRequest request) throws Exception {
    Map map = new HashMap<String, Object>();
    List<Scenario> scenarioList = scenarioDao.getAllRecords();
    List<Person> personList = personDao.getAllRecords();
    map.put("scenarioList", scenarioList);
    map.put("personList", personList);
    return map;
  }

  @Override
  protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
    logger.debug("Search scenario controller");
    ModelAndView mav = new ModelAndView(getSuccessView());
    ScenarioSearcherCommand scenarioSearcherCommand = (ScenarioSearcherCommand) command;
   
    logger.debug("I have search scenario command: " + scenarioSearcherCommand);
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
   List<Scenario> scenarioResults = scenarioDao.getScenarioSearchResults
           (requests, personDao.getLoggedPerson().getPersonId());
     mav.addObject("scenarioResults", scenarioResults);
     mav.addObject("resultsEmpty", scenarioResults.isEmpty());
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

  public PersonDao getPersonDao() {
    return personDao;
  }

  public void setPersonDao(PersonDao personDao) {
    this.personDao = personDao;
  }

  public ScenarioDao getScenarioDao() {
    return scenarioDao;
  }

  public void setScenarioDao(ScenarioDao scenarioDao) {
    this.scenarioDao = scenarioDao;
  }
  
}
