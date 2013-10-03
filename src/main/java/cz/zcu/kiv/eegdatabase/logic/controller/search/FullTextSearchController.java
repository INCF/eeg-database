/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   FullTextSearchController.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.controller.search;

import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.logic.wrapper.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
  private GenericDao<Weather, Integer> weatherDao;
  private GenericDao<ExperimentOptParamDef, Integer> experimentOptParamDef;
  private GenericDao<ArticleComment, Integer> commentDao;

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
      Set<FulltextResult> results = new HashSet<FulltextResult>();
      Wrapper w;


      Map<Scenario, String> scen = scenarioDao.getFulltextResults(fullTextQuery);
      for (Map.Entry<Scenario, String> entry : scen.entrySet()) {
        w = new ScenarioWrapper(entry.getKey());
        results.add(new FulltextResult(entry.getKey().getScenarioId(), entry.getValue(), w.className(), w.getPath(), w.getTitle()));
      }

      Map<Experiment, String> exp = experimentDao.getFulltextResults(fullTextQuery);
      for (Map.Entry<Experiment, String> entry : exp.entrySet()) {
        w = new ExperimentWrapper(entry.getKey());
        results.add(new FulltextResult(entry.getKey().getExperimentId(), entry.getValue(), w.className(), w.getPath(), w.getTitle()));
      }

      Map<Person, String> per = personDao.getFulltextResults(fullTextQuery);
      for (Map.Entry<Person, String> entry : per.entrySet()) {
        w = new PersonWrapper(entry.getKey());
        results.add(new FulltextResult(entry.getKey().getPersonId(), entry.getValue(), w.className(), w.getPath(), w.getTitle()));
      }

      Map<Article, String> art = articleDao.getFulltextResults(fullTextQuery);
      for (Map.Entry<Article, String> entry : art.entrySet()) {
        w = new ArticleWrapper(entry.getKey());
        results.add(new FulltextResult(entry.getKey().getArticleId(), entry.getValue(), w.className(), w.getPath(), w.getTitle()));
      }

      Map<Hardware, String> hw = hardwareDao.getFulltextResults(fullTextQuery);
      for (Map.Entry<Hardware, String> entry : hw.entrySet()) {
        for (Experiment e : entry.getKey().getExperiments()) {
          w = new ExperimentWrapper(e);
          results.add(new FulltextResult(e.getExperimentId(), entry.getValue(), w.className(), w.getPath(), w.getTitle()));
        }
      }

//      Map<VisualImpairment, String> vis = eyesDefectDao.getFulltextResults(fullTextQuery);
//      for (Map.Entry<VisualImpairment, String> entry : vis.entrySet()) {
//        for (Person p : entry.getKey().getPersons()) {
//          w = new PersonWrapper(p);
//          results.add(new FulltextResult(p.getPersonId(), entry.getValue(), w.className(), w.getPath(), w.getTitle()));
//        }
//      }
//
//      Map<HearingImpairment, String> hear = hearingImpairmentDao.getFulltextResults(fullTextQuery);
//       for (Map.Entry<HearingImpairment, String> entry : hear.entrySet()) {
//        for (Person p : entry.getKey().getPersons()) {
//          w = new PersonWrapper(p);
//          results.add(new FulltextResult(p.getPersonId(), entry.getValue(), w.className(), w.getPath(), w.getTitle()));
//        }
//      }

      Map<Weather, String> wea = weatherDao.getFulltextResults(fullTextQuery);
      for (Map.Entry<Weather, String> entry : wea.entrySet()) {
        for (Experiment e : entry.getKey().getExperiments()) {
          w = new ExperimentWrapper(e);
          results.add(new FulltextResult(e.getExperimentId(), entry.getValue(), w.className(), w.getPath(), w.getTitle()));
        }
      }

      Map<ArticleComment, String> com = commentDao.getFulltextResults(fullTextQuery);
      for (Map.Entry<ArticleComment, String> entry : com.entrySet()) {
        w = new ArticleWrapper(entry.getKey().getArticle());
        results.add(new FulltextResult(entry.getKey().getArticle().getArticleId(), entry.getValue(), w.className(), w.getPath(), w.getTitle()));
      }

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

}
