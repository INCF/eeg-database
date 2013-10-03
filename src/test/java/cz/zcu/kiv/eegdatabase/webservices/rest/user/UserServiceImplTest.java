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
 *   UserServiceImplTest.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.webservices.rest.user;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.webservices.rest.user.wrappers.PersonDataList;
import cz.zcu.kiv.eegdatabase.webservices.rest.user.wrappers.UserInfo;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for testing REST user service.
 *
 * @author Petr Miko
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-context.xml"})
public class UserServiceImplTest {

    @Autowired
    private PersonDao personDao;
    @Autowired
    private UserService userService;

    @Before
    public void setUp() throws Exception {
        //credentials in plain, not really safe, but don't know another way to get logged in person
        Person user = personDao.getPerson("petrmiko@students.zcu.cz");
        List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(user.getAuthority()));
        Authentication auth = new UsernamePasswordAuthenticationToken(user, "heslo", authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }


    @Test
    public void testLogin() throws Exception {
       Person user = personDao.getLoggedPerson();
        UserInfo userInfo = userService.login();

        Assert.assertNotNull(userInfo);
        Assert.assertEquals(user.getGivenname(), userInfo.getName());
        Assert.assertEquals(user.getSurname(), userInfo.getSurname());
        Assert.assertEquals(user.getAuthority(), userInfo.getRights());
    }

    @Test
    public void testGetUsers() throws Exception {
       List<Person> users = new ArrayList<Person>();
       PersonDataList userList = userService.getUsers();

        //ignoring records without username and mail
        for(Person person : personDao.getAllRecords()){
            if(person.getUsername() == null && person.getEmail() == null) continue;
            users.add(person);
        }

        Assert.assertNotNull(userList);
        Assert.assertEquals(users.size(), userList.people.size());
    }
}
