package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.SimpleGenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.service.PersonService;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: jnovotny
 * Date: 9.3.12
 * Time: 14:30
 */
public class PersonDaoTest extends AbstractDataAccessTest {

    @Autowired
    protected PersonDao personDao;

    @Autowired
    protected EducationLevelDao educationLevelDao;

    @Autowired
    protected PersonService personService;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    Person person;
    EducationLevel educationLevel;
    String password;


    @Before
    public void init(){
        educationLevel = new EducationLevel();
        educationLevel.setDefaultNumber(0);
        educationLevel.setTitle("junit-education-level");
        educationLevel.setEducationLevelId(0);

        password = ControllerUtils.getRandomPassword();
        person = new Person();
        person.setUsername("junit@test.reader");
        person.setAuthority(Util.ROLE_READER);
        person.setPassword(encoder.encode(password));
        person.setSurname("junit-test-surname");
        person.setGivenname("junit-test-name");
        person.setGender('M');
        person.setLaterality('X');
        person.setEducationLevel(educationLevel);
    }

    @Test
    @Transactional
    public void testCreateEducationLevel(){
        int startCount = educationLevelDao.getEducationLevels("junit-education-level").size();
        Integer id = (Integer) educationLevelDao.create(educationLevel);
        assertNotNull(id);
        assertEquals(startCount + 1, educationLevelDao.getEducationLevels("junit-education-level").size());
    }

    @Test
    public void testVerifyPassword(){
        assertTrue(encoder.matches(password, person.getPassword()));
    }

    @Test
    @Transactional
	public void testCreatePerson(){
           //hibernateTemplate.save(educationLevel);
           personDao.create(person);
           assertNotNull(personDao.read(person.getPersonId()));
	}

    @Test
    @Transactional
	public void testCreatePersonByService(){
           person.setEducationLevel(null);//simluate missing education level
           personService.createPerson(person);
           assertNotNull(personDao.read(person.getPersonId()));
	}
}
