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
  private SectionCreator scenarioSection;
  private SectionCreator experimentSection;
  private SectionCreator personSection;
  private SectionCreator articleSection;



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

      String[] scenFields = {"title", "description"};
      results = scenarioSection.createSection(scenarioDao.getLuceneQuery
              (fullTextQuery, scenFields), Scenario.class, scenFields);

      String[] exFields = {"weathernote", "temperature"};
      results.addAll(experimentSection.createSection(experimentDao.getLuceneQuery
              (fullTextQuery, exFields), Experiment.class, exFields));

      String[] perFields = {"note", "email"};
      results.addAll(personSection.createSection(personDao.getLuceneQuery
              (fullTextQuery, perFields), Person.class, perFields));

      String[] artFields = {"title", "text"};
      results.addAll(articleSection.createSection(articleDao.getLuceneQuery
              (fullTextQuery, artFields), Article.class, artFields));

      String[] hardFields = {"title", "type", "description"};
      results.addAll(experimentSection.createSection(hardwareDao.getLuceneQuery
              (fullTextQuery, hardFields), Hardware.class, hardFields));

      String[] visualImpairmentFields = {"description"};
      results.addAll(personSection.createSection(eyesDefectDao.getLuceneQuery
              (fullTextQuery, visualImpairmentFields), VisualImpairment.class, visualImpairmentFields));

      String[] hearingFields = {"decription"};
      results.addAll(personSection.createSection(hearingImpairmentDao.getLuceneQuery
              (fullTextQuery, hearingFields), HearingImpairment.class, hearingFields));

      String[] weatherFields = {"title", "description"};
      results.addAll(experimentSection.createSection(weatherDao.getLuceneQuery
              (fullTextQuery, weatherFields), Weather.class, weatherFields));

//      String[] exOptParamDefFields = {"paramName", "paramDataType"};
//      results.addAll(scenarioSection.createSection(experimentOptParamDef.getLuceneQuery
//              (fullTextQuery, exOptParamDefFields), ExperimentOptParamDef.class, exOptParamDefFields));

      String[] commentPar = {"text"};
      results.addAll(articleSection.createSection(commentDao.getLuceneQuery
              (fullTextQuery, commentPar), ArticleComment.class, commentPar));


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
    public SectionCreator getScenarioSection() {
    return scenarioSection;
  }

  public void setScenarioSection(SectionCreator scenarioSection) {
    this.scenarioSection = scenarioSection;
  }


  public SectionCreator getArticleSection() {
    return articleSection;
  }

  public void setArticleSection(SectionCreator articleSection) {
    this.articleSection = articleSection;
  }

  public SectionCreator getExperimentSection() {
    return experimentSection;
  }

  public void setExperimentSection(SectionCreator experimentSection) {
    this.experimentSection = experimentSection;
  }

  public SectionCreator getPersonSection() {
    return personSection;
  }

  public void setPersonSection(SectionCreator personSection) {
    this.personSection = personSection;
  }
}
