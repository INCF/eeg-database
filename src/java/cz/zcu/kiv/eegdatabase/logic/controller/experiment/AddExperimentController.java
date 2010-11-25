package cz.zcu.kiv.eegdatabase.logic.controller.experiment;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.dao.ScenarioDao;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import cz.zcu.kiv.eegdatabase.logic.controller.util.ControllerUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import java.text.ParseException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Controller which processes form for adding experiment. Processes also
 * the form validation.
 *
 * @author Jindra
 */
public class AddExperimentController
        extends SimpleFormController
        implements Validator {

  private Log log = LogFactory.getLog(getClass());
  private GenericDao<Experiment, Integer> experimentDao;
  private PersonDao personDao;
  private ScenarioDao scenarioDao;
  private GenericDao<Hardware, Integer> hardwareDao;
  private GenericDao<Weather, Integer> weatherDao;
  private ResearchGroupDao researchGroupDao;
  private AuthorizationManager auth;

  public AddExperimentController() {
    setCommandClass(AddExperimentCommand.class);
    setCommandName("addMeasuration");
  }

  @Override
  protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
    ModelAndView mav = super.showForm(request, response, errors);
    if (!auth.userIsExperimenter()) {
      mav.setViewName("experiments/userNotExperimenter");
    }
    mav.addObject("userIsExperimenter", auth.userIsExperimenter());
    return mav;
  }

  @Override
  protected Object formBackingObject(HttpServletRequest request) throws Exception {
    AddExperimentCommand data = (AddExperimentCommand) super.formBackingObject(request);

    String measurationIdString = request.getParameter("id");
    if (measurationIdString != null) {  // it is a form for editing
      int measurationId = Integer.parseInt(measurationIdString);

      log.debug("Filling backing object with data from measuration object #" + measurationId);
      data.setMeasurationId(measurationId);
      Experiment measuration = experimentDao.read(measurationId);

      String startDate = ControllerUtils.getDateFormat().format(measuration.getStartTime());
      log.debug("Setting start date = " + startDate);
      data.setStartDate(startDate);

      String startTime = ControllerUtils.getTimeFormat().format(measuration.getStartTime());
      log.debug("Setting start time = " + startTime);
      data.setStartTime(startTime);

      String endDate = ControllerUtils.getDateFormat().format(measuration.getEndTime());
      log.debug("Setting start date = " + endDate);
      data.setEndDate(endDate);

      String endTime = ControllerUtils.getTimeFormat().format(measuration.getEndTime());
      log.debug("Setting start time = " + endTime);
      data.setEndTime(endTime);

      log.debug("Setting measured person = " + measuration.getPersonBySubjectPersonId().getPersonId());
      data.setSubjectPerson(measuration.getPersonBySubjectPersonId().getPersonId());

      log.debug("Setting coExperimenters - count of selected coExperimenters: " + measuration.getPersons().size());
      int[] coExperimentersArray = new int[measuration.getPersons().size()];
      int i = 0;
      for (Person experimenter : measuration.getPersons()) {
        log.debug("Adding selected experimenter #" + experimenter.getPersonId());
        coExperimentersArray[i++] = experimenter.getPersonId();
      }
      data.setCoExperimenters(coExperimentersArray);

      log.debug("Setting selected scenario = " + measuration.getScenario().getScenarioId());
      data.setScenario(measuration.getScenario().getScenarioId());

      log.debug("Setting list of used hardware - count of selected pieces of hardware: " + measuration.getHardwares().size());
      int[] hardwareArray = new int[measuration.getHardwares().size()];
      int j = 0;
      for (Hardware hardware : measuration.getHardwares()) {
        log.debug("Adding selected hardware #" + hardware.getHardwareId());
        hardwareArray[j++] = hardware.getHardwareId();
      }
      data.setHardware(hardwareArray);

      log.debug("Setting selected weather #" + measuration.getWeather().getWeatherId());
      data.setWeather(measuration.getWeather().getWeatherId());

      log.debug("Setting weather note = " + measuration.getWeathernote());
      data.setWeatherNote(measuration.getWeathernote());

      log.debug("Setting temperature = " + measuration.getTemperature());
      data.setTemperature("" + measuration.getTemperature());

      data.setPrivateNote(measuration.isPrivateExperiment());
    }

    return data;
  }

  @Override
  protected Map referenceData(HttpServletRequest request) throws Exception {
    Map map = new HashMap<String, Object>();

    List<Person> personList = personDao.getAllRecords();
    map.put("personList", personList);

    List<Scenario> scenarioList = scenarioDao.getAllRecords();
    map.put("scenarioList", scenarioList);

    List<Hardware> hardwareList = hardwareDao.getAllRecords();
    map.put("hardwareList", hardwareList);

    List<Weather> weatherList = weatherDao.getAllRecords();
    map.put("weatherList", weatherList);

    List<ResearchGroup> researchGroupList =
            researchGroupDao.getResearchGroupsWhereAbleToWriteInto(personDao.getLoggedPerson());
    map.put("researchGroupList", researchGroupList);

    ResearchGroup defaultGroup = personDao.getLoggedPerson().getDefaultGroup();
    int defaultGroupId = (defaultGroup != null) ? defaultGroup.getResearchGroupId() : 0;
    map.put("defaultGroupId", defaultGroupId);

    return map;
  }

  @Override
  protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindException) throws Exception {
    ModelAndView mav = new ModelAndView(getSuccessView());

    mav.addObject("userIsExperimenter", auth.userIsExperimenter());

    AddExperimentCommand data = (AddExperimentCommand) command;

    Experiment experiment;

    boolean editing = data.getMeasurationId() > 0;

    if (editing) {
      // This is the editation of experiment
      log.debug("Processing measuration form - editing existing measuration");

      log.debug("Checking the permission level.");
      if (!auth.userIsOwnerOrCoexperimenter(data.getMeasurationId())) {
        log.debug("User is not owner or co-experimenter - unable to edit experiment. Returning MAV.");
        mav.setViewName("experiments/unableToEditExperiment");
        return mav;
      }

      log.debug("Loading existing measuration object");
      experiment = experimentDao.read(data.getMeasurationId());

      log.debug("Removing edited experiment from hardwares");
      for (Hardware hardware : experiment.getHardwares()) {
        hardware.getExperiments().remove(experiment);
      }

      log.debug("Removing edited experiment from persons (coexperimenters)");
      for (Person person : experiment.getPersons()) {
        person.getExperiments().remove(experiment);
      }

    } else {
      // This is creating of new experiment
      log.debug("Processing measuration form - adding new measuration");

      log.debug("Checking the permission level.");
      if (!auth.userIsExperimenter()) {
        log.debug("User is not experimenter - unable to add experiment. Returning MAV.");
        mav.setViewName("experiments/userNotExperimenter");
        return mav;
      }

      log.debug("Creating new Measuration object");
      experiment = new Experiment();

      // This assignment is commited only when new experiment is being created
      log.debug("Setting the owner to the logged user.");
      experiment.setPersonByOwnerId(personDao.getLoggedPerson());

      log.debug("Setting the group, which is the new experiment being added into.");
      ResearchGroup researchGroup = new ResearchGroup();
      researchGroup.setResearchGroupId(data.getResearchGroup());
      experiment.setResearchGroup(researchGroup);


    }

    log.debug("Setting Weather object - ID " + data.getWeather());
    Weather weather = new Weather();
    weather.setWeatherId(data.getWeather());
    experiment.setWeather(weather);

    log.debug("Setting Scenario object - ID " + data.getScenario());
    Scenario scenario = new Scenario();
    scenario.setScenarioId(data.getScenario());
    experiment.setScenario(scenario);

    log.debug("Setting Person object (measured person) - ID " + data.getSubjectPerson());
    Person subjectPerson = new Person();
    subjectPerson.setPersonId(data.getSubjectPerson());
    experiment.setPersonBySubjectPersonId(subjectPerson);

    Date startDate = ControllerUtils.getDateFormatWithTime().parse(data.getStartDate() + " " + data.getStartTime());
    experiment.setStartTime(new Timestamp(startDate.getTime()));
    log.debug("Setting start date - " + startDate);

    Date endDate = ControllerUtils.getDateFormatWithTime().parse(data.getEndDate() + " " + data.getEndTime());
    experiment.setEndTime(new Timestamp(endDate.getTime()));
    log.debug("Setting end date - " + endDate);

    log.debug("Setting the temperature - " + data.getTemperature());
    experiment.setTemperature(Integer.parseInt(data.getTemperature()));

    log.debug("Setting the weather note - " + data.getWeatherNote());
    experiment.setWeathernote(data.getWeatherNote());

    log.debug("Started setting the Hardware objects");
    int[] hardwareArray = data.getHardware();
    Set<Hardware> hardwareSet = new HashSet<Hardware>();
    for (int hardwareId : hardwareArray) {
      Hardware tempHardware = hardwareDao.read(hardwareId);
      hardwareSet.add(tempHardware);
      tempHardware.getExperiments().add(experiment);
      log.debug("Added Hardware object - ID " + hardwareId);
    }
    log.debug("Setting Hardware list to Measuration object");
    experiment.setHardwares(hardwareSet);

    log.debug("Started setting the Person objects (coExperimenters)");
    int[] coExperimentersArray = data.getCoExperimenters();
    Set<Person> coExperimenterSet = new HashSet<Person>();
    for (int personId : coExperimentersArray) {
      Person tempExperimenter = personDao.read(personId);
      coExperimenterSet.add(tempExperimenter);
      tempExperimenter.getExperiments().add(experiment);
      log.debug("Added Person object - ID " + tempExperimenter.getPersonId());
    }
    log.debug("Setting Person list to Measuration object");
    experiment.setPersons(coExperimenterSet);

    log.debug("Setting private/public access");
    experiment.setPrivateExperiment(data.isPrivateNote());


    if (data.getMeasurationId() > 0) {  // editing existing measuration
      log.debug("Saving the Measuration object to database using DAO - update()");
      experimentDao.update(experiment);
    } else {  // creating new measuration
      log.debug("Saving the Measuration object to database using DAO - create()");
      experimentDao.create(experiment);
    }


    log.debug("Returning MAV object");
    return mav;
  }

  public boolean supports(Class clazz) {
    return clazz.equals(AddExperimentCommand.class);
  }

  public void validate(Object command, Errors errors) {
    AddExperimentCommand data = (AddExperimentCommand) command;

    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "startDate", "required.date");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "startTime", "required.time");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "endDate", "required.date");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "endTime", "required.time");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "temperature", "required.field");

    if (data.getMeasurationId() > 0) {
      // Edit of existing experiment
      // No special actions yet
    } else {
      // Creating new experiment
      if (data.getResearchGroup() == -1) {
        // research group not chosen
        errors.rejectValue("researchGroup", "required.researchGroup");
      } else if (!auth.personAbleToWriteIntoGroup(data.getResearchGroup())) {
        errors.rejectValue("researchGroup", "invalid.notAbleToAddExperimentInGroup");
      }
    }

    if (data.getSubjectPerson() == -1) {  // measured person not chosen
      errors.rejectValue("subjectPerson", "required.subjectPerson");
    }

    if (data.getScenario() == -1) {  // scenario not selected
      errors.rejectValue("scenario", "required.scenario");
    }

    if (data.getHardware().length == 0) {  // no hardware selected
      errors.rejectValue("hardware", "required.hardware");
    }

    if (data.getWeather() == -1) {  // weather not selected
      errors.rejectValue("weather", "required.weather");
    }

    try {
      ControllerUtils.getDateFormat().parse(data.getStartDate());
    } catch (ParseException ex) {
      errors.rejectValue("startDate", "invalid.date");
    }

    try {
      ControllerUtils.getDateFormat().parse(data.getEndDate());
    } catch (ParseException ex) {
      errors.rejectValue("endDate", "invalid.date");
    }

    try {
      ControllerUtils.getTimeFormat().parse(data.getStartTime());
    } catch (ParseException ex) {
      errors.rejectValue("startTime", "invalid.time");
    }

    try {
      ControllerUtils.getTimeFormat().parse(data.getEndTime());
    } catch (ParseException ex) {
      errors.rejectValue("endTime", "invalid.time");
    }

    try {
      Integer.parseInt(data.getTemperature());
    } catch (NumberFormatException e) {
      errors.rejectValue("temperature", "invalid.temperature");
    }

  }

  public GenericDao<Experiment, Integer> getExperimentDao() {
    return experimentDao;
  }

  public void setExperimentDao(GenericDao<Experiment, Integer> experimentDao) {
    this.experimentDao = experimentDao;
  }

  public GenericDao<Hardware, Integer> getHardwareDao() {
    return hardwareDao;
  }

  public void setHardwareDao(GenericDao<Hardware, Integer> hardwareDao) {
    this.hardwareDao = hardwareDao;
  }

  public PersonDao getPersonDao() {
    return personDao;
  }

  public void setPersonDao(PersonDao personDao) {
    this.personDao = personDao;
  }

  public ScenarioDao getScenarioDao() {
    return scenarioDao;
  }

  public void setScenarioDao(ScenarioDao scenarioDao) {
    this.scenarioDao = scenarioDao;
  }

  public GenericDao<Weather, Integer> getWeatherDao() {
    return weatherDao;
  }

  public void setWeatherDao(GenericDao<Weather, Integer> weatherDao) {
    this.weatherDao = weatherDao;
  }

  public ResearchGroupDao getResearchGroupDao() {
    return researchGroupDao;
  }

  public void setResearchGroupDao(ResearchGroupDao researchGroupDao) {
    this.researchGroupDao = researchGroupDao;
  }

  public AuthorizationManager getAuth() {
    return auth;
  }

  public void setAuth(AuthorizationManager auth) {
    this.auth = auth;
  }
}
