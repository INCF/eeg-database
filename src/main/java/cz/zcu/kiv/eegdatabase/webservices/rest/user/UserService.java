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
 *   UserService.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.webservices.rest.user;

import cz.zcu.kiv.eegdatabase.webservices.rest.common.exception.RestServiceException;
import cz.zcu.kiv.eegdatabase.webservices.rest.user.wrappers.PersonData;
import cz.zcu.kiv.eegdatabase.webservices.rest.user.wrappers.PersonDataList;
import cz.zcu.kiv.eegdatabase.webservices.rest.user.wrappers.UserInfo;

import java.util.Locale;

/**
 * Interface of service, which provides basic operations upon user part of eeg database, eg. login or creating new users.
 *
 * @author Petr Miko
 */
public interface UserService {

    /**
     * Creates new user inside eeg base.
     *
     * @param registrationPath URL of registration confirmation page
     * @param personData       new person/user information
     * @param locale           used locale
     * @return basic information about created person/user
     * @throws RestServiceException error during person/user creation process
     */
    public PersonData create(String registrationPath, PersonData personData, Locale locale) throws RestServiceException;

    /**
     * Method for verifying user's credentials.
     *
     * @return basic information about accessing person/user
     */
    public UserInfo login();

    /**
     * Method for obtaining eeg base user list.
     *
     * @return users of eeg base
     */
    public PersonDataList getUsers();
}
