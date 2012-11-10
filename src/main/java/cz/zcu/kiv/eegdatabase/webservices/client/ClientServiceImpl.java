package cz.zcu.kiv.eegdatabase.webservices.client;

import cz.zcu.kiv.eegdatabase.data.dao.*;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.activation.DataHandler;
import javax.jws.WebService;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Timestamp;
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
    private PersonOptParamDefDao personOptParamDefDao;
    private DataFileDao dataFileDao;
    private ExperimentDao experimentDao;
    private GenericDao<PersonOptParamVal,PersonOptParamValId> personOptParamValDao;
    private GenericDao<ExperimentOptParamVal,ExperimentOptParamValId> experimentOptParamValDao;
    private GenericDao<FileMetadataParamVal,FileMetadataParamValId> fileMetadataParamValDao;
    private GenericDao<ResearchGroupMembership,ResearchGroupMembershipId> researchGroupMembershipDao;
    private ExperimentOptParamDefDao experimentOptParamDefDao;
    private FileMetadataParamDefDao fileMetadataParamDefDao;
    private ScenarioDao scenarioDao;
    private int newId;
    private static final Log log = LogFactory.getLog(ClientServiceImpl.class);

    public boolean isServiceAvailable() {
            log.debug("User " + personDao.getLoggedPerson().getEmail() +
                        " verified connection with client web service.");
        return true;
    }

    public int addHardware(HardwareInfo info){
        Hardware h = new Hardware();
        h.setDefaultNumber(info.getDefaultNumber());
        h.setDescription(info.getDescription());
        h.setTitle(info.getTitle());
        h.setType(info.getType());
        newId = hardwareDao.create(h);
        for(int groupId : info.getResearchGroupIdList()){
            ResearchGroup r = researchGroupDao.read(groupId);
            hardwareDao.createGroupRel(h,r);
        }
        return newId;
    }

    public int addWeather(WeatherInfo info) {
        Weather w = new Weather();
        w.setDefaultNumber(info.getDefaultNumber());
        w.setDescription(info.getDescription());
        w.setTitle(info.getTitle());
        newId = weatherDao.create(w);
        for(int groupId : info.getResearchGroupIdList()){
            ResearchGroup r = researchGroupDao.read(groupId);
            weatherDao.createGroupRel(w,r);
        }
        return newId;
    }

    @Override
    public int addResearchGroup(ResearchGroupInfo info) {
        ResearchGroup r = new ResearchGroup();
        r.setTitle(info.getTitle());
        r.setDescription(info.getTitle());
        Person p = personDao.getLoggedPerson();
        r.setPerson(p);
        newId = researchGroupDao.create(r);
        p.getResearchGroups().add(r);
        personDao.update(p);
        return newId;
    }

    @Override
    public void addResearchGroupMembership(ResearchGroupMembershipInfo info) {
        try{
        ResearchGroupMembershipId id = new ResearchGroupMembershipId();
        ResearchGroupMembership membership = new ResearchGroupMembership();
        Person p = personDao.read(info.getId().getPersonId());
        ResearchGroup r = researchGroupDao.read(info.getId().getResearchGroupId());
        id.setPersonId(p.getPersonId());
        id.setResearchGroupId(r.getResearchGroupId());
        membership.setId(id);
        membership.setAuthority(info.getAuthority());
        membership.setPerson(p);
        membership.setResearchGroup(r);
        researchGroupMembershipDao.create(membership);
        p.getResearchGroupMemberships().add(membership);
        r.getResearchGroupMemberships().add(membership);
        personDao.update(p);
        researchGroupDao.update(r);
        }catch(Exception e ){
            e.printStackTrace();
        }
    }

    @Override
    public int addPerson(PersonInfo info) {
        Person p = new Person();
        p.setUsername(info.getUsername());
        p.setGender(info.getGender());
        p.setNote(info.getNote());
        p.setSurname(info.getSurname());
        p.setGivenname(info.getGivenname());
        p.setLaterality(info.getLaterality());
        p.setPhoneNumber(info.getPhoneNumber());
        p.setDateOfBirth(new Timestamp(info.getDateOfBirthInMillis()));
        EducationLevel e = educationLevelDao.read(info.getEducationLevelId());
        p.setEducationLevel(e);
        newId = personDao.create(p);
        e.getPersons().add(p);
        educationLevelDao.update(e);
        return newId;
    }

    @Override
    public int addScenario(ScenarioInfo info) {
        return 0;  //TODO To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int addDataFile(DataFileInfo info, DataHandler inputData) throws ClientServiceException {
        DataFile file = new DataFile();
        Experiment e = (Experiment) experimentDao.read(info.getExperimentId());
        file.setExperiment(e);
        file.setDescription(info.getDescription());
        file.setFilename(info.getFileName());
        file.setMimetype(info.getMimeType());

        try {
            if (inputData != null) {
                Blob blob = dataFileDao.createBlob(inputData.getInputStream(), (int) info.getFileLength());
                file.setFileContent(blob);
            }
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
            throw new ClientServiceException(ex);
        }

        int fileId = dataFileDao.create(file);
        e.getDataFiles().add(file);
        experimentDao.update(e);
        log.debug("User " + personDao.getLoggedPerson().getEmail() + " created new data file (primary key " + fileId + ").");
        return fileId;
    }


    @Override
    public int addPersonOptParamDef(PersonOptParamDefInfo info) {
        PersonOptParamDef p = new PersonOptParamDef();
        p.setDefaultNumber(info.getDefaultNumber());
        p.setParamName(info.getParamName());
        p.setParamDataType(info.getParamDataType());
        newId = personOptParamDefDao.create(p);
        for(int groupId : info.getResearchGroupIdList()){
            ResearchGroup r = researchGroupDao.read(groupId);
            personOptParamDefDao.createGroupRel(p,r);
        }
        return newId;
    }

    @Override
    public void addPersonOptParamVal(PersonOptParamValInfo info) {
       PersonOptParamValId id = new PersonOptParamValId(info.getId().getPersonId(),info.getId().getPersonAdditionalParamId());
       PersonOptParamVal val = new PersonOptParamVal();
        val.setId(id);
        val.setPerson(personDao.read(id.getPersonId()));
        val.setPersonOptParamDef(personOptParamDefDao.read(id.getPersonAdditionalParamId()));
        val.setParamValue(info.getParamValue());
        personOptParamValDao.create(val);
    }

    @Override
    public int addExperimentOptParamDef(ExperimentOptParamDefInfo info) {
        ExperimentOptParamDef e = new ExperimentOptParamDef();
        e.setDefaultNumber(info.getDefaultNumber());
        e.setParamName(info.getParamName());
        e.setParamDataType(info.getParamDataType());
        newId = experimentOptParamDefDao.create(e);
        for(int groupId : info.getResearchGroupIdList()){
            ResearchGroup r = researchGroupDao.read(groupId);
            experimentOptParamDefDao.createGroupRel(e,r);
        }
        return newId;
    }

    @Override
    public void addExperimentOptParamVal(ExperimentOptParamValInfo info) {
        ExperimentOptParamValId id = new ExperimentOptParamValId(info.getId().getExperimentId(),info.getId().getExperimentOptParamDefId());
        ExperimentOptParamVal val = new ExperimentOptParamVal();
        val.setId(id);
        val.setExperiment((Experiment) experimentDao.read(id.getExperimentId()));
        val.setExperimentOptParamDef(experimentOptParamDefDao.read(id.getExperimentOptParamDefId()));
        val.setParamValue(info.getParamValue());
        experimentOptParamValDao.create(val);
    }

    @Override
    public int addFileMetadataParamDef(FileMetadataParamDefInfo info) {
        FileMetadataParamDef f = new FileMetadataParamDef();
        f.setDefaultNumber(info.getDefaultNumber());
        f.setParamName(info.getParamName());
        f.setParamDataType(info.getParamDataType());
        newId = fileMetadataParamDefDao.create(f);
        for(int groupId : info.getResearchGroupIdList()){
            ResearchGroup r = researchGroupDao.read(groupId);
            fileMetadataParamDefDao.createGroupRel(f,r);
        }
        return newId;
    }

    @Override
    public void addFileMetadataParamVal(FileMetadataParamValInfo info) {
        FileMetadataParamValId id = new FileMetadataParamValId(info.getId().getFileMetadataParamDefId(),info.getId().getDataFileId());
        FileMetadataParamVal val = new FileMetadataParamVal();
        val.setId(id);
        val.setDataFile(dataFileDao.read(id.getDataFileId()));
        val.setFileMetadataParamDef(fileMetadataParamDefDao.read(id.getFileMetadataParamDefId()));
        val.setMetadataValue(info.getMetadataValue());
        fileMetadataParamValDao.create(val);
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
            i.setEducationLevelId(p.getEducationLevel().getEducationLevelId());
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
            info.setPersonOwner(scenario.getPerson().getPersonId());
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

    @Override
    public List<PersonOptParamValInfo> getPersonOptParamValList(){
        List<PersonOptParamValInfo> infos = new LinkedList<PersonOptParamValInfo>();
        List<PersonOptParamVal> valuesDb = personOptParamValDao.getAllRecords();
        for(PersonOptParamVal o : valuesDb){
            PersonOptParamValInfo i = new PersonOptParamValInfo();
            i.setId(o.getId());
            i.setParamValue(o.getParamValue());
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
                i.setPersonOwner(r.getPerson().getPersonId());
            }
            // adding research group memberships
            List<ResearchGroupMembership> memberships = researchGroupMembershipDao.readByParameter("researchGroup.researchGroupId",r.getResearchGroupId());
            for(ResearchGroupMembership rm : memberships){
                ResearchGroupMembershipInfo ri = new ResearchGroupMembershipInfo();
                ResearchGroupMembershipId id = new ResearchGroupMembershipId();
                id.setPersonId(rm.getId().getPersonId());
                id.setResearchGroupId(rm.getId().getResearchGroupId());
                ri.setAuthority(rm.getAuthority());
                ri.setId(id);
                i.getResearchGroupMembershipInfos().add(ri);
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

    public void setHardwareDao(HardwareDao hardwareDao) {
        this.hardwareDao = hardwareDao;
    }

    public void setWeatherDao(WeatherDao weatherDao) {
        this.weatherDao = weatherDao;
    }

    public void setEducationLevelDao(EducationLevelDao educationLevelDao) {
        this.educationLevelDao = educationLevelDao;
    }

    public void setScenarioDao(ScenarioDao scenarioDao) {
        this.scenarioDao = scenarioDao;
    }

    public void setPersonOptParamDefDao(PersonOptParamDefDao personOptParamDefDao) {
        this.personOptParamDefDao = personOptParamDefDao;
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

    public void setDataFileDao(DataFileDao dataFileDao) {
        this.dataFileDao = dataFileDao;
    }

    public void setExperimentDao(ExperimentDao experimentDao) {
        this.experimentDao = experimentDao;
    }


    public void setExperimentOptParamValDao(SimpleGenericDao<ExperimentOptParamVal, ExperimentOptParamValId> experimentOptParamValDao) {
        this.experimentOptParamValDao = experimentOptParamValDao;
    }

    public void setFileMetadataParamValDao(SimpleGenericDao<FileMetadataParamVal, FileMetadataParamValId> fileMetadataParamValDao) {
        this.fileMetadataParamValDao = fileMetadataParamValDao;
    }

    public void setResearchGroupMembershipDao(GenericDao<ResearchGroupMembership, ResearchGroupMembershipId> researchGroupMembershipDao) {
        this.researchGroupMembershipDao = researchGroupMembershipDao;
    }
}
