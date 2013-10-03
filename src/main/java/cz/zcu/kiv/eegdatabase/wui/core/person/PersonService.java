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
 *   PersonService.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.person;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.controller.person.AddPersonCommand;
import cz.zcu.kiv.eegdatabase.logic.controller.search.SearchRequest;
import cz.zcu.kiv.eegdatabase.logic.controller.social.SocialUser;
import cz.zcu.kiv.eegdatabase.wui.core.GenericService;

public interface PersonService extends GenericService<Person, Integer> {

    Person getPerson(String userName);

    Person getPersonByHash(String hashCode);

    Person getPersonByFbUid(String fbUid);
    
    Person getPersonForDetail(int id);

    void changeUserPassword(String userName, String newPass);

    boolean isPasswordEquals(String userName, String password);

    void forgottenPassword(Person person);

    List<Person> getPersonsWherePendingRequirement();

    boolean usernameExists(String userName);

    boolean fbUidExists(String id);

    List<Person> getSupervisors();

    Person getLoggedPerson();

    Map getInfoForAccountOverview(Person loggedPerson);

    List<Person> getRecordsNewerThan(long oracleScn);

    boolean userNameInGroup(String userName, int groupId);

    List<Person> getPersonSearchResults(List<SearchRequest> requests);

    int getCountForList();

    List<Person> getDataForList(int start, int limit);
    
    Person createPerson(SocialUser userFb, Integer educationLevelId);
    
    Person createPerson(AddPersonCommand apc) throws ParseException;

    void initialize(Person person);
}
