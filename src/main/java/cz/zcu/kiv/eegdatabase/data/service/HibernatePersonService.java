package cz.zcu.kiv.eegdatabase.data.service;

import com.restfb.types.User;
import cz.zcu.kiv.eegdatabase.data.dao.EducationLevelDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.logic.controller.person.AddPersonCommand;
import cz.zcu.kiv.eegdatabase.logic.controller.root.RegistrationCommand;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.exception.ParseErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.Normalizer;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Jirka-admin
 * Date: 12.3.12
 * Time: 1:08
 */
public class HibernatePersonService implements PersonService {

    private Log log = LogFactory.getLog(getClass());

    //TODO remove when obsolete (education level enforced or nullable)
    private String DEFAULT_EDUCATION_LEVEL = "NotKnown";

    @Autowired
    private PersonDao personDao;

    @Autowired
    private EducationLevelDao educationLevelDao;

    @Override
    public Person createPerson(RegistrationCommand rc) throws ParseException {
        //copying the data to Person entity
        Person person = new Person();
        person.setGivenname(rc.getGivenname());
        person.setSurname(rc.getSurname());
        person.setDateOfBirth(parseDate(rc.getDateOfBirth()));
        person.setGender(rc.getGender().charAt(0));
        person.setEmail(rc.getEmail());
        person.setUsername(rc.getUsername());
        person.setLaterality('X');
        person.setEducationLevel(educationLevelDao.read(rc.getEducationLevel()));
        person.setPassword(encodePassword(rc.getPassword()));

        //Code specific for this object type
        log.debug("Setting authority = ROLE_USER");
        person.setAuthority(Util.ROLE_USER);

        return createPerson(person);
    }

    @Override
    public Person createPerson(AddPersonCommand apc) throws ParseException {
        //copying the data to Person entity
        Person person = new Person();
        person.setGivenname(apc.getGivenname());
        person.setSurname(apc.getSurname());
        person.setDateOfBirth(parseDate(apc.getDateOfBirth()));
        person.setGender(apc.getGender().charAt(0));
        person.setEmail(apc.getEmail());
        person.setPhoneNumber(apc.getPhoneNumber());
        person.setNote(apc.getNote());
        person.setLaterality(apc.getLaterality().charAt(0));
        person.setEducationLevel(educationLevelDao.read(apc.getEducationLevel()));

        //Code specific for this object type
        person.setUsername(createUniqueUsername(apc.getGivenname(), apc.getSurname()));
        person.setPassword(encodeRandomPassword());
        log.debug("Setting authority to ROLE_READER");
        person.setAuthority(Util.ROLE_READER);

        return createPerson(person);
    }

    @Override
    public Person createPerson(User userFb, Integer educationLevelId) {
        //copying the data to Person entity
        Person person = new Person();
        person.setGivenname(userFb.getFirstName());
        person.setSurname(userFb.getLastName());
        person.setGender(userFb.getGender().toUpperCase().charAt(0));
        person.setEmail(userFb.getEmail());
        person.setDateOfBirth(new Timestamp(userFb.getBirthdayAsDate().getTime()));
        person.setEducationLevel(educationLevelId == null ? null : educationLevelDao.read(educationLevelId));

        //Code specific for this object type
        person.setUsername(createUniqueUsername(userFb.getFirstName(), userFb.getLastName()));
        person.setPassword(encodeRandomPassword());
        log.debug("Setting authority to ROLE_USER");
        person.setAuthority("ROLE_USER");
        log.debug("Setting confirmed");
        person.setConfirmed(true);
        person.setFacebookId(userFb.getId());
        return createPerson(person);
    }

    @Override
    public Person createPerson(String givenName, String surname, String dateOfBirth, String email,
                               String gender, String phoneNumber, String note, Integer educationLevelId,
                               String userName, String plainTextPwd, String authority) throws ParseException {
        Person person = new Person();
        person.setGivenname(givenName);
        person.setSurname(surname);
        person.setDateOfBirth(parseDate(dateOfBirth));
        person.setGender(gender.charAt(0));
        person.setEmail(email);
        person.setPhoneNumber(phoneNumber);
        person.setNote(note);
        person.setUsername(userName);
        person.setPassword(encodePassword(plainTextPwd));
        person.setAuthority(authority);
        person.setEducationLevel(educationLevelId == null ? null : educationLevelDao.read(educationLevelId));

        return createPerson(person);
    }

    @Override
    public Person createPerson(String givenName, String surname, String dateOfBirth, String email,
                               String gender, String phoneNumber, String note, Integer educationLevelId)
            throws ParseException {

        String userName = createUniqueUsername(givenName, surname);
        String plainTextPwd = ControllerUtils.getRandomPassword();
        log.debug("Setting authority to ROLE_READER");
        String authority = Util.ROLE_READER;

        return createPerson(givenName, surname, dateOfBirth,email,gender,phoneNumber,note,educationLevelId,
                userName,plainTextPwd,authority);
    }

    @Override
    public Person createPerson(Person person){
        if(person.getAuthenticationHash() == null){
            log.debug("Hashing the username");
            person.setAuthenticationHash(ControllerUtils.getMD5String(person.getUsername()));
        }
        if(person.getRegistrationDate() == null){
            log.debug("Setting registration date");
            person.setRegistrationDate(new Timestamp(System.currentTimeMillis()));
        }
        if(person.getEducationLevel() == null){//TODO remove if educationLevel is nullable or always set by the caller
            log.info("EducationLevel of " + person.getUsername() + " is not set, trying to assign default value " + DEFAULT_EDUCATION_LEVEL);
            List<EducationLevel> def = educationLevelDao.getEducationLevels(DEFAULT_EDUCATION_LEVEL);
            person.setEducationLevel(def.isEmpty() ? null : def.get(0));
        }
        log.debug("Persisting new Person");
        log.debug("Username = " + person.getUsername());
        log.debug("Givenname = " + person.getGivenname());
        log.debug("Surname = " + person.getSurname());
        log.debug("Date of birth = " + person.getDateOfBirth());
        log.debug("Gender = " + person.getGender());
        log.debug("Email = " + person.getEmail());
        log.debug("Phone number = " + person.getPhoneNumber());
        log.debug("Note = " + person.getNote());
        log.debug("Authority = " + person.getAuthority());

        personDao.create(person);
        return person;
    }

    private String createUniqueUsername(String firstName, String surname) {
        log.debug("Generating unique username");
        String username = firstName.toLowerCase() + "-" + surname.toLowerCase();

        // Removing the diacritical marks
        String decomposed = Normalizer.normalize(username, Normalizer.Form.NFD);
        username = decomposed.replaceAll("[^\\p{ASCII}]", "");

        String tempUsername = username;
        // if the username already exists generate random numbers until some combination is free
        // if the username does not exist, we can use it (so the while is jumped over)
        while (personDao.usernameExists(tempUsername)) {
            Random random = new Random();
            int number = random.nextInt(999) + 1;  // not many users are expected to have the same name and surname, so 1000 numbers is enough
            tempUsername = username + "-" + number;
        }
        return tempUsername;
    }

    private String encodeRandomPassword(){
        log.debug("Generating random password");
        return encodePassword(ControllerUtils.getRandomPassword());
    }

    /**
     * @return password encoded by whatever mechanism is currently used to store passwords
     */
    private String encodePassword(String plaintextPassword){
        String result = ControllerUtils.getMD5String(plaintextPassword);
        //log.debug("Encoded password = " + result);//uncomment if needed
        return result;
    }

    private Timestamp parseDate(String date) throws ParseException {
        Date dateOfBirth = ControllerUtils.getDateFormat().parse(date);
        return new Timestamp(dateOfBirth.getTime());
    }
}
