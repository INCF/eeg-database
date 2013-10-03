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
 *   MailServiceTest.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.service;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import org.apache.log4j.net.SMTPAppender;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Locale;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: Jiri Novotny
 * Date: 12.3.12
 * Time: 3:09
 * To change this template use File | Settings | File Templates.
 */
public class MailServiceTest extends AbstractDataAccessTest {

    private static final String SMTP_SERVER_USERNAME = "PUT_REAL_USERNAME_HERE";
    private static final String SMTP_SERVER_PASSWORD = "PUT_REAL_PASSWORD_HERE";

    private static final int SMTP_SERVER_PORT = 465;
    private static final String SMTP_SERVER_PROTOCOL = "smtps";

    private static final String INCOMING_EMAIL_ADDRESS = "your_inbox@example.com";

    @Autowired
    private MailService mailService;
    @Autowired
    private JavaMailSenderImpl mailSender;//should be same object as the one used in mailService

    private Person person;
    private Locale locale = null;

    @Before
    public void init(){
        person = createPerson();
        mailSender.setUsername(SMTP_SERVER_USERNAME);
        mailSender.setPassword(SMTP_SERVER_PASSWORD);
        mailSender.setPort(SMTP_SERVER_PORT);
        mailSender.setProtocol(SMTP_SERVER_PROTOCOL);
        if(SMTP_SERVER_PORT == 465){
            mailSender.setJavaMailProperties(getPropertiesForSSL());
        }
    }

    @Test
    @Ignore(value = "To run this test, provide real login data and comment out the ignore")
    public void testSendRegistrationEmail(){
        mailService.sendRegistrationConfirmMail(person,locale);
        System.out.println("Email was sent successfully");
    }

    @Test
    @Ignore(value = "To run this test, provide real login data and comment out the ignore")
    public void testSendRequestForGroupRoleMail(){
        mailService.sendRequestForGroupRoleMail(INCOMING_EMAIL_ADDRESS,1234,
                person.getUsername(),"junit-research-group", locale);
        System.out.println("Email was sent successfully");
    }

    @Test
    @Ignore(value = "To run this test, provide real login data and comment out the ignore")
    public void testSendRequestForJoiningGroupMail(){
        mailService.sendRequestForJoiningGroupMail(INCOMING_EMAIL_ADDRESS,1234,
                person.getUsername(),"junit-research-group", locale);
        System.out.println("Email was sent successfully");
    }

    private Person createPerson() {
        Person person = new Person();
        person.setUsername(INCOMING_EMAIL_ADDRESS);
        person.setAuthority(Util.ROLE_READER);
        person.setPassword(ControllerUtils.getMD5String(ControllerUtils.getRandomPassword()));
        person.setSurname("junit-test-surname");
        person.setGivenname("junit-test-name");
        person.setAuthenticationHash("DEADBEEF");
        return person;
    }

    private Properties getPropertiesForSSL(){
        Properties pr = new Properties();
        pr.setProperty("mail.smtps.auth","true");
        pr.setProperty("mail.smtps.starttls.enable","true");
        pr.setProperty("mail.smtps.debug","true");
        return pr;
    }
}
