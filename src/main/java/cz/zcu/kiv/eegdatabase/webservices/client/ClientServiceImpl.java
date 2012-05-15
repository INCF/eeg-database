package cz.zcu.kiv.eegdatabase.webservices.client;

import cz.zcu.kiv.eegdatabase.data.dao.HardwareDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.dao.WeatherDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.HardwareInfo;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.PersonInfo;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.ResearchGroupInfo;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.WeatherInfo;
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
    private static final Log log = LogFactory.getLog(ClientServiceImpl.class);

    public boolean isServiceAvailable() {
            log.debug("User " + personDao.getLoggedPerson().getEmail() +
                        " verified connection with client web service.");
        return true;
    }

    public List<PersonInfo> getAllPeople() {
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
            i.setLaterality(p.getLaterality());
            people.add(i);
        }
        log.debug("User " + personDao.getLoggedPerson().getEmail() + " retrieved list of people.");
        return people;
    }


    @Override
    public List<ResearchGroupInfo> getMyResearchGroups() {
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
}
