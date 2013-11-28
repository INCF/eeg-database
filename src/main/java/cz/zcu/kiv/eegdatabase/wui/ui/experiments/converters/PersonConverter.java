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
 *   PersonConverter.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments.converters;

import java.util.List;
import java.util.Locale;

import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.string.Strings;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;

public class PersonConverter implements IConverter<Person> {
    
    private static final long serialVersionUID = -6158291362415634010L;
    private PersonFacade personFacade;

    public PersonConverter(PersonFacade personFacade) {
        this.personFacade = personFacade;
    }

    @Override
    public Person convertToObject(String s, Locale locale) {
        if(Strings.isEmpty(s)){
            return null;
        }

        String[] datas = s.split(" ");
        String name = datas[datas.length -1];

        List<Person> persons = personFacade.readByParameter("username", name);
        Person person = new Person();
        person.setUsername(name);
        
        return (persons != null && persons.size() > 0) ? persons.get(0) : person;
    }

    @Override
    public String convertToString(Person person, Locale locale) {
        return person.getAutoCompleteData();
    }
}
