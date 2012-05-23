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
    private ExperimentOptParamDefDao experimentOptParamDefDao;
    private FileMetadataParamDefDao fileMetadataParamDefDao;
    private ScenarioDao scenarioDao;
    private static final Log log = LogFactory.getLog(ClientServiceImpl.class);

    public boolean isServiceAvailable() {
            log.debug("User " + personDao.getLoggedPerson().getEmail() +
                        " verified connection with client web service.");
        return true;
    }

    public int createHardware(HardwareInfo info){
        Hardware h = new Hardware();
        h.setDefaultNumber(info.getDefaultNumber());
        h.setDescription(info.getDescription());
        h.setTitle(info.getTitle());
        h.setType(info.getType());
        int newId = hardwareDao.create(h);
        for(int groupId : info.getResearchGroupIdList()){
            ResearchGroup r = researchGroupDao.read(groupId);
            hardwareDao.createGroupRel(h,r);
        }
        return newId;
    }

    public int createWeather(WeatherInfo info) {
        Weather w = new Weather();
        w.setDefaultNumber(info.getDefaultNumber());
        w.setDescription(info.getDescription());
        w.setTitle(info.getTitle());
        int newId = weatherDao.create(w);
        for(int groupId : info.getResearchGroupIdList()){
            ResearchGroup r = researchGroupDao.read(groupId);
            weatherDao.createGroupRel(w,r);
        }
        return newId;
    }

    @Override
    public int createPersonOptParamDef(PersonOptParamDefInfo info) {
        PersonOptParamDef p = new PersonOptParamDef();
        p.setDefaultNumber(info.getDefaultNumber());
        p.setParamName(info.getParamName());
        p.setParamDataType(info.getParamDataType());
        int newId = personOptParamDefDao.create(p);
        for(int groupId : info.getResearchGroupIdList()){
            ResearchGroup r = researchGroupDao.read(groupId);
            personOptParamDefDao.createGroupRel(p,r);
        }
        return newId;
    }

    @Override
    public int createExperimentOptParamDef(ExperimentOptParamDefInfo info) {
        ExperimentOptParamDef e = new ExperimentOptParamDef();
        e.setDefaultNumber(info.getDefaultNumber());
        e.setParamName(info.getParamName());
        e.setParamDataType(info.getParamDataType());
        int newId = experimentOptParamDefDao.create(e);
        for(int groupId : info.getResearchGroupIdList()){
            ResearchGroup r = researchGroupDao.read(groupId);
            experimentOptParamDefDao.createGroupRel(e,r);
        }
        return newId;
    }

    @Override
    public int createFileMetadataParamDef(FileMetadataParamDefInfo info) {
        FileMetadataParamDef f = new FileMetadataParamDef();
        f.setDefaultNumber(info.getDefaultNumber());
        f.setParamName(info.getParamName());
        f.setParamDataType(info.getParamDataType());
        int newId = fileMetadataParamDefDao.create(f);
        for(int groupId : info.getResearchGroupIdList()){
            ResearchGroup r = researchGroupDao.read(groupId);
            fileMetadataParamDefDao.createGroupRel(f,r);
        }
        return newId;
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

            // adding optional parameter for experiments
            List<ExperimentOptParamDef> expParamDb = experimentOptParamDefDao.getRecordsByGroup(r.getResearchGroupId());
            for(ExperimentOptParamDef ed : expParamDb) {
                ExperimentOptParamDefInfo ei = new ExperimentOptParamDefInfo();
                ei.setParamName(ed.getParamName());
                ei.setExperimentOptParamDefId(ed.getExperimentOptParamDefId());
                ei.setDefaultNumber(ed.getDefaultNumber());
                ei.setParamDataType(ed.getParamDataType());
                i.getExperimentOptParamDefInfos().add(ei);
            }

            // adding file metadata
            List<FileMetadataParamDef> fileMetadataDb = fileMetadataParamDefDao.getRecordsByGroup(r.getResearchGroupId());
            for(FileMetadataParamDef fd : fileMetadataDb) {
                FileMetadataParamDefInfo fi = new FileMetadataParamDefInfo();
                fi.setParamName(fd.getParamName());
                fi.setFileMetadataParamDefId(fd.getFileMetadataParamDefId());
                fi.setDefaultNumber(fd.getDefaultNumber());
                fi.setParamDataType(fd.getParamDataType());
                i.getFileMetadataParamDefInfos().add(fi);
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

    public void setExperimentOptParamDefDao(ExperimentOptParamDefDao experimentOptParamDefDao) {
        this.experimentOptParamDefDao = experimentOptParamDefDao;
    }

    public void setFileMetadataParamDefDao(FileMetadataParamDefDao fileMetadataParamDefDao) {
        this.fileMetadataParamDefDao = fileMetadataParamDefDao;
    }
}
