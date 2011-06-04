package cz.zcu.kiv.eegdatabase.logic.controller.experiment;

import cz.zcu.kiv.eegdatabase.data.dao.HardwareDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ScenarioDao;
import cz.zcu.kiv.eegdatabase.data.dao.WeatherDao;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.text.ParseException;
import java.util.Date;
import java.util.Random;

import net.sf.json.JSONObject;
import org.springframework.web.servlet.support.RequestContextUtils;

import static org.apache.commons.io.IOUtils.toByteArray;

/**
 * Adding new weather, person atd. to database
 * User: pbruha
 * Date: 3.4.11
 * Time: 11:00
 * To change this template use File | Settings | File Templates.
 */
public class WizardAjaxMultiController extends MultiActionController {
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    private WeatherDao weatherDao;
    @Autowired
    private HardwareDao hardwareDao;
    @Autowired
    private PersonDao personDao;
    @Autowired
    private ScenarioDao scenarioDao;
    @Autowired
    private JavaMailSenderImpl mailSender;
    @Autowired
    private HierarchicalMessageSource messageSource;
    private SimpleMailMessage mailMessage;
    private String domain;

    /**
     * Added new weather to database
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public ModelAndView addNewWeather(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ModelAndView mav = new ModelAndView("experiments/JSonView");
        log.debug("WizardAjaxMultiController - Add new weather.");
        String weatherTitle;
        String weatherDescription;
        Weather weather = null;
        weatherTitle = request.getParameter("title");
        weatherDescription = request.getParameter("description");
        JSONObject jo = new JSONObject();
        if (!weatherTitle.equals("") && !weatherDescription.equals("") && weatherDao.canSaveNewDescription(weatherDescription)) {
            log.debug("Creating new weather object.");
            weather = new Weather();
            weather.setTitle(weatherTitle);
            weather.setDescription(weatherDescription);
            weatherDao.create(weather);
            log.debug("Saving attribute - success: true");
            jo.put("success", true);
            log.debug("Saving attribute - weatherId: " + weather.getWeatherId());
            jo.put("weatherId", weather.getWeatherId());
        } else {
            log.debug("Saving attribute - success: false");
            jo.put("success", false);
        }
        log.debug("Saving  JSONObject: " + jo);
        mav.addObject("result", jo);
        return mav;
    }

    /**
     * Method adds new hardware to database
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public ModelAndView addNewHardware(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ModelAndView mav = new ModelAndView("experiments/JSonView");
        log.debug("WizardAjaxMultiController - Add new hardware.");

        Hardware hardware = null;
        String hardwareTitle;
        String hardwareType;
        String hardwareDescription;

        hardwareTitle = request.getParameter("title");
        hardwareType = request.getParameter("type");
        hardwareDescription = request.getParameter("description");

        JSONObject jo = new JSONObject();
        if (!hardwareTitle.equals("") && !hardwareType.equals("") && !hardwareDescription.equals("") && hardwareDao.canSaveTitle(hardwareTitle)) {
            // Creating new
            log.debug("Creating new hardware object.");
            hardware = new Hardware();
            hardware.setTitle(hardwareTitle);
            hardware.setType(hardwareType);
            hardware.setDescription(hardwareDescription);
            hardwareDao.create(hardware);
            log.debug("Saving attribute - success: true");
            jo.put("success", true);
            log.debug("Saving attribute - hardwareId: " + hardware.getHardwareId());
            jo.put("hardwareId", hardware.getHardwareId());
        } else {
            log.debug("Saving attribute - success: false");
            jo.put("success", false);
        }
        log.debug("Saving  JSONObject: " + jo);
        mav.addObject("result", jo);
        return mav;
    }

    /**
     * Adds new person to database
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public ModelAndView addNewPerson(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ModelAndView mav = new ModelAndView("experiments/JSonView");
        log.debug("WizardAjaxMultiController - Add new person.");

        Person person = null;
        String givenname;
        String surname;
        String dateOfBirthS;
        String email;
        String gender;
        String phoneNumber;
        String note;

        givenname = request.getParameter("givenname");
        surname = request.getParameter("surname");
        dateOfBirthS = request.getParameter("dateOfBirth");
        email = request.getParameter("email");
        gender = request.getParameter("gender");

        phoneNumber = request.getParameter("phoneNumber");
        note = request.getParameter("note");

        JSONObject jo = new JSONObject();

        log.debug("Creating new person.");
        person = new Person();

        log.debug("Setting givenname = " + givenname);
        person.setGivenname(givenname);

        log.debug("Setting surname = " + surname);
        person.setSurname(surname);

        Date dateOfBirth = null;
        try {
            dateOfBirth = ControllerUtils.getDateFormat().parse(dateOfBirthS);
        } catch (ParseException e) {
            log.error(e);
        }
        person.setDateOfBirth(new Timestamp(dateOfBirth.getTime()));
        log.debug("Setting date of birth = " + dateOfBirth);

        log.debug("Setting gender = " + gender.charAt(0));
        person.setGender(gender.charAt(0));

        log.debug("Setting email = " + email);
        person.setEmail(email);

        log.debug("Setting phone number = " + phoneNumber);
        person.setPhoneNumber(phoneNumber);

        log.debug("Setting note = " + note);
        person.setNote(note);

        log.debug("Generating username");
        String username = givenname.toLowerCase() + "-" + surname.toLowerCase();

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

        personDao.create(person);

         log.debug("Hashing the username");
        String authHash = ControllerUtils.getMD5String(givenname);

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
        try {
            helper.setTo(email);
        } catch (MessagingException e) {
            log.error(e);
        }
        //helper.setFrom(messageSource.getMessage("registration.email.from",null, RequestContextUtils.getLocale(request)));
        try {
            helper.setSubject(messageSource.getMessage("registration.email.subject", null, RequestContextUtils.getLocale(request)));
        } catch (MessagingException e) {
            log.error(e);
        }
        try {
            helper.setText(emailBody, true);
        } catch (MessagingException e) {
            log.error(e);
        }

        try {
            log.debug("Sending e-mail");
            mailSender.send(mimeMessage);
            log.debug("E-mail was sent");
        } catch (MailException e) {
            log.error("E-mail was NOT sent");
            log.error(e);
            e.printStackTrace();
        }



        log.debug("Saving attribute - success: true");
        jo.put("success", true);
        log.debug("Saving attribute - personId: " + person.getPersonId());
        jo.put("personId", person.getPersonId());

        //log.debug("Generating username");
        //String username = apc.getGivenname().toLowerCase() + "-" + apc.getSurname().toLowerCase();
        log.debug("Saving  JSONObject: " + jo);
        mav.addObject("result", jo);
        return mav;
    }

    public ModelAndView addNewScenario(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ModelAndView mav = new ModelAndView("experiments/JSonView");
        log.debug("WizardAjaxMultiController - Add new scenario.");
        //System.out.println("WizardAjaxMultiController - Add new scenario.");
        JSONObject jo = new JSONObject();
        Scenario scenario = new Scenario();

        int researchGroupId = 0;
        String scenarioTitle = "";
        String scenarioLength = "";
        String scenarioDescription = "";
        String dataFile;
        String privateNote;

        log.debug("Setting logged person.");
        scenario.setPerson(personDao.getLoggedPerson());

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        log.debug("Setting research group.");
        researchGroupId = Integer.parseInt(multipartRequest.getParameter("researchGroup"));
        ResearchGroup group = new ResearchGroup();
        group.setResearchGroupId(researchGroupId);
        scenario.setResearchGroup(group);
        scenarioTitle = multipartRequest.getParameter("scenarioTitle");
        log.debug("Setting scenario title: " + scenarioTitle);
        scenario.setTitle(scenarioTitle);

         scenarioLength = multipartRequest.getParameter("length");
         log.debug("Setting scenario length: " + scenarioLength);
         scenario.setScenarioLength(Integer.parseInt(scenarioLength));

        scenarioDescription = multipartRequest.getParameter("scenarioDescription");
        log.debug("Setting scenario description: " + scenarioDescription);
        scenario.setDescription(scenarioDescription);

        MultipartFile multipartFile = multipartRequest.getFile("dataFile");

         scenario.setScenarioName(scenarioTitle + "text.fileTypeXml");
         scenario.setMimetype("text/xml");
         scenario.getScenarioType().setScenarioXml(Hibernate.createBlob(multipartFile.getBytes()));

        /* researchGroupId = Integer.parseInt(request.getParameter("researchGroup"));
                scenarioTitle = request.getParameter("scenarioTitle");
                scenarioLength = request.getParameter("length");
                scenarioDescription = request.getParameter("scenarioDescription");
                dataFile = request.getParameter("dataFile");
                privateNote = request.getParameter("privateNote");
        */


        log.debug("Setting private/public access");
        scenario.setPrivateScenario(true);
        scenarioDao.create(scenario);
        log.debug("Saving attribute - success: true");
        jo.put("success", true);
        log.debug("Saving attribute - scenarioId: " + scenario.getScenarioId());
        jo.put("personId", scenario.getScenarioId());


        log.debug("Saving  JSONObject: " + jo);
        mav.addObject("result", jo);
        return mav;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public SimpleMailMessage getMailMessage() {
        return mailMessage;
    }

    public void setMailMessage(SimpleMailMessage mailMessage) {
        this.mailMessage = mailMessage;
    }
}


