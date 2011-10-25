package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.controller.search.SearchRequest;

import java.util.List;
import java.util.Map;

/**
 * DAO for fetching and saving objects with people.
 *
 * @author Jindrich Pergler
 */
public interface PersonDao extends GenericDao<Person, Integer> {

    public Person getPerson(String userName);

    public Person getPersonByHash(String hashCode);

    public Person getPersonByFbUid(String fbUid);

    public List<Person> getPersonsWherePendingRequirement();

    public boolean usernameExists(String userName);

    public boolean fbUidExists(String id);

    public boolean emailExists(String email);

    public List<Person> getSupervisors();

    public Person getLoggedPerson();

    public Map getInfoForAccountOverview(Person loggedPerson);

    public List<Person> getRecordsNewerThan(long oracleScn);

    public boolean userNameInGroup(String userName, int groupId);

    public List<Person> getPersonSearchResults(List<SearchRequest> requests);


}
