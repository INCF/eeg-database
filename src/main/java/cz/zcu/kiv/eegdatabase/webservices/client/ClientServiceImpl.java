package cz.zcu.kiv.eegdatabase.webservices.client;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.webservices.dataDownload.wrappers.PersonInfo;
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
    private static final Log log = LogFactory.getLog(ClientServiceImpl.class);

    public boolean isServiceAvailable() {
            log.debug("User " + personDao.getLoggedPerson().getEmail() +
                        " verified connection with client web service.");
        return true;
    }

    public List<PersonInfo> getAllPeople() {
        List<PersonInfo> people = new LinkedList<PersonInfo>();
        List<Person> peopleDb = personDao.getAllRecords();

        for (Person subject : peopleDb) {
            PersonInfo person = new PersonInfo();

            person.setPersonId(subject.getPersonId());
            person.setGender(subject.getGender());
            person.setGivenName(subject.getGivenname());
            person.setSurname(subject.getSurname());
            person.setScn(subject.getScn());

            if (subject.getDefaultGroup() != null)
                person.setDefaultGroupId(subject.getDefaultGroup().getResearchGroupId());
            else
                person.setDefaultGroupId(-1);

            people.add(person);
        }

        log.debug("User " + personDao.getLoggedPerson().getEmail() + " retrieved list of people.");
        return people;
    }



    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }
}
