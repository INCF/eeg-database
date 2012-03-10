package cz.zcu.kiv.eegdatabase.logic.controller.person;

import cz.zcu.kiv.eegdatabase.data.dao.EducationLevelDao;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.util.*;

public class AddPersonController extends SimpleFormController {

    private Log log = LogFactory.getLog(getClass());
    private PersonDao personDao;
    private EducationLevelDao educationLevelDao;
    private JavaMailSenderImpl mailSender;
    private HierarchicalMessageSource messageSource;
    private String domain;


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
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindException) throws Exception {
        AddPersonCommand apc = (AddPersonCommand) command;

        log.debug("Creating new person.");
        Person person = new Person();

        log.debug("Setting givenname = " + apc.getGivenname());
        person.setGivenname(apc.getGivenname());

        log.debug("Setting surname = " + apc.getSurname());
        person.setSurname(apc.getSurname());

        Date dateOfBirth = ControllerUtils.getDateFormat().parse(apc.getDateOfBirth());
        person.setDateOfBirth(new Timestamp(dateOfBirth.getTime()));
        log.debug("Setting date of birth = " + dateOfBirth);

        log.debug("Setting gender = " + apc.getGender());
        person.setGender(apc.getGender().charAt(0));

        log.debug("Setting email = " + apc.getEmail());
        person.setEmail(apc.getEmail());

        log.debug("Setting phone number = " + apc.getPhoneNumber());
        person.setPhoneNumber(apc.getPhoneNumber());

        log.debug("Setting note = " + apc.getNote());
        person.setNote(apc.getNote());

        log.debug("Generating username");
        String username = apc.getGivenname().toLowerCase() + "-" + apc.getSurname().toLowerCase();

        // Removing the diacritical marks
        String decomposed = Normalizer.normalize(username, Normalizer.Form.NFD);
        username = decomposed.replaceAll("[^\\p{ASCII}]", "");

        String tempUsername = username;
        // if the username already exists generate random numbers until some combination is free
        // if the username does not exist, we can use it (so the while is jumped over)
        while (personDao.usernameExists(tempUsername)) {
            Random random = new Random();
            int number = random.nextInt(999) + 1;  // not many users are expected to have the same name and surname, so 1000 numbers is enough
            tempUsername = username + "-" + number;
        }

        username = tempUsername;
        log.debug("Unique username was generated: " + username);
        person.setUsername(username);

        log.debug("Generating random password");
        String password = ControllerUtils.getRandomPassword();
        person.setPassword(ControllerUtils.getMD5String(password));

        log.debug("Setting authority to ROLE_READER");
        person.setAuthority("ROLE_READER");


        log.debug("Creating new Person object");
        person.setLaterality(apc.getLaterality().charAt(0));
        person.setEducationLevel(educationLevelDao.read(apc.getEducationLevel()));
        personDao.create(person);


        log.debug("Hashing the username");
        String authHash = ControllerUtils.getMD5String(apc.getGivenname());

        String userName = "<b>" + person.getUsername() + "</b>";
        String pass = "<b>" + person.getPassword() + "</b>";

        log.debug("Creating email content");
        String emailBody = "<html><body>";

        emailBody += "<h4>" + messageSource.getMessage("registration.email.body.hello",
                null, RequestContextUtils.getLocale(request)) + "</h4>";

        emailBody += "<p>" + messageSource.getMessage("registration.email.body.text.part1",
                null, RequestContextUtils.getLocale(request)) + "</p>";

        emailBody += "<p>" + messageSource.getMessage("registration.email.body.text.part2",
                new String[]{userName}, RequestContextUtils.getLocale(request)) + "<br/>";

        emailBody += messageSource.getMessage("registration.email.body.text.part3",
                new String[]{pass}, RequestContextUtils.getLocale(request)) + "</p>";

        emailBody += "<p>" + messageSource.getMessage("registration.email.body.text.part4",
                null, RequestContextUtils.getLocale(request)) + "<br/>";


        emailBody +=
                "<a href=\"http://" + domain + "/registration-confirmation.html?activation=" + authHash + "\">" +
                        "http://" + domain + "/registration-confirmation.html?activation=" + authHash + "" +
                        "</a>" +
                        "</p>";

        emailBody += "</body></html>";

        log.debug("email body: " + emailBody);


        log.debug("Composing e-mail message");
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        //message.setContent(confirmLink, "text/html");
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setTo(apc.getEmail());
        //helper.setFrom(messageSource.getMessage("registration.email.from",null, RequestContextUtils.getLocale(request)));
        helper.setSubject(messageSource.getMessage("registration.email.subject", null, RequestContextUtils.getLocale(request)));
        helper.setText(emailBody, true);

        try {
            log.debug("Sending e-mail");
            mailSender.send(mimeMessage);
            log.debug("E-mail was sent");
        } catch (MailException e) {
            log.error("E-mail was NOT sent");
            log.error(e);
            e.printStackTrace();
        }

        log.debug("Returning MAV");
        ModelAndView mav = new ModelAndView(getSuccessView() + person.getPersonId());
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

    public JavaMailSenderImpl getMailSender() {
        return mailSender;
    }

    public void setMailSender(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public HierarchicalMessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(HierarchicalMessageSource messageSource) {
        this.messageSource = messageSource;
    }


}
