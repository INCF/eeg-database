package cz.zcu.kiv.eegdatabase.test.perf.persistentLayer;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: Kabourek
 * Date: 17.5.11
 * Time: 20:04
 * To change this template use File | Settings | File Templates.
 * Test for people service.
 */
public class PeopleServicePerformanceTest extends PerformanceTest {

    @Autowired
    PersonDao personDao;

    private Person person;

    public void createPeopleTest(){
        person = new Person();
        person.setSurname("surname");
        person.setUsername("username");
        personDao.create(person);
    }

}
