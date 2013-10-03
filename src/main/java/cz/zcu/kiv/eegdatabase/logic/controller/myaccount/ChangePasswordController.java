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
 *   ChangePasswordController.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.myaccount;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.delegate.MyAccountDelegate;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangePasswordController extends SimpleFormController {

    private Log log = LogFactory.getLog(getClass());
    private PersonDao personDao;

    public ChangePasswordController() {
        setCommandClass(ChangePasswordCommand.class);
        setCommandName("myAccount");
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindException) throws Exception {
        ChangePasswordCommand changePasswordCommand = (ChangePasswordCommand) command;

        log.debug("Saving new password for actual user");
        String newPassword = changePasswordCommand.getNewPassword();
        String passwordHash = new BCryptPasswordEncoder().encode(newPassword);
        Person user = personDao.getPerson(ControllerUtils.getLoggedUserName());
        user.setPassword(passwordHash);
        log.debug("Setting password hash [" + passwordHash + "] for user [" + user.getUsername() + "]");
        personDao.update(user);

        log.debug("Returning MAV");
        ModelAndView mav = new ModelAndView(getSuccessView());
        return mav;
    }

    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {
        Map map = new HashMap<String, Object>();
        MyAccountDelegate.setUserIsInAnyGroup(map, personDao.getLoggedPerson());
        return map;
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }
}
