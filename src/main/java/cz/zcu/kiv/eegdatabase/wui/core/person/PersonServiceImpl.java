package cz.zcu.kiv.eegdatabase.wui.core.person;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import cz.zcu.kiv.eegdatabase.data.dao.EducationLevelDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.service.MailService;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.logic.controller.social.SocialUser;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;

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
    public void createPerson(Person person) {

        person.setPassword(encodePassword(person.getPassword()));
        person.setLaterality(DEFAULT_LATERALITY);

        log.debug("Setting authority = ROLE_USER");
        person.setAuthority(Util.ROLE_USER);

        
        person = newPerson(person);

        mailService.sendRegistrationConfirmMail(person, EEGDataBaseSession.get().getLocale());
    }
    
    @Override
    @Transactional
    public Person createPerson(SocialUser userFb, Integer educationLevelId){
        //copying the data to Person entity

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

        //Code specific for this object type
        person.setPassword(encodeRandomPassword());
        log.debug("Setting authority to ROLE_USER");
        person.setAuthority("ROLE_USER");
        log.debug("Setting confirmed");
        person.setConfirmed(true);
        
        return newPerson(person);
    }

    private Person newPerson(Person person) {
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

    @Override
    @Transactional(readOnly = true)
    public Person getPersonByHash(String hashCode) {

        return personDAO.getPersonByHash(hashCode);

    }

    private String encodePassword(String plaintextPassword) {
        return new BCryptPasswordEncoder().encode(plaintextPassword);
    }
    
    private String encodeRandomPassword(){
        log.debug("Generating random password");
        return encodePassword(ControllerUtils.getRandomPassword());
    }

    private boolean matchPasswords(String plaintextPassword, String encodedPassword) {
        return new BCryptPasswordEncoder().matches(plaintextPassword, encodedPassword);
    }

    @Override
    @Transactional
    public void deletePerson(Person user) {
        personDAO.delete(personDAO.getPerson(user.getEmail()));
    }

    @Override
    @Transactional
    public void updatePerson(Person user) {
        personDAO.update(user);
    }

    @Override
    public boolean usernameExists(String userName) {
        return personDAO.usernameExists(userName);
    }

    @Override
    @Transactional(readOnly = true)
    public Person getPersonByUserName(String userName) {
        return personDAO.getPerson(userName);
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

}
