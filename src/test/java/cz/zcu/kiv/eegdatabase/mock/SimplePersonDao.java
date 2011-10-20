package cz.zcu.kiv.eegdatabase.mock;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.controller.search.SearchRequest;
import org.apache.lucene.queryParser.ParseException;

import java.util.List;
import java.util.Map;

/**
 * Created: 5.5.11
 *
 * @author Jenda Kolena, jendakolena@gmail.com
 * @version 0.1
 */
public class SimplePersonDao implements PersonDao
{
    PersonDao realPersonDao = null;

    public SimplePersonDao()
    {

    }

    public SimplePersonDao(PersonDao realPersonDao)
    {
        this.realPersonDao = realPersonDao;
    }

    public Person getPerson(String userName)
    {
        return (realPersonDao == null) ? null : realPersonDao.getPerson(userName);
    }

    public Person getPersonByHash(String hashCode)
    {
        return (realPersonDao == null) ? null : realPersonDao.getPerson(hashCode);
    }

    public Person getPersonByFbUid(String fbUid)
    {
        return (realPersonDao == null) ? null : realPersonDao.getPerson(fbUid);
    }

    public List<Person> getPersonsWherePendingRequirement()
    {
        return (realPersonDao == null) ? null : realPersonDao.getPersonsWherePendingRequirement();
    }

    public boolean usernameExists(String userName)
    {
        return (realPersonDao == null) ? null : realPersonDao.usernameExists(userName);
    }

    public boolean fbUidExists(String id)
    {
        return (realPersonDao == null) ? null : realPersonDao.fbUidExists(id);
    }

    public boolean emailExists(String email)
    {
        return (realPersonDao == null) ? null : realPersonDao.emailExists(email);
    }

    public List<Person> getSupervisors()
    {
        return (realPersonDao == null) ? null : realPersonDao.getSupervisors();
    }

    public Person getLoggedPerson()
    {
        return MockFactory.createPerson();
    }

    public Map getInfoForAccountOverview(Person loggedPerson)
    {
        return (realPersonDao == null) ? null : realPersonDao.getInfoForAccountOverview(loggedPerson);
    }

    public boolean userNameInGroup(String userName, int groupId)
    {
        return (realPersonDao == null) ? null : realPersonDao.userNameInGroup(userName, groupId);
    }

    public List<Person> getPersonSearchResults(List<SearchRequest> requests)
    {
        return (realPersonDao == null) ? null : realPersonDao.getPersonSearchResults(requests);
    }

    public Integer create(Person newInstance)
    {
        return (realPersonDao == null) ? null : realPersonDao.create(newInstance);
    }

    public Person read(Integer id)
    {
        return (realPersonDao == null) ? null : realPersonDao.read(id);
    }

    public void update(Person transientObject)
    {
        if (realPersonDao != null) realPersonDao.update(transientObject);
    }

    public void delete(Person persistentObject)
    {
        if (realPersonDao != null) realPersonDao.delete(persistentObject);
    }

    public List<Person> getAllRecords()
    {
        return (realPersonDao == null) ? null : realPersonDao.getAllRecords();
    }

    public List<Person> getRecordsAtSides(int first, int max)
    {
        return (realPersonDao == null) ? null : realPersonDao.getRecordsAtSides(first, max);
    }

    public int getCountRecords()
    {
        return (realPersonDao == null) ? 0 : realPersonDao.getCountRecords();
    }

    public Map<Person, String> getFulltextResults(String fullTextQuery) throws ParseException
    {
        return (realPersonDao == null) ? null : realPersonDao.getFulltextResults(fullTextQuery);
    }
}
