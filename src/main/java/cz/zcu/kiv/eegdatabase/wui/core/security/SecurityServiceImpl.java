package cz.zcu.kiv.eegdatabase.wui.core.security;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import cz.zcu.kiv.eegdatabase.data.dao.EducationLevelDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import cz.zcu.kiv.eegdatabase.wui.core.dto.FullUserDTO;

public class SecurityServiceImpl implements SecurityService {

    private Log log = LogFactory.getLog(getClass());

    private String DEFAULT_EDUCATION_LEVEL = "NotKnown";
    private final static char DEFAULT_LATERALITY = 'X';

    PersonDao personDAO;

    EducationLevelDao educationLevelDao;

    @Required
    public void setPersonDAO(PersonDao personDAO) {
        this.personDAO = personDAO;
    }

    @Required
    public void setEducationLevelDao(EducationLevelDao educationLevelDao) {
        this.educationLevelDao = educationLevelDao;
    }

    @Override
    @Transactional
    public void createUser(FullUserDTO user) {
        try {
            Person person = new Person();
            person.setGivenname(user.getName());
            person.setSurname(user.getSurname());
            person.setDateOfBirth(parseDate(user.getDateOfBirth()));
            person.setGender(user.getGender().getShortcut());
            person.setUsername(user.getEmail());
            person.setPassword(encodePassword(user.getPassword()));
            person.setLaterality(DEFAULT_LATERALITY);
            person.setEducationLevel(educationLevelDao.read(user.getEducationLevel().getEducationLevelId()));

            log.debug("Setting authority = ROLE_USER");
            person.setAuthority(Util.ROLE_USER);

            createPerson(person);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
    }
    
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
        log.debug("Laterality = " + person.getLaterality());

        personDAO.create(person);
        return person;
    }

    private Timestamp parseDate(String date) throws ParseException {
        Date dateOfBirth = ControllerUtils.getDateFormat().parse(date);
        return new Timestamp(dateOfBirth.getTime());
    }

    private String encodePassword(String plaintextPassword) {
        return new BCryptPasswordEncoder().encode(plaintextPassword);
    }

}
