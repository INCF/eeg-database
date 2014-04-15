package cz.zcu.kiv.eegdatabase.data;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by stebjan on 15.4.14.
 */
public class TestUtils {


    public static Person createReaderPersonForTesting() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Person person = new Person();
        person.setUsername("test1@test.test");
        person.setAuthority(Util.ROLE_READER);
        person.setPassword(encoder.encode(ControllerUtils.getRandomPassword()));
        person.setSurname("test-surname");
        person.setGivenname("test-name");
        person.setGender('M');
        person.setLaterality('X');
        return person;
    }

    public static Person createAdminPersonForTesting() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Person person = new Person();
        person.setUsername("test1@test.test");
        person.setAuthority(Util.ROLE_ADMIN);
        person.setPassword(encoder.encode(ControllerUtils.getRandomPassword()));
        person.setSurname("test-surname");
        person.setGivenname("test-name");
        person.setGender('M');
        person.setLaterality('X');
        return person;
    }
}
