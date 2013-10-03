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
 *   UserServiceImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.webservices.rest.user;

import cz.zcu.kiv.eegdatabase.data.dao.EducationLevelDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import cz.zcu.kiv.eegdatabase.webservices.rest.common.exception.RestServiceException;
import cz.zcu.kiv.eegdatabase.webservices.rest.user.wrappers.PersonData;
import cz.zcu.kiv.eegdatabase.webservices.rest.user.wrappers.PersonDataList;
import cz.zcu.kiv.eegdatabase.webservices.rest.user.wrappers.UserInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Implementation of user service.
 *
 * @author Petr Miko
 */
@Service
public class UserServiceImpl implements UserService {

    private final static Log log = LogFactory.getLog(UserServiceImpl.class);
    private String DEFAULT_EDUCATION_LEVEL = "NotKnown";
    @Qualifier("mailSender")
    @Autowired
    private JavaMailSender mailSender;
    @Qualifier("messageSource")
    @Autowired
    private HierarchicalMessageSource messageSource;
    @Qualifier("mailMessage")
    @Autowired
    private SimpleMailMessage mailMessage;
    @Autowired
    @Qualifier("personDao")
    private PersonDao personDao;
    @Qualifier("educationLevelDao")
    @Autowired
    private EducationLevelDao educationLevelDao;

    /**
     * @{@inheritDoc}
     */
    @Transactional
    @Override
    public PersonData create(String registrationPath, PersonData personData, Locale locale) throws RestServiceException {
        try {
            Person person = new Person();
            person.setGivenname(personData.getName());
            person.setSurname(personData.getSurname());
            person.setPassword(personData.getPassword() == null ? ControllerUtils.getRandomPassword() : personData.getPassword());
            String plainPassword = person.getPassword();

            person.setRegistrationDate(new Timestamp(System.currentTimeMillis()));
            person.setGender(personData.getGender().charAt(0));
            person.setLaterality(personData.getLeftHanded().charAt(0));

            person.setDateOfBirth(new Timestamp(ControllerUtils.getDateFormat().parse(personData.getBirthday()).getTime()));
            person.setPhoneNumber(personData.getPhone());
            person.setNote(personData.getNotes());

            //dummy default education level
            List<EducationLevel> def = educationLevelDao.getEducationLevels(DEFAULT_EDUCATION_LEVEL);
            person.setEducationLevel(def.isEmpty() ? null : def.get(0));
            //default role
            person.setAuthority(Util.ROLE_READER);

            // security specifics
            person.setUsername(personData.getEmail());
            person.setAuthenticationHash(ControllerUtils.getMD5String(personData.getEmail()));
            person.setPassword(new BCryptPasswordEncoder().encode(plainPassword));

            int pk = personDao.create(person);
            sendRegistrationConfirmMail(registrationPath, plainPassword, person, locale);

            personData.setId(pk);

            return personData;
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
            throw new RestServiceException(e);
        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
            throw new RestServiceException(e);
        }
    }

    /**
     * @{@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public UserInfo login() {
        Person user = personDao.getLoggedPerson();
        return new UserInfo(user.getGivenname(), user.getSurname(), user.getAuthority());
    }

    @Override
    @Transactional(readOnly = true)
    public PersonDataList getUsers() {
        List<Person> original = personDao.getAllRecords();
        List<PersonData> people = new ArrayList<PersonData>();

        if (original != null) {
            for (Person p : original) {
                //records without email are not real users - omitted


                PersonData person = new PersonData();
                person.setId(p.getPersonId());
                person.setName(p.getGivenname());
                person.setSurname(p.getSurname());
                person.setEmail(p.getUsername() == null ? p.getEmail() : p.getUsername());

                //people without mail contact are not real users - omitted
                if (person.getEmail() == null) continue;

                if (p.getDateOfBirth() != null)
                    person.setBirthday(ControllerUtils.getDateFormat().format(p.getDateOfBirth()));
                person.setGender(String.valueOf(p.getGender()));
                person.setLeftHanded(String.valueOf(p.getLaterality()));
                person.setNotes(p.getNote());
                people.add(person);
            }
        }

        return new PersonDataList(people);
    }

    /**
     * Method for sending registration confirmation email.
     *
     * @param registrationPath path to registration confirmation page
     * @param plainPassword    password in plain text
     * @param user             created user object
     * @param locale           locale
     * @throws MailException      error during sending mail
     * @throws MessagingException error during sending mail
     */
    private void sendRegistrationConfirmMail(String registrationPath, String plainPassword, Person user, Locale locale) throws MailException, MessagingException {
        log.debug("Creating email content");
        StringBuilder sb = new StringBuilder();
        String login = "<b>" + user.getUsername() + "</b>";

        sb.append("<html><body>");
        sb.append("<h4>");
        sb.append(messageSource.getMessage("registration.email.welcome", null, locale));
        sb.append("</h4>");
        sb.append("<p>");
        sb.append(messageSource.getMessage("registration.email.body.yourLogin", new String[]{login}, locale));
        sb.append("</p>");
        sb.append("<p>");
        sb.append(messageSource.getMessage("registration.email.body.yourPassword", new String[]{plainPassword}, locale));
        sb.append("</p>");
        sb.append("<p>");
        sb.append(messageSource.getMessage("registration.email.body.clickToRegister", null, locale));
        sb.append("<br/>");

        String confirmURL = registrationPath + user.getAuthenticationHash();
        sb.append("<a href=\"").append(confirmURL).append("\">").append(confirmURL).append("</a>");
        sb.append("</p>");
        sb.append("</body></html>");

        String emailSubject = messageSource.getMessage("registration.email.subject", null, locale);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
        message.setFrom(mailMessage.getFrom());
        message.setTo(user.getEmail());
        message.setSubject(emailSubject);
        message.setText(sb.toString(), true);
        mailSender.send(mimeMessage);
    }
}
