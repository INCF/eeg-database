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
package cz.zcu.kiv.eegdatabase.data.service;


import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.controller.person.AddPersonCommand;
import cz.zcu.kiv.eegdatabase.logic.controller.root.RegistrationCommand;
import cz.zcu.kiv.eegdatabase.logic.controller.social.SocialUser;

import java.text.ParseException;


/**
 * Created by IntelliJ IDEA.
 * User: Jiri Novotny
 * Date: 12.3.12
 * Time: 2:39
 */

public interface PersonService {
    Person createPerson(RegistrationCommand rc) throws ParseException;

    Person createPerson(AddPersonCommand apc) throws ParseException;

    Person createPerson(SocialUser userFb, Integer educationLevelId);

    public Person createPerson(String givenName, String surname, String dateOfBirth, String email,
                           String gender, String phoneNumber, String note, Integer educationLevelId,
                           String plainTextPwd, String authority) throws ParseException;

    /**
     * Creates person with random password and minimal authority
     */
    Person createPerson(String givenName, String surname, String dateOfBirth, String email,
                        String gender, String phoneNumber, String note, Integer educationLevelId) throws ParseException;

    Person createPerson(Person person);
}
