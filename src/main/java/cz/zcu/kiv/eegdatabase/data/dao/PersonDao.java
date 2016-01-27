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
 *   PersonDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.search.SearchRequest;

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
    
    public Person getPersonForDetail(int id);

    public List<Person> getPersonsWherePendingRequirement();

    public boolean usernameExists(String userName);

    public boolean fbUidExists(String id);

    public List<Person> getSupervisors();

    public Person getLoggedPerson();

    public Map getInfoForAccountOverview(Person loggedPerson);

    public boolean userNameInGroup(String userName, int groupId);

    public List<Person> getPersonSearchResults(List<SearchRequest> requests);

    public int getCountForList();

    List<Person> getDataForList(int start, int limit);

    public void initialize(Person person);
}
