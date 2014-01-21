/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   AddExperimentController.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.experiment;

import cz.zcu.kiv.eegdatabase.data.dao.*;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Controller which processes form for adding experiment.
 *
 * @author Jindra
 */
@Controller
@SessionAttributes("addMeasuration")
public class AddExperimentController {
    private Log log = LogFactory.getLog(getClass());
    @Autowired
    @Qualifier("experimentDao")
    private GenericDao<Experiment, Integer> experimentDao;
    @Autowired
    private PersonDao personDao;
    @Autowired
    private ScenarioDao scenarioDao;
    @Autowired
    private WeatherDao weatherDao;
    @Autowired
    private ResearchGroupDao researchGroupDao;
    @Autowired
    private AuthorizationManager auth;
    private AddExperimentValidator addExperimentValidator;

    @Autowired
    public AddExperimentController(AddExperimentValidator addExperimentValidator){
        this.addExperimentValidator = addExperimentValidator;
    }

    /**@RequestMapping(value="experiments/add-experiment.html",method= RequestMethod.GET)
    protected String showAddForm(ModelMap model) {
        if (!auth.userIsExperimenter()) {
            return "experiments/userNotExperimenter";
        }
        model.addAttribute("userIsExperimenter", auth.userIsExperimenter());
        AddExperimentCommand data = new AddExperimentCommand();

        model.addAttribute("addMeasuration",data);
        return "experiments/addExperimentForm";
    }     */

    @RequestMapping(value="experiments/edit.html",method= RequestMethod.GET)
    protected String showEditForm(@RequestParam("id") String measurationIdString, ModelMap model) {
        if (!auth.userIsExperimenter()) {
            return "experiments/userNotExperimenter";
        }
        model.addAttribute("userIsExperimenter", auth.userIsExperimenter());
        AddExperimentCommand data = new AddExperimentCommand();
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

            log.debug("Setting weather note = " + measuration.getEnvironmentNote());
            data.setWeatherNote(measuration.getEnvironmentNote());

            log.debug("Setting temperature = " + measuration.getTemperature());
            data.setTemperature("" + measuration.getTemperature());

            data.setPrivateNote(measuration.isPrivateExperiment());
        }
        model.addAttribute("addMeasuration", data);
        return "experiments/addExperimentForm";
    }

    @ModelAttribute("personList")
    private List<Person> populatePersonList(){
        List<Person> personList = personDao.getAllRecords();
        return personList;
    }

    @ModelAttribute("scenarioList")
    private List<Scenario> populateScenarioList(){
        List<Scenario> scenarioList = scenarioDao.getAllRecords();
        return scenarioList;
    }


    @ModelAttribute("weatherList")
    private List<Weather> populateWeatherList(@RequestParam("id") String idString){
        int experimentId = 0;
        if(idString!=null){
            experimentId = Integer.parseInt(idString) ;
        }
        int groupId = experimentDao.read(experimentId).getResearchGroup().getResearchGroupId();
        List<Weather> list = weatherDao.getRecordsByGroup(groupId);
        return list;
    }

    @ModelAttribute("researchGroupList")
    private List<ResearchGroup> populateResearchGroupList(){
        List<ResearchGroup> researchGroupList = researchGroupDao.getResearchGroupsWhereAbleToWriteInto(personDao.getLoggedPerson());
        return researchGroupList;
    }

    @ModelAttribute("defaultGroupId")
    private int populateDefaultGroupId(){
        ResearchGroup defaultGroup = personDao.getLoggedPerson().getDefaultGroup();
        int defaultGroupId = (defaultGroup != null) ? defaultGroup.getResearchGroupId() : 0;
        return defaultGroupId;
    }

    @ModelAttribute("researchGroupTitle")
    private String fillResearchGroupTitleForExperiment(@RequestParam("id") String idString){
        log.debug("Loading experiment info");
        int experimentId = Integer.parseInt(idString);
        Experiment experiment = (Experiment) experimentDao.read(experimentId);
        return experiment.getResearchGroup().getTitle();
    }

    @RequestMapping(method=RequestMethod.POST)
    protected String onSubmit(@ModelAttribute("addMeasuration") AddExperimentCommand data, BindingResult result, ModelMap model) throws Exception{
        addExperimentValidator.validate(data, result);
        if (result.hasErrors()) {
            return "experiments/addExperimentForm";
        }

        model.addAttribute("userIsExperimenter", (auth.userIsExperimenter()) || (auth.isAdmin()));

        Experiment experiment;

        boolean editing = data.getMeasurationId() > 0;

        if (editing) {
            // This is the editation of experiment
            log.debug("Processing measuration form - editing existing measuration");

            log.debug("Checking the permission level.");
            if ((!auth.userIsOwnerOrCoexperimenter(data.getMeasurationId()))&&(!auth.isAdmin())) {
                log.debug("User is not owner or co-experimenter - unable to edit experiment. Returning MAV.");
                return "experiments/unableToEditExperiment";
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
            if ((!auth.userIsExperimenter())) {
                log.debug("User is not experimenter - unable to add experiment. Returning MAV.");
                return "experiments/userNotExperimenter";
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
        experiment.setEnvironmentNote(data.getWeatherNote());

        log.debug("Started setting the Hardware objects");
        int[] hardwareArray = data.getHardware();
        Set<Hardware> hardwareSet = new HashSet<Hardware>();
        for (int hardwareId : hardwareArray) {
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
        String redirect = "redirect:/experiments/detail.html?experimentId="+experiment.getExperimentId();
        return redirect;
    }

    public boolean supports(Class clazz) {
        return clazz.equals(AddExperimentCommand.class);
    }


    public GenericDao<Experiment, Integer> getExperimentDao() {
        return experimentDao;
    }

    public void setExperimentDao(GenericDao<Experiment, Integer> experimentDao) {
        this.experimentDao = experimentDao;
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

    public WeatherDao getWeatherDao() {
        return weatherDao;
    }

    public void setWeatherDao(WeatherDao weatherDao) {
        this.weatherDao = weatherDao;
    }

    public AddExperimentValidator getAddExperimentValidator() {
        return addExperimentValidator;
    }

    public void setAddExperimentValidator(AddExperimentValidator addExperimentValidator) {
        this.addExperimentValidator = addExperimentValidator;
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
