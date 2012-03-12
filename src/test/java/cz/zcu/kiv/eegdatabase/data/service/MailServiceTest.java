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
        person.setUsername("junit-test-reader");
        person.setAuthority(Util.ROLE_READER);
        person.setPassword(ControllerUtils.getMD5String(ControllerUtils.getRandomPassword()));
        person.setEmail(INCOMING_EMAIL_ADDRESS);
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
