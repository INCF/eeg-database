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
 *   MyAccountDelegate.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.delegate;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Delegate class for multicontroller which processes My account section.
 *
 * @author Jindrich Pergler
 */
public class MyAccountDelegate {

    private ResearchGroupDao researchGroupDao;
    private PersonDao personDao;

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

    public ModelAndView overview(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("myAccount/overview");

        Map userInfo = personDao.getInfoForAccountOverview(personDao.getLoggedPerson());
        mav.addObject("userInfo", userInfo);

        Person person = personDao.getLoggedPerson();
        mav.addObject("facebookConnected", (person.getFacebookId() != null));
        mav.addObject("userFacebookId", person.getFacebookId());

        List list = researchGroupDao.getGroupDataForAccountOverview(personDao.getLoggedPerson());
        mav.addObject("membershipList", list);
        mav.addObject("membershipListEmpty", list.isEmpty());
        setUserIsInAnyGroup(mav,personDao.getLoggedPerson());
        return mav;
    }

    public ModelAndView status(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("redirect:/connect.html");

       
        return mav;
    }
    
    public static void setUserIsInAnyGroup(ModelAndView mav, Person loggedUser) {
        if(!loggedUser.getResearchGroupMemberships().isEmpty()){
            mav.addObject("userIsInAnyGroup", true);
        }
    }

    public static void setUserIsInAnyGroup(Map map, Person loggedUser) {
        if(!loggedUser.getResearchGroupMemberships().isEmpty()){
            map.put("userIsInAnyGroup", true);
        }
    }
}
