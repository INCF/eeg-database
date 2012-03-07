package cz.zcu.kiv.eegdatabase.logic.controller.root;

import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.ImageCaptchaService;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrationController extends SimpleFormController {

    private Log log = LogFactory.getLog(getClass());
    private PersonDao personDao;
    private GenericDao<EducationLevel, Integer> educationLevelDao;
    private JavaMailSenderImpl mailSender;
    private SimpleMailMessage mailMessage;
    private HierarchicalMessageSource messageSource;
    private String domain;
    private static final String DEFAULT_CAPTCHA_RESPONSE_PARAMETER_NAME = "j_captcha_response";
    protected ImageCaptchaService captchaService;
    protected String captchaResponseParameterName = DEFAULT_CAPTCHA_RESPONSE_PARAMETER_NAME;

    public RegistrationController() {
        setCommandClass(RegistrationCommand.class);
        setCommandName("registration");
    }

    @Override
    protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors) throws Exception {
        validateCaptcha(request, errors);
    }
     @Override
     protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
         Map map = new HashMap<String, Object>();
         List<EducationLevel> list = educationLevelDao.getAllRecords();
         map.put("education", list);
         return map;
     }


    //validates captch image text
    protected void validateCaptcha(HttpServletRequest request, BindException errors) {
        boolean isResponseCorrect = false;
        String captchaId = request.getSession().getId();
        String response = request.getParameter(captchaResponseParameterName);
        log.debug("captchaId " + captchaId);
        log.debug("captha response " + response);
        try {
            if (response != null) {
                isResponseCorrect =
                        captchaService.validateResponseForID(captchaId, response);
            }
        } catch (CaptchaServiceException e) {
            log.error(e);
        }
        if (!isResponseCorrect) {
            String objectName = "Captcha";
            String[] codes = {"invalid"};
            Object[] arguments = {};
            String defaultMessage = "Invalid image test entered!";
            ObjectError oe = new ObjectError(objectName, codes, arguments, defaultMessage);
            errors.addError(oe);
        }
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindException) throws Exception {
        RegistrationCommand rc = (RegistrationCommand) command;

        log.debug("Creating new person.");
        Person person = new Person();

        log.debug("Setting givenname = " + rc.getGivenname());
        person.setGivenname(rc.getGivenname());

        log.debug("Setting surname = " + rc.getSurname());
        person.setSurname(rc.getSurname());

        Date dateOfBirth = ControllerUtils.getDateFormat().parse(rc.getDateOfBirth());
        person.setDateOfBirth(new Timestamp(dateOfBirth.getTime()));
        log.debug("Setting date of birth = " + dateOfBirth);

        log.debug("Setting gender = " + rc.getGender());
        person.setGender(rc.getGender().charAt(0));

        log.debug("Setting email = " + rc.getEmail());
        person.setEmail(rc.getEmail());

        log.debug("Setting username = " + rc.getUsername());
        person.setUsername(rc.getUsername());

        String passwordHash = ControllerUtils.getMD5String(rc.getPassword());
        log.debug("Setting password = " + passwordHash);
        person.setPassword(passwordHash);

        log.debug("Setting authority = ROLE_USER");
        person.setAuthority(Util.ROLE_USER);

        log.debug("Setting registration date");
        person.setRegistrationDate(new Timestamp(System.currentTimeMillis()));


        log.debug("Hashing the username");
        String authHash = ControllerUtils.getMD5String(rc.getUsername());

        log.debug("Setting authentication hash code");
        person.setAuthenticationHash(authHash);

        person.setLaterality('X');
        person.setEducationLevel(educationLevelDao.read(rc.getEducationLevel()));

        log.debug("Creating new Person object");
        personDao.create(person);

        String userName = "<b>" + rc.getUsername() + "</b>";
        String password = "<b>" + rc.getPassword() + "</b>";

        log.debug("Creating email content");
        String emailBody = "<html><body>";

        emailBody += "<h4>" + messageSource.getMessage("registration.email.body.hello",
                null, RequestContextUtils.getLocale(request)) + "</h4>";

        emailBody += "<p>" + messageSource.getMessage("registration.email.body.text.part1",
                null, RequestContextUtils.getLocale(request)) + "</p>";

        emailBody += "<p>" + messageSource.getMessage("registration.email.body.text.part2",
                new String[]{userName}, RequestContextUtils.getLocale(request)) + "<br/>";

        emailBody += messageSource.getMessage("registration.email.body.text.part3",
                new String[]{password}, RequestContextUtils.getLocale(request)) + "</p>";

        emailBody += "<p>" + messageSource.getMessage("registration.email.body.text.part4",
                null, RequestContextUtils.getLocale(request)) + "<br/>";


        emailBody +=
                "<a href=\"http://" + domain + "/registration-confirmation.html?activation=" + authHash + "\">"
                        + "http://" + domain + "/registration-confirmation.html?activation=" + authHash + ""
                        + "</a>"
                        + "</p>";

        emailBody += "</body></html>";

        log.debug("email body: " + emailBody);


        log.debug("Composing e-mail message");
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
        message.setFrom(mailMessage.getFrom());

        //  message.setContent("text/html");
        message.setTo(rc.getEmail());
        //helper.setFrom(messageSource.getMessage("registration.email.from", null, RequestContextUtils.getLocale(request)));
        message.setSubject(messageSource.getMessage("registration.email.subject", null, RequestContextUtils.getLocale(request)));
        message.setText(emailBody, true);

        try {
            log.debug("Sending e-mail" + message);
            log.debug("mailSender" + mailSender);
            log.debug("smtp " + mailSender.getHost());
            mailSender.send(mimeMessage);
            log.debug("E-mail was sent");
        } catch (MailException e) {
            log.error("E-mail was NOT sent");
            log.error(e);
            e.printStackTrace();
        }

        log.debug("Returning MAV");
        ModelAndView mav = new ModelAndView(getSuccessView());
        mav.addObject("personEmail", person.getEmail());
        return mav;
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    public GenericDao<EducationLevel, Integer> getEducationLevelDao() {
        return educationLevelDao;
    }

    public void setEducationLevelDao(GenericDao<EducationLevel, Integer> educationLevelDao) {
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

    public void setCaptchaService(ImageCaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    public void setCaptchaResponseParameterName(String captchaResponseParameterName) {
        this.captchaResponseParameterName = captchaResponseParameterName;
    }

    /**
     * @return the mailMessage
     */
    public SimpleMailMessage getMailMessage() {
        return mailMessage;
    }

    /**
     * @param mailMessage the mailMessage to set
     */
    public void setMailMessage(SimpleMailMessage mailMessage) {
        this.mailMessage = mailMessage;
    }

}
