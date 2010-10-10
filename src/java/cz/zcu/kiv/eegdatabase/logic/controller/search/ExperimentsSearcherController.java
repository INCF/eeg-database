/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.controller.search;

import cz.zcu.kiv.eegdatabase.data.dao.ExperimentDao;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import java.util.List;
import cz.zcu.kiv.eegdatabase.logic.commandobjects.ExperimentsSearcherCommand;
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
import java.util.Map;

/**
 *
 * @author pbruha
 */
public class ExperimentsSearcherController extends SimpleFormController {

  Log log = LogFactory.getLog(getClass());
  //private GenericDao<Object, Integer> genericDao;
  private GenericDao<Person, Integer> personDao;
  private ExperimentDao experimentDao;
  private GenericDao<Scenario, Integer> scenarioDao;
  private GenericDao<Hardware, Integer> hardwareDao;

  public ExperimentsSearcherController() {
    setCommandClass(ExperimentsSearcherCommand.class);
    setCommandName("experimentsSearcherCommand");
  }

  @Override
  protected Object formBackingObject(HttpServletRequest request) throws Exception {
    ExperimentsSearcherCommand search = (ExperimentsSearcherCommand) super.formBackingObject(request);
    return search;
  }

   @Override
  protected Map referenceData(HttpServletRequest request) throws Exception {
    Map map = new HashMap<String, Object>();
    List<Scenario> scenarioList = scenarioDao.getAllRecords();
    List<Hardware> hardwareList = hardwareDao.getAllRecords();
    map.put("scenarioList",scenarioList);
    map.put("hardwareList", hardwareList);
    return map;
  }

  @Override
  protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
    logger.debug("Search experiments controller");
    ModelAndView mav = new ModelAndView(getSuccessView());
    ExperimentsSearcherCommand measurationSearchCommand = (ExperimentsSearcherCommand) command;
    logger.debug("I have search experiments command: " + measurationSearchCommand);
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
     List<Experiment> experimentResults = experimentDao.getExperimentSearchResults(requests);
    mav.addObject("experimentsResults", experimentResults);
    mav.addObject("resultsEmpty", experimentResults.isEmpty());
    return mav;
  }

//  public GenericDao<Object, Integer> getGenericDao() {
//    return genericDao;
//  }
//
//  public void setGenericDao(GenericDao<Object, Integer> genericDao) {
//    this.genericDao = genericDao;
//  }
  public GenericDao<Person, Integer> getPersonDao() {
    return personDao;
  }

  public void setPersonDao(GenericDao<Person, Integer> personDao) {
    this.personDao = personDao;
  }

  public GenericDao<Scenario, Integer> getScenarioDao() {
    return scenarioDao;
  }

  public void setScenarioDao(GenericDao<Scenario, Integer> scenarioDao) {
    this.scenarioDao = scenarioDao;
  }

  public ExperimentDao getExperimentDao() {
    return experimentDao;
  }

  public void setExperimentDao(ExperimentDao experimentDao) {
    this.experimentDao = experimentDao;
  }

  public GenericDao<Hardware, Integer> getHardwareDao() {
    return hardwareDao;
  }

  public void setHardwareDao(GenericDao<Hardware, Integer> hardwareDao) {
    this.hardwareDao = hardwareDao;
  }
  
}
