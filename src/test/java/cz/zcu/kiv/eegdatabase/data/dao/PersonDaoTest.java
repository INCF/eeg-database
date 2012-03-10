package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.SimpleGenericDao;
import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
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
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
    protected SessionFactory sessionFactory;

    /**
     * Used as universal DAO for entity manipulation, in case
     * there is no specialized implementation available
     */
    protected HibernateTemplate hibernateTemplate;

    Person person;
    EducationLevel educationLevel;

    @Before
    public void init(){
        hibernateTemplate = createHibernateTemplate(sessionFactory);

        educationLevel = new EducationLevel();
        educationLevel.setDefaultNumber(0);
        educationLevel.setTitle("junit-education-level");
        educationLevel.setEducationLevelId(0);

        person = new Person();
        person.setUsername("junit-test-reader");
        person.setAuthority(Util.ROLE_READER);
        person.setPassword(ControllerUtils.getMD5String(ControllerUtils.getRandomPassword()));
        person.setEmail("junit@test.reader");
        person.setSurname("junit-test-surname");
        person.setGivenname("junit-test-name");
        person.setGender('M');
        person.setLaterality('X');
        person.setEducationLevel(educationLevel);
    }

    public HibernateTemplate createHibernateTemplate(SessionFactory sessionFactory){
        assertNotNull("Session factory must exist",sessionFactory);
        HibernateDaoSupport daoSupport = new HibernateDaoSupport() {};//empty impl
        daoSupport.setSessionFactory(sessionFactory);
        return daoSupport.getHibernateTemplate();
    }

    @Test
    @Transactional
    public void testCreateEducationLevel(){
        init();
        Integer id = (Integer) hibernateTemplate.save(educationLevel);
        assertNotNull(id);
    }

    @Test
    @Transactional
	public void testCreatePerson(){
           //hibernateTemplate.save(educationLevel);
           personDao.create(person);
           assertNotNull(personDao.read(person.getPersonId()));
	}
}
