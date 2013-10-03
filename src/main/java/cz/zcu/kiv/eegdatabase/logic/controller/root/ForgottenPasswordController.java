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
 *   ForgottenPasswordController.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.root;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ForgottenPasswordController extends SimpleFormController {

    private Log log = LogFactory.getLog(getClass());
    private PersonDao personDao;
    private MailSender mailSender;
    private SimpleMailMessage mailMessage;
    private String failedView;

    public ForgottenPasswordController() {
        setCommandClass(ForgottenPasswordCommand.class);
        setCommandName("forgottenPassword");
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindException) throws Exception {
        ForgottenPasswordCommand fpc = (ForgottenPasswordCommand) command;

        Person user = personDao.getPerson(fpc.getUsername());

        log.debug("Creating new mail object");
        SimpleMailMessage mail = new SimpleMailMessage(mailMessage);

        String recipient = user.getEmail();
        log.debug("Composing e-mail - TO: " + recipient);
        mail.setTo(recipient);

        String subject = mail.getSubject() + " - Password reset";
        log.debug("Composing e-mail - SUBJECT: " + subject);
        mail.setSubject(subject);

        String password = ControllerUtils.getRandomPassword();
        String text = "Your password for EEGbase portal was reset. Your new password (within brackets) is [" + password + "]\n\n" +
                "Please change the password after logging into system.";
        log.debug("Composing e-mail - TEXT: " + text);
        mail.setText(text);

        String mavName = getSuccessView();
        try {
            log.debug("Sending e-mail message");
            mailSender.send(mail);
            log.debug("E-mail message sent successfully");

            log.debug("Updating new password into database");
            user.setPassword(new BCryptPasswordEncoder().encode(password));
            personDao.update(user);
            log.debug("Password updated");
        } catch (MailException e) {
            log.debug("E-mail message was NOT sent");
            log.debug("Password was NOT changed");
            mavName = getFailedView();
        }

        log.debug("Returning MAV");
        ModelAndView mav = new ModelAndView(mavName);
        return mav;
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    public MailSender getMailSender() {
        return mailSender;
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public SimpleMailMessage getMailMessage() {
        return mailMessage;
    }

    public void setMailMessage(SimpleMailMessage mailMessage) {
        this.mailMessage = mailMessage;
    }

    public String getFailedView() {
        return failedView;
    }

    public void setFailedView(String failedView) {
        this.failedView = failedView;
    }
}
