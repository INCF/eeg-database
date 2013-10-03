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
 *   AddPersonController.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.person;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.EducationLevelDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.service.MailService;
import cz.zcu.kiv.eegdatabase.data.service.PersonService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.mail.MailException;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddPersonController extends SimpleFormController {

    private Log log = LogFactory.getLog(getClass());
    private PersonDao personDao;
    private EducationLevelDao educationLevelDao;
    private HierarchicalMessageSource messageSource;
    private MailService mailService;
    private PersonService personService;
    @Autowired
    private AuthorizationManager auth;

    public AddPersonController() {
        setCommandClass(AddPersonCommand.class);
        setCommandName("addPerson");
    }

    @Override
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        Map map = new HashMap<String, Object>();
        List<EducationLevel> list = educationLevelDao.getAllRecords();
        map.put("education", list);
        return map;
    }

    @Override
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
        ModelAndView mav = super.showForm(request, response, errors);
        boolean userIsExperimenter = auth.userIsExperimenter();
        mav.addObject("userIsExperimenter", userIsExperimenter);

        return mav;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindException) throws Exception {
        AddPersonCommand apc = (AddPersonCommand) command;
        boolean userIsExperimenter = auth.userIsExperimenter();

        Person person = personService.createPerson(apc);

        try {
            mailService.sendRegistrationConfirmMail(person, RequestContextUtils.getLocale(request));
        } catch (MailException e) {
            log.error("E-mail was NOT sent");
            log.error(e);
            e.printStackTrace();
        }

        log.debug("Returning MAV");
        ModelAndView mav = new ModelAndView(getSuccessView() + person.getPersonId());
        mav.addObject("userIsExperimenter", userIsExperimenter);
        return mav;
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    public EducationLevelDao getEducationLevelDao() {
        return educationLevelDao;
    }

    public void setEducationLevelDao(EducationLevelDao educationLevelDao) {
        this.educationLevelDao = educationLevelDao;
    }

    public HierarchicalMessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(HierarchicalMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public MailService getMailService() {
        return mailService;
    }

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    public PersonService getPersonService() {
        return personService;
    }

    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }
}
