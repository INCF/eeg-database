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
 *   PersonMapper.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.person;

import java.sql.Timestamp;
import java.util.Date;

import org.joda.time.DateTime;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.wui.core.Gender;
import cz.zcu.kiv.eegdatabase.wui.core.dto.FullPersonDTO;

public class PersonMapper {

    public FullPersonDTO convertToDTO(Person person) {

        FullPersonDTO dto = new FullPersonDTO();
        dto.setId(person.getPersonId());
        dto.setName(person.getGivenname());
        dto.setSurname(person.getSurname());
        dto.setDateOfBirth(person.getDateOfBirth() == null ? null : person.getDateOfBirth());
        dto.setEmail(person.getUsername().toLowerCase());
        dto.setUsername(person.getUsername());
        dto.setGender(Gender.getGenderByShortcut(person.getGender()));
        dto.setConfirmed(person.isConfirmed());
        dto.setRegistrationDate(new DateTime(person.getRegistrationDate().getTime()));

        dto.setEducationLevel(person.getEducationLevel());
        dto.setLaterality(person.getLaterality());
        dto.setAuthority(person.getAuthority());

        return dto;
    }

    public Person convertToEntity(FullPersonDTO dto, Person person) {

        person.setPersonId(dto.getId());
        person.setGivenname(dto.getName());
        person.setSurname(dto.getSurname());
        person.setDateOfBirth(new Timestamp(dto.getDateOfBirth().getTime()));
        person.setUsername(dto.getEmail().toLowerCase());
        person.setGender(dto.getGender().getShortcut());
        person.setConfirmed(dto.isConfirmed());
        person.setRegistrationDate(new Timestamp(dto.getRegistrationDate().getMillis()));
        person.setLaterality(dto.getLaterality());
        person.setAuthority(dto.getAuthority());
        person.setPassword(dto.getPassword());
        person.setEducationLevel(dto.getEducationLevel());

        return person;
    }
}
