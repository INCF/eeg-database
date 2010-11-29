/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.controller.search;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Article;
import cz.zcu.kiv.eegdatabase.data.pojo.ArticleComment;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.HearingImpairment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.data.pojo.VisualImpairment;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import cz.zcu.kiv.eegdatabase.logic.commandobjects.FullTextSearchCommand;
import cz.zcu.kiv.eegdatabase.logic.wrapper.ArticleWrapper;
import cz.zcu.kiv.eegdatabase.logic.wrapper.ExperimentWrapper;
import cz.zcu.kiv.eegdatabase.logic.wrapper.PersonWrapper;
import cz.zcu.kiv.eegdatabase.logic.wrapper.ScenarioWrapper;
import cz.zcu.kiv.eegdatabase.logic.wrapper.Wrapper;
import java.util.ArrayList;
import java.util.Set;
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
  private GenericDao<ArticleComment, Integer> commentDao;
  private SectionCreator creator;



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
      Set<FulltextResult> results = null;

      String[] scenFields = {"title", "description", "scenarioLength"};
      results = creator.createSection(scenarioDao.getLuceneQuery
              (fullTextQuery, scenFields), scenFields, SectionType.SCENARIO, RelationshipType.SECTION_OBJ);

      String[] exFields = {"weathernote", "temperature"};
      results.addAll(creator.createSection(experimentDao.getLuceneQuery
              (fullTextQuery, exFields), exFields, SectionType.EXPERIMENT, RelationshipType.SECTION_OBJ));

      String[] perFields = {"note", "email"};
      results.addAll(creator.createSection(personDao.getLuceneQuery
              (fullTextQuery, perFields), perFields, SectionType.PERSON ,RelationshipType.SECTION_OBJ));

      String[] artFields = {"title", "text"};
      results.addAll(creator.createSection(articleDao.getLuceneQuery
              (fullTextQuery, artFields), artFields, SectionType.ARTICLE, RelationshipType.SECTION_OBJ));

      String[] hardFields = {"title", "type", "description"};
      results.addAll(creator.createSection(hardwareDao.getLuceneQuery
              (fullTextQuery, hardFields), hardFields, SectionType.EXPERIMENT, RelationshipType.SET));

      String[] visualImpairmentFields = {"description"};
      results.addAll(creator.createSection(eyesDefectDao.getLuceneQuery
              (fullTextQuery, visualImpairmentFields), visualImpairmentFields, SectionType.PERSON, RelationshipType.SET));

      String[] hearingFields = {"decription"};
      results.addAll(creator.createSection(hearingImpairmentDao.getLuceneQuery
              (fullTextQuery, hearingFields), hearingFields, SectionType.PERSON, RelationshipType.SET));

      String[] weatherFields = {"title", "description"};
      results.addAll(creator.createSection(weatherDao.getLuceneQuery
              (fullTextQuery, weatherFields), weatherFields, SectionType.EXPERIMENT, RelationshipType.SET));

//      String[] exOptParamDefFields = {"paramName", "paramDataType"};
//      results.addAll(scenarioSection.createSection(experimentOptParamDef.getLuceneQuery
//              (fullTextQuery, exOptParamDefFields), ExperimentOptParamDef.class, exOptParamDefFields));

      String[] commentPar = {"text"};
      results.addAll(creator.createSection(commentDao.getLuceneQuery
              (fullTextQuery, commentPar), commentPar, SectionType.ARTICLE, RelationshipType.SIMPLE_REL));


      logger.debug("I have results: " + results);

      mav.addObject("searchedString", fullTextQuery);
      mav.addObject("searchResults", results);
      mav.addObject("resultsEmpty", results.isEmpty());
    } else {
      mistakes.add("Unable to parse query: " + fullTextQuery);
      logger.debug("Unable to parse query: " + fullTextQuery);
      mav.addObject("errors", mistakes);
    }
    return mav;
  }

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

  public GenericDao<ArticleComment, Integer> getCommentDao() {
    return commentDao;
  }

  public void setCommentDao(GenericDao<ArticleComment, Integer> commentDao) {
    this.commentDao = commentDao;
  }

  public SectionCreator getCreator() {
    return creator;
  }

  public void setCreator(SectionCreator creator) {
    this.creator = creator;
  }

}
