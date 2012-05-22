package cz.zcu.kiv.eegdatabase.webservices.client;

import cz.zcu.kiv.eegdatabase.data.dao.*;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.jws.WebService;
import java.util.LinkedList;
import java.util.List;

/**
 * @author František Liška
 */
@WebService(endpointInterface = "cz.zcu.kiv.eegdatabase.webservices.client.ClientService")
public class ClientServiceImpl implements ClientService{
    private PersonDao personDao;
    private ResearchGroupDao researchGroupDao;
    private HardwareDao hardwareDao;
    private WeatherDao weatherDao;
    private EducationLevelDao educationLevelDao;
    private ScenarioSchemasDao scenarioSchemasDao;
    private PersonOptParamDefDao personOptParamDefDao;
    private SimpleGenericDao<PersonOptParamVal,PersonOptParamValId> personOptParamValDao;
    private ScenarioDao scenarioDao;
    private static final Log log = LogFactory.getLog(ClientServiceImpl.class);

    public boolean isServiceAvailable() {
            log.debug("User " + personDao.getLoggedPerson().getEmail() +
                        " verified connection with client web service.");
        return true;
    }

    public List<PersonInfo> getPersonList() {
        List<PersonInfo> people = new LinkedList<PersonInfo>();
        List<Person> peopleDb = personDao.getAllRecords();
        for (Person p : peopleDb) {
            PersonInfo i = new PersonInfo();
            i.setPersonId(p.getPersonId());
            i.setGivenname(p.getGivenname());
            i.setSurname(p.getSurname());
            if(p.getDateOfBirth()!=null){
                i.setDateOfBirthInMillis(p.getDateOfBirth().getTime());
            }
            i.setGender(p.getGender());
            i.setPhoneNumber(p.getPhoneNumber());
            if(p.getRegistrationDate()!=null){
                i.setRegistrationDateInMillis(p.getRegistrationDate().getTime());
            }
            i.setNote(p.getNote());
            i.setUsername(p.getUsername());
            i.setEducationLevelTitle(p.getEducationLevel().getTitle());
            i.setLaterality(p.getLaterality());
            people.add(i);
        }
        log.debug("User " + personDao.getLoggedPerson().getEmail() + " retrieved list of people.");
        return people;
    }

    @Override
    public List<ScenarioInfo> getScenarioList() {
        List<Scenario> scenarios = scenarioDao.getAllRecords();
        List<ScenarioInfo> scens = new LinkedList<ScenarioInfo>();

        for (Scenario scenario : scenarios) {
            ScenarioInfo info = new ScenarioInfo();

            info.setDescription(scenario.getDescription());
            info.setMimeType(scenario.getMimetype());
            info.setOwnerUsername(scenario.getPerson().getUsername());
            info.setResearchGroupTitle(scenario.getResearchGroup().getTitle());
            info.setScenarioId(scenario.getScenarioId());
            info.setScenarioLength(scenario.getScenarioLength());
            info.setScenarioName(scenario.getScenarioName());
            info.setPrivateScenario(scenario.isPrivateScenario());
            info.setTitle(scenario.getTitle());

            scens.add(info);
        }
        return scens;
    }

    @Override
    public List<EducationLevelInfo> getEducationLevelList() {
        List<EducationLevelInfo> levels = new LinkedList<EducationLevelInfo>();
        List<EducationLevel> levelsDb = educationLevelDao.getAllRecords();
        for (EducationLevel o : levelsDb) {
            EducationLevelInfo i = new EducationLevelInfo();
            i.setEducationLevelId(o.getEducationLevelId());
            i.setTitle(o.getTitle());
            i.setDefaultNumber(o.getDefaultNumber());
            levels.add(i);
        }
        log.debug("User " + personDao.getLoggedPerson().getEmail() + " retrieved list of education levels.");
        return levels;
    }

    public List<PersonOptParamValInfo> getPersonOptParamValList(){
        List<PersonOptParamValInfo> infos = new LinkedList<PersonOptParamValInfo>();
        List<PersonOptParamVal> valuesDb = personOptParamValDao.getAllRecords();
        for(PersonOptParamVal o : valuesDb){
            PersonOptParamValInfo i = new PersonOptParamValInfo();
            i.setId(o.getId());
            i.setPersonId(o.getPerson().getPersonId());
            i.setParamValue(o.getParamValue());
            i.setPersonOptParamDefId(o.getPersonOptParamDef().getPersonOptParamDefId());
            infos.add(i);
        }
        log.debug("User " + personDao.getLoggedPerson().getEmail() + " retrieved list of person optional parameters.");
        return infos;
    }

    @Override
    public List<ResearchGroupInfo> getResearchGroupList() {
        List<ResearchGroupInfo> groups = new LinkedList<ResearchGroupInfo>();
        List<ResearchGroup> groupsDb = researchGroupDao.getResearchGroupsWhereMember(personDao.getLoggedPerson());
        for(ResearchGroup r : groupsDb) {
            ResearchGroupInfo i = new ResearchGroupInfo();
            i.setResearchGroupId(r.getResearchGroupId());
            i.setTitle(r.getTitle());
            i.setDescription(r.getDescription());
            if(r.getPerson()!=null){
                i.setOwnerUsername(r.getPerson().getEmail());
            }
            // adding hardware
            List<Hardware> hardwareDb = hardwareDao.getRecordsByGroup(r.getResearchGroupId());
            for(Hardware h : hardwareDb) {
                HardwareInfo hi = new HardwareInfo();
                hi.setHardwareId(h.getHardwareId());
                hi.setTitle(h.getTitle());
                hi.setDescription(h.getDescription());
                hi.setType(h.getType());
                hi.setDefaultNumber(h.getDefaultNumber());
                i.getHardwares().add(hi);
            }

            // adding weather
            List<Weather> weatherDb = weatherDao.getRecordsByGroup(r.getResearchGroupId());
            for(Weather w : weatherDb) {
                WeatherInfo wi = new WeatherInfo();
                wi.setWeatherId(w.getWeatherId());
                wi.setTitle(w.getTitle());
                wi.setDescription(w.getDescription());
                wi.setDefaultNumber(w.getDefaultNumber());
                i.getWeathers().add(wi);
            }

            // adding optional parameter for people
            List<PersonOptParamDef> personParamDb = personOptParamDefDao.getRecordsByGroup(r.getResearchGroupId());
            for(PersonOptParamDef pp : personParamDb) {
                PersonOptParamDefInfo pi = new PersonOptParamDefInfo();
                pi.setParamName(pp.getParamName());
                pi.setPersonOptParamDefId(pp.getPersonOptParamDefId());
                pi.setDefaultNumber(pp.getDefaultNumber());
                pi.setParamDataType(pi.getParamDataType());
                i.getPersonOptParamDefInfos().add(pi);
            }

            groups.add(i);
        }
        log.debug("User " + personDao.getLoggedPerson().getEmail() + " retrieved list of filled research groups.");
        return groups;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<ScenarioSchemasInfo> getScenarioSchemasList() {
        List<ScenarioSchemasInfo> infos = new LinkedList<ScenarioSchemasInfo>();
        List<ScenarioSchemas> levelsDb = scenarioSchemasDao.getAllRecords();
        for (ScenarioSchemas o : levelsDb) {
            if(o.getApproved()=='y'){
                ScenarioSchemasInfo i = new ScenarioSchemasInfo();
                i.setApproved(o.getApproved());
                i.setDescription(o.getDescription());
                i.setSchemaId(o.getSchemaId());
                i.setSchemaName(o.getSchemaName());
                infos.add(i);
            }
        }
        log.debug("User " + personDao.getLoggedPerson().getEmail() + " retrieved list of scenario schemas.");
        return infos;
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    public ResearchGroupDao getResearchGroupDao() {
        return researchGroupDao;
    }

    public void setResearchGroupDao(ResearchGroupDao researchGroupDao) {
        this.researchGroupDao = researchGroupDao;
    }

    public HardwareDao getHardwareDao() {
        return hardwareDao;
    }

    public void setHardwareDao(HardwareDao hardwareDao) {
        this.hardwareDao = hardwareDao;
    }

    public WeatherDao getWeatherDao() {
        return weatherDao;
    }

    public void setWeatherDao(WeatherDao weatherDao) {
        this.weatherDao = weatherDao;
    }

    public void setEducationLevelDao(EducationLevelDao educationLevelDao) {
        this.educationLevelDao = educationLevelDao;
    }

    public void setScenarioSchemasDao(ScenarioSchemasDao scenarioSchemasDao) {
        this.scenarioSchemasDao = scenarioSchemasDao;
    }

    public void setScenarioDao(ScenarioDao scenarioDao) {
        this.scenarioDao = scenarioDao;
    }

    public PersonOptParamDefDao getPersonOptParamDefDao() {
        return personOptParamDefDao;
    }

    public void setPersonOptParamDefDao(PersonOptParamDefDao personOptParamDefDao) {
        this.personOptParamDefDao = personOptParamDefDao;
    }

    public SimpleGenericDao<PersonOptParamVal, PersonOptParamValId> getPersonOptParamValDao() {
        return personOptParamValDao;
    }

    public void setPersonOptParamValDao(SimpleGenericDao<PersonOptParamVal, PersonOptParamValId> personOptParamValDao) {
        this.personOptParamValDao = personOptParamValDao;
    }
}
