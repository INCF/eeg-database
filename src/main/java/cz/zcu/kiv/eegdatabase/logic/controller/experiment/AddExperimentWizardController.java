package cz.zcu.kiv.eegdatabase.logic.controller.experiment;

import cz.zcu.kiv.eegdatabase.data.dao.*;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import cz.zcu.kiv.eegdatabase.logic.util.SignalProcessingUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractWizardFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.validation.ValidationUtils;
import org.springframework.web.servlet.mvc.multiaction.ParameterMethodNameResolver;

/**
 * Wizard for adding experiments
 * User: pbruha
 * Date: 10.3.11
 * Time: 13:38
 */
public class AddExperimentWizardController extends AbstractWizardFormController {

    private Log log = LogFactory.getLog(getClass());
    private GenericDao<Experiment, Integer> experimentDao;
    private ScenarioSchemasDao scenarioSchemasDao;
    private PersonDao personDao;
    private ScenarioDao scenarioDao;
    private HardwareDao hardwareDao;
    private WeatherDao weatherDao;
    private ResearchGroupDao researchGroupDao;
    private AuthorizationManager auth;
    private GenericDao<DataFile, Integer> dataFileDao;
    private ParameterMethodNameResolver methodNameResolver;

    public ParameterMethodNameResolver getMethodNameResolver() {
        return methodNameResolver;
    }

    public void setMethodNameResolver(ParameterMethodNameResolver methodNameResolver) {
        this.methodNameResolver = methodNameResolver;
    }

    public AddExperimentWizardController() {
        setCommandClass(AddExperimentWizardCommand.class);
        setCommandName("addExperimentWizard");
    }

    @Override
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
        ModelAndView mav = super.showForm(request, response, errors);
        if (!auth.userIsExperimenter()) {
            mav.setViewName("redirect:experiments/userNotExperimenter");
        }
        mav.addObject("userIsExperimenter", auth.userIsExperimenter());
        return mav;
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request)
            throws Exception {

        AddExperimentWizardCommand addExperimentWizardCommand = (AddExperimentWizardCommand) super.formBackingObject(request);


        String measurationIdString = request.getParameter("id");
        if (measurationIdString != null) {  // it is a form for editing
            int measurationId = Integer.parseInt(measurationIdString);

            log.debug("Filling backing object with data from measuration object #" + measurationId);
            addExperimentWizardCommand.setMeasurationId(measurationId);
            Experiment measuration = experimentDao.read(measurationId);

            String startDate = ControllerUtils.getDateFormat().format(measuration.getStartTime());
            log.debug("Setting start date = " + startDate);
            addExperimentWizardCommand.setStartDate(startDate);

            String startTime = ControllerUtils.getTimeFormat().format(measuration.getStartTime());
            log.debug("Setting start time = " + startTime);
            addExperimentWizardCommand.setStartTime(startTime);

            String endDate = ControllerUtils.getDateFormat().format(measuration.getEndTime());
            log.debug("Setting start date = " + endDate);
            addExperimentWizardCommand.setEndDate(endDate);

            String endTime = ControllerUtils.getTimeFormat().format(measuration.getEndTime());
            log.debug("Setting start time = " + endTime);
            addExperimentWizardCommand.setEndTime(endTime);

            log.debug("Setting measured person = " + measuration.getPersonBySubjectPersonId().getPersonId());
            addExperimentWizardCommand.setSubjectPerson(measuration.getPersonBySubjectPersonId().getPersonId());

            log.debug("Setting coExperimenters - count of selected coExperimenters: " + measuration.getPersons().size());
            int[] coExperimentersArray = new int[measuration.getPersons().size()];
            int i = 0;
            for (Person experimenter : measuration.getPersons()) {
                log.debug("Adding selected experimenter #" + experimenter.getPersonId());
                coExperimentersArray[i++] = experimenter.getPersonId();
            }
            addExperimentWizardCommand.setCoExperimenters(coExperimentersArray);

            log.debug("Setting selected scenario = " + measuration.getScenario().getScenarioId());
            addExperimentWizardCommand.setScenario(measuration.getScenario().getScenarioId());

            log.debug("Setting list of used hardware - count of selected pieces of hardware: " + measuration.getHardwares().size());
            int[] hardwareArray = new int[measuration.getHardwares().size()];
            int j = 0;
            for (Hardware hardware : measuration.getHardwares()) {
                log.debug("Adding selected hardware #" + hardware.getHardwareId());
                hardwareArray[j++] = hardware.getHardwareId();
            }
            addExperimentWizardCommand.setHardware(hardwareArray);

            log.debug("Setting selected weather #" + measuration.getWeather().getWeatherId());
            addExperimentWizardCommand.setWeather(measuration.getWeather().getWeatherId());

            log.debug("Setting weather note = " + measuration.getWeathernote());
            addExperimentWizardCommand.setWeatherNote(measuration.getWeathernote());

            log.debug("Setting temperature = " + measuration.getTemperature());
            addExperimentWizardCommand.setTemperature("" + measuration.getTemperature());

            addExperimentWizardCommand.setPrivateNote(measuration.isPrivateExperiment());
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate = new Date(System.currentTimeMillis());

        // current time + 1hour in millis
        Date endDate = new Date(System.currentTimeMillis() + 3600 * 1000);
        addExperimentWizardCommand.setStartDate(sdf.format(startDate));
        addExperimentWizardCommand.setEndDate(sdf.format(endDate));
        sdf = new SimpleDateFormat("HH:mm");
        addExperimentWizardCommand.setStartTime(sdf.format(startDate));
        addExperimentWizardCommand.setEndTime(sdf.format(endDate));
        addExperimentWizardCommand.setSamplingRate("1000");

        return addExperimentWizardCommand;

    }

    @Override
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        Map map = new HashMap<String, Object>();
        switch (page) {
            case 0:
                List<Person> personList = personDao.getAllRecords();
                Collections.sort(personList);
                map.put("personList", personList);

                List<ResearchGroup> researchGroupList =
                        researchGroupDao.getResearchGroupsWhereAbleToWriteInto(personDao.getLoggedPerson());
                map.put("researchGroupList", researchGroupList);

                ResearchGroup defaultGroup = personDao.getLoggedPerson().getDefaultGroup();
                int defaultGroupId = (defaultGroup != null) ? defaultGroup.getResearchGroupId() : 0;
                map.put("defaultGroupId", defaultGroupId);
                break;
            case 1:
                List<ResearchGroup> groups = researchGroupDao.getResearchGroupsWhereAbleToWriteInto(personDao.getLoggedPerson());
                map.put("researchGroupList", groups);
                List<ScenarioSchemas> schemaNames = scenarioSchemasDao.getSchemaNames();
                map.put("schemaNamesList", schemaNames);

                ResearchGroup defaultGroup1 = personDao.getLoggedPerson().getDefaultGroup();
                int defaultGroupId1 = (defaultGroup1 != null) ? defaultGroup1.getResearchGroupId() : 0;
                map.put("defaultGroupId", defaultGroupId1);
                List<Scenario> scenarioList = scenarioDao.getAllRecords();
                Collections.sort(scenarioList);
                map.put("scenarioList", scenarioList);

                AddExperimentWizardCommand data = (AddExperimentWizardCommand)command;
                int researchGroupId = data.getResearchGroup();
                List<Hardware> hardwareList = hardwareDao.getRecordsByGroup(researchGroupId);
                List<Weather> weatherList = weatherDao.getRecordsByGroup(researchGroupId);
                map.put("hardwareList", hardwareList);
                map.put("weatherList", weatherList);

                break;
            case 2:
                break;
        }


        return map;
    }

    @Override
    protected ModelAndView processFinish(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object command, BindException e) throws Exception {
        log.debug("Processing measuration form - adding new measuration");
        ModelAndView mav = new ModelAndView("redirect:/experiments/my-experiments.html");

        mav.addObject("userIsExperimenter", auth.userIsExperimenter());

        AddExperimentWizardCommand data = (AddExperimentWizardCommand) command;

        Experiment experiment;


        log.debug("Checking the permission level.");
        if (!auth.userIsExperimenter()) {
            log.debug("User is not experimenter - unable to add experiment. Returning MAV.");
            mav.setViewName("redirect:experiments/userNotExperimenter");
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
            System.out.println("hardwareId "+ hardwareId);
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

        //  log.debug("Creating measuration with ID " + addDataCommand.getMeasurationId());
        //     experiment.setExperimentId(addDataCommand.getMeasurationId());

        log.debug("Creating new Data object.");
        MultipartHttpServletRequest mpRequest = (MultipartHttpServletRequest)httpServletRequest;
    // the map containing file names mapped to files
        Map m = mpRequest.getFileMap();
        Set set = m.keySet();
        for (Object key: set) {
            MultipartFile file = (MultipartFile) m.get(key);
             if (file.getOriginalFilename().endsWith(".zip")) {
                    ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(file.getBytes()));
                    ZipEntry en = zis.getNextEntry();
                    while (en != null) {
                        if (en.isDirectory()) {
                            en = zis.getNextEntry();
                            continue;
                        }
                        DataFile dataFile = new DataFile();
                        dataFile.setExperiment(experiment);
                        String name[] = en.getName().split("/");
                        dataFile.setFilename(name[name.length-1]);
                        data.setSamplingRate(data.getSamplingRate());
                        dataFile.setFileContent(Hibernate.createBlob(SignalProcessingUtils.extractZipEntry(zis)));
                        String[] partOfName = en.getName().split("[.]");
                        dataFile.setMimetype(partOfName[partOfName.length-1]);
                        dataFileDao.create(dataFile);
                        en = zis.getNextEntry();
                    }
                } else {
            DataFile dataFile = new DataFile();
            dataFile.setExperiment(experiment);

            log.debug("Original name of uploaded file: " + file.getOriginalFilename());
            dataFile.setFilename(file.getOriginalFilename());

            log.debug("MIME type of the uploaded file: " + file.getContentType());
            dataFile.setMimetype(file.getContentType());

            log.debug("Parsing the sapmling rate.");
            double samplingRate = Double.parseDouble(data.getSamplingRate());
            dataFile.setSamplingRate(samplingRate);

            log.debug("Setting the binary data to object.");
            dataFile.setFileContent(Hibernate.createBlob(file.getBytes()));

            dataFileDao.create(dataFile);
            log.debug("Data stored into database.");
        }
        }

        log.debug("Returning MAV object");
        return mav;
    }

    @Override
    protected ModelAndView processCancel(HttpServletRequest request,
                                         HttpServletResponse response, Object command, BindException errors)
            throws Exception {
        return new ModelAndView("redirect:/home.html");
    }


    @Override
    protected void validatePage(Object command, Errors errors, int page) {
        AddExperimentWizardCommand data = (AddExperimentWizardCommand) command;


        switch (page) {
            case 0: //if page 1 , go validate with validatePage1Form
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "startDate", "required.date");
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "startTime", "required.time");
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "endDate", "required.date");
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "endTime", "required.time");
                if (data.getResearchGroup() == -1) {
                    // research group not chosen
                    errors.rejectValue("researchGroup", "required.researchGroup");
                } else if (!auth.personAbleToWriteIntoGroup(data.getResearchGroup())) {
                    errors.rejectValue("researchGroup", "invalid.notAbleToAddExperimentInGroup");
                }
                if (data.getSubjectPerson() == -1) {  // measured person not chosen
                    errors.rejectValue("subjectPerson", "required.subjectPerson");
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

                break;
            case 1: //if page 2 , go validate with validatePage2Form
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "temperature", "required.field");
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
                    int temp = Integer.parseInt(data.getTemperature());
                    if (temp < -273) {
                        errors.rejectValue("temperature", "invalid.minTemp");
                    }
                } catch (NumberFormatException e) {
                    errors.rejectValue("temperature", "invalid.temperature");
                }
                break;
            case 2: //if page 3 , go validate with validatePage3Form
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "samplingRate", "required.samplingRate");
                try {
                    double rate = Double.parseDouble(data.getSamplingRate());
                    if (rate <= 0) {
                        errors.rejectValue("samplingRate", "invalid.positiveRate");
                    }
                } catch (NumberFormatException e) {
                    errors.rejectValue("samplingRate", "invalid.invalidRate");
                }
                MultipartFile file = data.getDataFile();
                if (file.getOriginalFilename().length() == 0) {
                    errors.rejectValue("dataFile", "required.dataFile");
                }

                break;
        }

    }

    public boolean supports(Class aClass) {
        return aClass.equals(AddExperimentWizardCommand.class);
    }


    public GenericDao<Experiment, Integer> getExperimentDao() {
        return experimentDao;
    }

    public void setExperimentDao(GenericDao<Experiment, Integer> experimentDao) {
        this.experimentDao = experimentDao;
    }

    public WeatherDao getWeatherDao() {
        return weatherDao;
    }

    public void setWeatherDao(WeatherDao weatherDao) {
        this.weatherDao = weatherDao;
    }

    public HardwareDao getHardwareDao() {
        return hardwareDao;
    }

    public void setHardwareDao(HardwareDao hardwareDao) {
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

    public GenericDao<DataFile, Integer> getDataFileDao() {
        return dataFileDao;
    }

    public void setDataFileDao(GenericDao<DataFile, Integer> dataFileDao) {
        this.dataFileDao = dataFileDao;

    }

    public ScenarioSchemasDao getScenarioSchemasDao() {
        return scenarioSchemasDao;
    }

    public void setScenarioSchemasDao(ScenarioSchemasDao scenarioSchemasDao) {
        this.scenarioSchemasDao = scenarioSchemasDao;
    }
}
