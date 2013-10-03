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
 *   HomePageDelegate.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.delegate;

import cz.zcu.kiv.eegdatabase.data.dao.*;
import cz.zcu.kiv.eegdatabase.data.pojo.Article;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroupMembership;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

/**
 * Delegate class for multicontroller which processes groups section.
 *
 * @author Jindrich Pergler
 */
public class HomePageDelegate {

  private static final int LIMIT = 5;
  private ExperimentDao experimentDao;
  private ScenarioDao scenarioDao;
  private ResearchGroupDao researchGroupDao;
  private PersonDao personDao;
  private ArticleDao articleDao;
  private AuthorizationManager auth;

    public ModelAndView home(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("homePage/homePage");

        if (ControllerUtils.isUserLogged()) {
            Person person = personDao.getLoggedPerson();

            List myExperiments = experimentDao.getExperimentsWhereOwner(person, LIMIT);
            mav.addObject("myExperiments", myExperiments);
            mav.addObject("myExperimentsEmpty", myExperiments.isEmpty());

            List meAsSubjectList = experimentDao.getExperimentsWhereSubject(person, LIMIT);
            mav.addObject("meAsSubjectList", meAsSubjectList);
            mav.addObject("meAsSubjectListEmpty", meAsSubjectList.isEmpty());

            List myScenarios = scenarioDao.getScenariosWhereOwner(person, LIMIT);
            mav.addObject("myScenarios", myScenarios);
            mav.addObject("myScenariosEmpty", myScenarios.isEmpty());

            List<ResearchGroup> list = researchGroupDao.getResearchGroupsWhereMember(person, LIMIT);
            mav.addObject("groupList", list);
            mav.addObject("groupListEmpty", list.isEmpty());

            List articles = articleDao.getArticlesForUser(person, LIMIT);
            mav.addObject("articleList", articles);
        }
        return mav;
    }

    /**
     * Checks if user is admin in any group
     *
     * @param mav ModelAndView for display
     */
    public void setPermissionsToView(ModelAndView mav) {
        // isAdmin
        Person loggedUser = personDao.getLoggedPerson();
        if (loggedUser.getAuthority().equals("ROLE_ADMIN")) {
            mav.addObject("userIsAdminInAnyGroup", true);
            return;
        }
        // check all groups for admin role
        Set<ResearchGroupMembership> researchGroupMemberShips = loggedUser.getResearchGroupMemberships();
        for (ResearchGroupMembership member : researchGroupMemberShips) {
            if (auth.userIsAdminInGroup(member.getResearchGroup().getResearchGroupId())) {
                mav.addObject("userIsAdminInAnyGroup", true);
                return;
            }
        }
    }

  public ExperimentDao getExperimentDao() {
    return experimentDao;
  }

  public void setExperimentDao(ExperimentDao experimentDao) {
    this.experimentDao = experimentDao;
  }

  public ScenarioDao getScenarioDao() {
    return scenarioDao;
  }

  public void setScenarioDao(ScenarioDao scenarioDao) {
    this.scenarioDao = scenarioDao;
  }

  public ResearchGroupDao getResearchGroupDao() {
    return researchGroupDao;
  }

  public void setResearchGroupDao(ResearchGroupDao researchGroupDao) {
    this.researchGroupDao = researchGroupDao;
  }

  public PersonDao getPersonDao() {
    return personDao;
  }

  public void setPersonDao(PersonDao personDao) {
    this.personDao = personDao;
  }

  public ArticleDao getArticleDao() {
    return articleDao;
  }

  public void setArticleDao(ArticleDao articleDao) {
    this.articleDao = articleDao;
  }

  public AuthorizationManager getAuth() {
    return auth;
  }

  public void setAuth(AuthorizationManager auth) {
    this.auth = auth;
  }
}
