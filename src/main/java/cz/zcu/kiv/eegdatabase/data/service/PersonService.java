package cz.zcu.kiv.eegdatabase.data.service;

import com.restfb.types.User;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.controller.person.AddPersonCommand;
import cz.zcu.kiv.eegdatabase.logic.controller.root.RegistrationCommand;

import java.text.ParseException;

/**
 * Created by IntelliJ IDEA.
 * User: Jiri Novotny
 * Date: 12.3.12
 * Time: 2:39
 */
public interface PersonService {
    Person createPerson(RegistrationCommand rc) throws ParseException;

    Person createPerson(AddPersonCommand apc) throws ParseException;

    Person createPerson(User userFb, Integer educationLevelId);

    Person createPerson(String givenName, String surname, String dateOfBirth, String email,
                        String gender, String phoneNumber, String note, Integer educationLevelId
                        , String userName, String plainTextPwd, String authority) throws ParseException;

    /**
     * Creates person with username generated from givenName & userName, random password and minimal authority
     */
    Person createPerson(String givenName, String surname, String dateOfBirth, String email,
                        String gender, String phoneNumber, String note, Integer educationLevelId) throws ParseException;

    Person createPerson(Person person);
}
