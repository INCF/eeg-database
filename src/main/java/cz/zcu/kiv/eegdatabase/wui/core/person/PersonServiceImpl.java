package cz.zcu.kiv.eegdatabase.wui.core.person;

import cz.zcu.kiv.eegdatabase.data.dao.EducationLevelDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.service.MailService;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.logic.controller.person.AddPersonCommand;
import cz.zcu.kiv.eegdatabase.logic.controller.search.SearchRequest;
import cz.zcu.kiv.eegdatabase.logic.controller.social.SocialUser;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public class PersonServiceImpl implements PersonService {

    private Log log = LogFactory.getLog(getClass());

    private String DEFAULT_EDUCATION_LEVEL = "NotKnown";
    private final static char DEFAULT_LATERALITY = 'X';

    PersonDao personDAO;
    EducationLevelDao educationLevelDao;
    MailService mailService;

    @Required
    public void setPersonDAO(PersonDao personDAO) {
        this.personDAO = personDAO;
    }

    @Required
    public void setEducationLevelDao(EducationLevelDao educationLevelDao) {
        this.educationLevelDao = educationLevelDao;
    }

    @Required
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    @Transactional
    public Integer create(Person person) {

        person.setPassword(encodePassword(person.getPassword()));

        if (person.getLaterality() == '\u0000')
            person.setLaterality(DEFAULT_LATERALITY);

        log.debug("Setting authority = ROLE_USER");
        if (person.getAuthority() == null)
            person.setAuthority(Util.ROLE_USER);

        person = newPerson(person);

        mailService.sendRegistrationConfirmMail(person, EEGDataBaseSession.get().getLocale());

        return person.getPersonId();
    }

    @Override
    @Transactional
    public Person createPerson(SocialUser userFb, Integer educationLevelId) {
        // copying the data to Person entity

        Person person = personDAO.getPerson(userFb.getEmail());
        if (person != null) {
            return person;
        }

        person = new Person();
        person.setUsername(userFb.getEmail());
        person.setGivenname(userFb.getFirstName());
        person.setSurname(userFb.getLastName());
        person.setGender('M');

        person.setLaterality(DEFAULT_LATERALITY);
        person.setEducationLevel(educationLevelId == null ? null : educationLevelDao.read(educationLevelId));

        // Code specific for this object type
        person.setPassword(encodeRandomPassword());
        log.debug("Setting authority to ROLE_USER");
        person.setAuthority("ROLE_USER");
        log.debug("Setting confirmed");
        person.setConfirmed(true);

        return newPerson(person);
    }

    @Override
    @Transactional
    public Person createPerson(AddPersonCommand apc) throws ParseException {
        // copying the data to Person entity
        Person person = new Person();
        person.setGivenname(apc.getGivenname());
        person.setSurname(apc.getSurname());
        person.setDateOfBirth(new Timestamp(ControllerUtils.getDateFormat().parse(apc.getDateOfBirth()).getTime()));
        person.setGender(apc.getGender().charAt(0));
        person.setPhoneNumber(apc.getPhoneNumber());
        person.setNote(apc.getNote());
        person.setLaterality(apc.getLaterality().charAt(0));
        person.setEducationLevel(educationLevelDao.read(apc.getEducationLevel()));

        // Code specific for this object type
        person.setUsername(apc.getEmail());
        person.setPassword(encodeRandomPassword());
        log.debug("Setting authority to ROLE_READER");
        person.setAuthority(Util.ROLE_READER);

        return newPerson(person);
    }

    private Person newPerson(Person person) {
        if (person.getAuthenticationHash() == null) {
            log.debug("Hashing the username");
            person.setAuthenticationHash(ControllerUtils.getMD5String(person.getUsername()));
        }
        if (person.getRegistrationDate() == null) {
            log.debug("Setting registration date");
            person.setRegistrationDate(new Timestamp(System.currentTimeMillis()));
        }
        if (person.getEducationLevel() == null) {// TODO remove if
                                                 // educationLevel is nullable
                                                 // or always set by the caller
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

    @Override
    @Transactional(readOnly = true)
    public Person getPersonByHash(String hashCode) {

        return personDAO.getPersonByHash(hashCode);

    }

    private String encodePassword(String plaintextPassword) {

        if (plaintextPassword == null)
            return encodeRandomPassword();

        return new BCryptPasswordEncoder().encode(plaintextPassword);
    }

    private String encodeRandomPassword() {
        log.debug("Generating random password");
        return encodePassword(ControllerUtils.getRandomPassword());
    }

    private boolean matchPasswords(String plaintextPassword, String encodedPassword) {
        return new BCryptPasswordEncoder().matches(plaintextPassword, encodedPassword);
    }

    @Override
    @Transactional
    public void delete(Person person) {
        personDAO.delete(personDAO.read(person.getPersonId()));
    }

    @Override
    @Transactional
    public void update(Person person) {
        personDAO.update(person);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean usernameExists(String userName) {
        return personDAO.usernameExists(userName);
    }

    @Override
    @Transactional(readOnly = true)
    public Person getPerson(String username) {
        return personDAO.getPerson(username);
    }

    @Override
    @Transactional
    public void changeUserPassword(String userName, String password) {
        Person person = personDAO.getPerson(userName);

        person.setPassword(encodePassword(password));
        personDAO.update(person);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isPasswordEquals(String userName, String password) {
        return matchPasswords(password, personDAO.getPerson(userName).getPassword());
    }

    @Override
    @Transactional
    public void forgottenPassword(Person person) {
        Person user = personDAO.getPerson(person.getEmail());
        String plainPassword = ControllerUtils.getRandomPassword();

        if (mailService.sendForgottenPasswordMail(user.getEmail(), plainPassword)) {
            log.debug("Updating new password into database");
            user.setPassword(new BCryptPasswordEncoder().encode(plainPassword));
            personDAO.update(user);
            log.debug("Password updated");
        } else {
            log.debug("E-mail message was NOT sent");
            log.debug("Password was NOT changed");
        }

    }

    @Override
    @Transactional(readOnly = true)
    public Person read(Integer id) {
        return personDAO.read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> readByParameter(String parameterName, int parameterValue) {
        return personDAO.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> readByParameter(String parameterName, String parameterValue) {
        return personDAO.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> getAllRecords() {
        return personDAO.getAllRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> getRecordsAtSides(int first, int max) {
        return personDAO.getRecordsAtSides(first, max);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountRecords() {
        return personDAO.getCountRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> getUnique(Person example) {
        return personDAO.findByExample(example);
    }

    @Override
    @Transactional(readOnly = true)
    public Person getPersonByFbUid(String fbUid) {
        return personDAO.getPersonByFbUid(fbUid);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> getPersonsWherePendingRequirement() {
        return personDAO.getPersonsWherePendingRequirement();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean fbUidExists(String id) {
        return personDAO.fbUidExists(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> getSupervisors() {
        return personDAO.getSupervisors();
    }

    @Override
    @Transactional(readOnly = true)
    public Person getLoggedPerson() {
        return personDAO.getLoggedPerson();
    }

    @Override
    @Transactional(readOnly = true)
    public Map getInfoForAccountOverview(Person loggedPerson) {
        return personDAO.getInfoForAccountOverview(loggedPerson);
    }

    @Override
    public List<Person> getRecordsNewerThan(long oracleScn) {
        return personDAO.getRecordsNewerThan(oracleScn);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userNameInGroup(String userName, int groupId) {
        return personDAO.userNameInGroup(userName, groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> getPersonSearchResults(List<SearchRequest> requests) {
        return personDAO.getPersonSearchResults(requests);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountForList() {
        return personDAO.getCountForList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> getDataForList(int start, int limit) {
        return personDAO.getDataForList(start, limit);
    }

    @Override
    @Transactional(readOnly = true)
    public Person getPersonForDetail(int id) {
        return personDAO.getPersonForDetail(id);
    }

}
