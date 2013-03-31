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
        Assert.assertEquals(users.size(), userList.getPeople().size());
    }
}
