/**
 * *****************************************************************************
 * This file is part of the EEG-database project
 *
 * ==========================================
 *
 * Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 *  ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 *  ***********************************************************************************************************************
 *
 * TestUtils.java, 2014/04/31 00:01 Jan Stebetak
 *****************************************************************************
 */
package cz.zcu.kiv.eegdatabase.data;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Timestamp;

public class TestUtils {


    public static Person createPersonForTesting(String username, String role) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Person person = new Person();
        person.setUsername(username);
        person.setAuthority(role);
        person.setDateOfBirth(new Timestamp(10));
        person.setPassword(encoder.encode("stebjan"));
        person.setSurname("test-surname");
        person.setGivenname("test-name");
        person.setGender('M');
        person.setLaterality('X');
        return person;
    }
}
