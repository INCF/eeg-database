package cz.zcu.kiv.eegdatabase.logic.controller.experiment;

import cz.zcu.kiv.eegdatabase.data.dao.*;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.data.service.HibernatePersonService;
import cz.zcu.kiv.eegdatabase.data.service.MailService;
import cz.zcu.kiv.eegdatabase.data.service.PersonService;
import cz.zcu.kiv.eegdatabase.data.service.SpringJavaMailService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

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
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private ScenarioDao scenarioDao;
    @Autowired
    private JavaMailSenderImpl mailSender;
    @Autowired
    private HierarchicalMessageSource messageSource;
    @Autowired
    private MailService mailService;
    @Autowired
    private PersonService personService;

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
        AddExperimentWizardCommand data = (AddExperimentWizardCommand)request.getSession().getAttribute("cz.zcu.kiv.eegdatabase.logic.controller.experiment.AddExperimentWizardController.FORM.addExperimentWizard") ;
        ResearchGroup researchGroup = researchGroupDao.read(data.getResearchGroup());
        JSONObject jo = new JSONObject();
        if (!weatherTitle.equals("") && !weatherDescription.equals("") && weatherDao.canSaveNewTitle(weatherTitle,researchGroup.getResearchGroupId())) {
            log.debug("Creating new weather object.");
            weather = new Weather();
            weather.setTitle(weatherTitle);
            weather.setDescription(weatherDescription);
            weather.setDefaultNumber(0);
            weatherDao.create(weather);

            weatherDao.createGroupRel(weather, researchGroup);
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
        AddExperimentWizardCommand data = (AddExperimentWizardCommand)request.getSession().getAttribute("cz.zcu.kiv.eegdatabase.logic.controller.experiment.AddExperimentWizardController.FORM.addExperimentWizard") ;
        ResearchGroup researchGroup = researchGroupDao.read(data.getResearchGroup());
        hardwareTitle = request.getParameter("title");
        hardwareType = request.getParameter("type");
        hardwareDescription = request.getParameter("description");

        JSONObject jo = new JSONObject();
        if (!hardwareTitle.equals("") && !hardwareType.equals("") && !hardwareDescription.equals("") && hardwareDao.canSaveNewTitle(hardwareTitle,researchGroup.getResearchGroupId())) {
            // Creating new
            log.debug("Creating new hardware object.");
            hardware = new Hardware();
            hardware.setTitle(hardwareTitle);
            hardware.setType(hardwareType);
            hardware.setDescription(hardwareDescription);
            hardware.setDefaultNumber(0);
            hardwareDao.create(hardware);
            hardwareDao.createGroupRel(hardware, researchGroup);
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
        try {
            person = personService.createPerson(
                    request.getParameter("givenname"),
                    request.getParameter("surname"),
                    request.getParameter("dateOfBirth"),
                    request.getParameter("email"),
                    request.getParameter("gender"),
                    request.getParameter("phoneNumber"),
                    request.getParameter("note"),
                    null
            );
            mailService.sendRegistrationConfirmMail(person, RequestContextUtils.getLocale(request));
        } catch (MailException e) {
            log.error("E-mail was NOT sent");
            log.error(e);
            e.printStackTrace();
        } catch (ParseException e) {//throw futher? (code will fail anyway)
            log.error(e);
        }

        JSONObject jo = new JSONObject();

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
}


