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
        person.setPassword(encoder.encode(ControllerUtils.getRandomPassword()));
        person.setSurname("test-surname");
        person.setGivenname("test-name");
        person.setGender('M');
        person.setLaterality('X');
        return person;
    }
}
