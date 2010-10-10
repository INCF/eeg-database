/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.controller.search;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Article;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.HearingImpairment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.data.pojo.VisualImpairment;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import java.util.List;
import cz.zcu.kiv.eegdatabase.logic.commandobjects.FullTextSearchCommand;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 *
 * @author Petr Brůha
 */
public class FullTextSearchController extends SimpleFormController {

  Log log = LogFactory.getLog(getClass());
  //private GenericDao<Object, Integer> genericDao;
  private GenericDao<Person, Integer> personDao;
  private GenericDao<Experiment, Integer> experimentDao;
  private GenericDao<Scenario, Integer> scenarioDao;
  private GenericDao<Article, Integer> articleDao;
  private GenericDao<Hardware, Integer> hardwareDao;
  private GenericDao<HearingImpairment, Integer> hearingImpairmentDao;
  private GenericDao<VisualImpairment, Integer> eyesDefectDao;
  private GenericDao<Weather, Integer> weatherDao;
  private GenericDao<ExperimentOptParamDef, Integer> experimentOptParamDef;

  public FullTextSearchController() {
    setCommandClass(FullTextSearchCommand.class);
    setCommandName("fullTextSearchCommand");
  }

  @Override
  protected Object formBackingObject(HttpServletRequest request) throws Exception {
    FullTextSearchCommand search = (FullTextSearchCommand) super.formBackingObject(request);
    return search;
  }

  @Override
  protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
    logger.debug("Full text search controller");
    ModelAndView mav = new ModelAndView(getSuccessView());
    ArrayList<String> mistakes = new ArrayList<String>();
    FullTextSearchCommand fullTextSearchCommand = (FullTextSearchCommand) command;
    logger.debug("I have fullTextSearchCommand: " + fullTextSearchCommand);
    String fullTextQuery = fullTextSearchCommand.getSearchTI();
    if (!fullTextQuery.equals("") && !fullTextQuery.startsWith("*")) {
      String[] ScenFields = {"TITLE", "DESCRIPTION"};
      List<Scenario> results = scenarioDao.getFullTextResult(fullTextQuery, ScenFields);
      String[] ExFields = {"WEATHERNOTE"};
      List<Experiment> exResults = experimentDao.getFullTextResult(fullTextQuery, ExFields);
      String[] PerFields = {"NOTE"};
      List<Person> personResults = personDao.getFullTextResult(fullTextQuery, PerFields);
      String[] ArtFields = {"ARTICLETITLE", "ARTICLETEXT"};
      List<Article> articleResults = articleDao.getFullTextResult(fullTextQuery, ArtFields);
      String[] HardFields = {"HARDWARETITLE", "TYPE", "HARDWAREDESCRIPTION"};
      List<Hardware> hardwareResults = hardwareDao.getFullTextResult(fullTextQuery, HardFields);
      String[] VisualImpairmentFields = {"VISUALIMPAIRMENTDESCRIPTION"};
      List<VisualImpairment> visualImpairmentResults = eyesDefectDao.getFullTextResult(fullTextQuery, VisualImpairmentFields);
      String[] HearingFields = {"HEARINGDESCRIPTION"};
      List<HearingImpairment> hearingImpairmentResults = hearingImpairmentDao.getFullTextResult(fullTextQuery, HearingFields);
      String[] WeatherFields = {"WEATHERTITLE", "WEATHERDESCRIPTION"};
      List<Weather> weatherResults = weatherDao.getFullTextResult(fullTextQuery, WeatherFields);
      String[] ExOptParamDefFields = {"EXPARAMNAME", "EXPARAMDATATYPE"};
      List<ExperimentOptParamDef> experimentOptParamDefResults = experimentOptParamDef.getFullTextResult(fullTextQuery, ExOptParamDefFields);
      logger.debug("I have results: " + results);
      if (results != null) {
        for (Scenario meas : results) {
          logger.debug("Results: " + meas);
        }
      }
      mav.addObject("searchedString", fullTextQuery);
      mav.addObject("searchResults", results);
      mav.addObject("exResults", exResults);
      mav.addObject("personResults", personResults);
      mav.addObject("articleResults", articleResults);
      mav.addObject("hardwareResults", hardwareResults);
      mav.addObject("hearingImpairmentResults", hearingImpairmentResults);
      mav.addObject("visualImpairmentResults", visualImpairmentResults);
      mav.addObject("weatherResults", weatherResults);
      mav.addObject("expOptParDefResults", experimentOptParamDefResults);
    } else {
      mistakes.add("Unable to parse query: " + fullTextQuery);
      logger.debug("Unable to parse query: " + fullTextQuery);
      mav.addObject("errors", mistakes);
    }
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

  public GenericDao<Experiment, Integer> getExperimentDao() {
    return experimentDao;
  }

  public void setExperimentDao(GenericDao<Experiment, Integer> experimentDao) {
    this.experimentDao = experimentDao;
  }

  public GenericDao<Article, Integer> getArticleDao() {
    return articleDao;
  }

  public void setArticleDao(GenericDao<Article, Integer> articleDao) {
    this.articleDao = articleDao;
  }

  public GenericDao<Hardware, Integer> getHardwareDao() {
    return hardwareDao;
  }

  public void setHardwareDao(GenericDao<Hardware, Integer> hardwareDao) {
    this.hardwareDao = hardwareDao;
  }

  public GenericDao<HearingImpairment, Integer> getHearingImpairmentDao() {
    return hearingImpairmentDao;
  }

  public void setHearingImpairmentDao(GenericDao<HearingImpairment, Integer> hearingImpairmentDao) {
    this.hearingImpairmentDao = hearingImpairmentDao;
  }

  public GenericDao<VisualImpairment, Integer> getEyesDefectDao() {
    return eyesDefectDao;
  }

  public void setEyesDefectDao(GenericDao<VisualImpairment, Integer> eyesDefectDao) {
    this.eyesDefectDao = eyesDefectDao;
  }

  public GenericDao<Weather, Integer> getWeatherDao() {
    return weatherDao;
  }

  public void setWeatherDao(GenericDao<Weather, Integer> weatherDao) {
    this.weatherDao = weatherDao;
  }

  public GenericDao<ExperimentOptParamDef, Integer> getExperimentOptParamDef() {
    return experimentOptParamDef;
  }

  public void setExperimentOptParamDef(GenericDao<ExperimentOptParamDef, Integer> experimentOptParamDef) {
    this.experimentOptParamDef = experimentOptParamDef;
  }
}
