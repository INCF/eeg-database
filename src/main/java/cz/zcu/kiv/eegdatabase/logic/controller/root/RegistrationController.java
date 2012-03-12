package cz.zcu.kiv.eegdatabase.logic.controller.root;

import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.ImageCaptchaService;
import cz.zcu.kiv.eegdatabase.data.dao.EducationLevelDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.service.HibernatePersonService;
import cz.zcu.kiv.eegdatabase.data.service.MailService;
import cz.zcu.kiv.eegdatabase.data.service.PersonService;
import cz.zcu.kiv.eegdatabase.data.service.SpringJavaMailService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.mail.MailException;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrationController extends SimpleFormController {

    private Log log = LogFactory.getLog(getClass());
    private PersonDao personDao;
    private EducationLevelDao educationLevelDao;
    private HierarchicalMessageSource messageSource;
    private MailService mailService;
    private PersonService personService;
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

        Person person = personService.createPerson(rc);

        try {
            mailService.sendRegistrationConfirmMail(person, RequestContextUtils.getLocale(request));
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

    public void setCaptchaService(ImageCaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    public void setCaptchaResponseParameterName(String captchaResponseParameterName) {
        this.captchaResponseParameterName = captchaResponseParameterName;
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
