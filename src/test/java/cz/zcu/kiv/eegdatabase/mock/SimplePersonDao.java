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
 *   SimplePersonDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.mock;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.SimpleGenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.controller.search.SearchRequest;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created: 5.5.11
 *
 * @author Jenda Kolena, jendakolena@gmail.com
 * @version 0.1
 */
public class SimplePersonDao extends SimpleGenericDao<Person, Integer> implements PersonDao
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

    public List<Person> getRecordsNewerThan(long oracleScn) {
        String hqlQuery = "from Person p where p.scn > :oracleScn";
        List<Person> list = getHibernateTemplate().findByNamedParam(hqlQuery, "oracleScn", oracleScn);
        return list;
    }

    public boolean userNameInGroup(String userName, int groupId)
    {
        return (realPersonDao == null) ? null : realPersonDao.userNameInGroup(userName, groupId);
    }

    public List<Person> getPersonSearchResults(List<SearchRequest> requests)
    {
        return (realPersonDao == null) ? null : realPersonDao.getPersonSearchResults(requests);
    }

    @Override
    public int getCountForList() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List getDataForList(int start, int limit) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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

    public Map<Person, String> getFulltextResults(String fullTextQuery) throws ParseException {
        return (realPersonDao == null) ? null : realPersonDao.getFulltextResults(fullTextQuery);
    }

    @Override
    public Person getPersonForDetail(int id) {
        return (realPersonDao == null) ? null : realPersonDao.getPersonForDetail(id);
    }

    @Override
    public void initialize(Person person){
        realPersonDao.initialize(person);
    }
}
