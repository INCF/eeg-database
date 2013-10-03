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
 *   ConfirmController.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.root;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ConfirmController extends AbstractController {
    private Log log = LogFactory.getLog(getClass());

    private PersonDao personDao;


    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Processing ConfirmController controller");
        ModelAndView mav;

        Person person = personDao.getPersonByHash(request.getParameter("activation"));

        if (person == null) {
            return new ModelAndView("system/registrationFalse");
        }

        if (person.isConfirmed()) {
            return new ModelAndView("system/registrationConfirmRepeated");
        }
        if (confirmedInTime(System.currentTimeMillis(), person)) {
            person.setConfirmed(true);
            mav = new ModelAndView("system/confirmationSuccessfull");
        } else {
            mav = new ModelAndView("system/registrationFalse");
            personDao.delete(person);
        }

        return mav;
    }

    public boolean confirmedInTime(long clickTime, Person person) {
        long requestTime = person.getRegistrationDate().getTime();
        //8 days in ms
        long maximumDelay = 691200000;
        return (clickTime - requestTime < maximumDelay);
    }


    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }
}
