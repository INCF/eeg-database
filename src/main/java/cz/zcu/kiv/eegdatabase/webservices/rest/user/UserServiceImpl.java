package cz.zcu.kiv.eegdatabase.webservices.rest.user;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.controller.person.AddPersonCommand;
import cz.zcu.kiv.eegdatabase.webservices.rest.common.exception.RestServiceException;
import cz.zcu.kiv.eegdatabase.webservices.rest.user.wrappers.UserInfo;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.text.ParseException;
import java.util.Locale;

/**
 * Implementation of user service.
 *
 * @author Petr Miko
 */
@Service
public class UserServiceImpl implements UserService {

    private final static Log log = LogFactory.getLog(UserServiceImpl.class);
    @Qualifier("mailSender")
    @Autowired
    private JavaMailSender mailSender;
    @Qualifier("messageSource")
    @Autowired
    private HierarchicalMessageSource messageSource;
    @Qualifier("mailMessage")
    @Autowired
    private SimpleMailMessage mailMessage;
    
    @Deprecated()
    @Autowired
    @Qualifier("personDao")
    private PersonDao personDao;
    
    @Qualifier("personService")
    @Autowired
    private PersonService personService;

    /**
     * @{@inheritDoc}
     */
    @Override
    public UserInfo create(String registrationPath, AddPersonCommand cmd, Locale locale) throws RestServiceException {
        try {
            Person person = personService.createPerson(cmd);
            sendRegistrationConfirmMail(registrationPath, person, locale);
            return new UserInfo(person.getGivenname(), person.getSurname(), person.getAuthority());
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
        Person user = personService.getLoggedPerson();
        return new UserInfo(user.getGivenname(), user.getSurname(), user.getAuthority());
    }

    /**
     * Method for sending registration confirmation email.
     * @param registrationPath path to registration confirmation page
     * @param user created user object
     * @param locale locale
     * @throws MailException error during sending mail
     * @throws MessagingException error during sending mail
     */
    private void sendRegistrationConfirmMail(String registrationPath, Person user, Locale locale) throws MailException, MessagingException {
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
