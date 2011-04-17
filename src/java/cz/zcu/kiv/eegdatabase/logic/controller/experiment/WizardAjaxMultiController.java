package cz.zcu.kiv.eegdatabase.logic.controller.experiment;

import cz.zcu.kiv.eegdatabase.data.dao.HardwareDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.WeatherDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

import net.sf.json.JSONObject;

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
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public ModelAndView addNewPerson(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ModelAndView mav = new ModelAndView("experiments/JSonView");
        log.debug("WizardAjaxMultiController - Add new person.");
        System.out.println("WizardAjaxMultiController - Add new person.");

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
        personDao.create(person);

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
}
