package cz.zcu.kiv.eegdatabase.wui.core.security;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import cz.zcu.kiv.eegdatabase.data.dao.EducationLevelDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.service.MailService;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.core.dto.FullPersonDTO;
import cz.zcu.kiv.eegdatabase.wui.core.educationlevel.EducationLevelDTO;
import cz.zcu.kiv.eegdatabase.wui.ui.security.Gender;

public class SecurityServiceImpl implements SecurityService {

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
    public void createPerson(FullPersonDTO user) {

        Person person = convertToEntity(user, new Person());
        person.setPassword(encodePassword(user.getPassword()));
        person.setLaterality(DEFAULT_LATERALITY);

        log.debug("Setting authority = ROLE_USER");
        person.setAuthority(Util.ROLE_USER);

        if (person.getAuthenticationHash() == null) {
            log.debug("Hashing the username");
            person.setAuthenticationHash(ControllerUtils.getMD5String(person.getUsername()));
        }

        if (person.getRegistrationDate() == null) {
            log.debug("Setting registration date");
            person.setRegistrationDate(new Timestamp(System.currentTimeMillis()));
        }

        if (person.getEducationLevel() == null) {
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
        
        mailService.sendRegistrationConfirmMail(person, EEGDataBaseSession.get().getLocale());
    }

    @Override
    @Transactional(readOnly = true)
    public FullPersonDTO getPersonByHash(String hashCode) {

        return convertToDTO(personDAO.getPersonByHash(hashCode));

    }

    private String encodePassword(String plaintextPassword) {
        return new BCryptPasswordEncoder().encode(plaintextPassword);
    }

    private FullPersonDTO convertToDTO(Person person) {

        FullPersonDTO dto = new FullPersonDTO();
        dto.setId(person.getPersonId());
        dto.setName(person.getGivenname());
        dto.setSurname(person.getSurname());
        dto.setDateOfBirth(new Date(person.getDateOfBirth().getTime()));
        dto.setEmail(person.getUsername());
        dto.setGender(Gender.getGenderByShortcut(person.getGender()));
        dto.setConfirmed(person.isConfirmed());
        dto.setRegistrationDate(new DateTime(person.getRegistrationDate().getTime()));

        EducationLevel educationLevel = educationLevelDao.read(person.getEducationLevel().getEducationLevelId());
        EducationLevelDTO edu = new EducationLevelDTO();
        edu.setId(educationLevel.getEducationLevelId());
        edu.setTitle(educationLevel.getTitle());
        dto.setEducationLevel(edu);
        dto.setLaterality(person.getLaterality());

        return dto;
    }

    private Person convertToEntity(FullPersonDTO dto, Person person) {

        person.setPersonId(dto.getId());
        person.setGivenname(dto.getName());
        person.setSurname(dto.getSurname());
        person.setDateOfBirth(new Timestamp(dto.getDateOfBirth().getTime()));
        person.setUsername(dto.getEmail());
        person.setGender(dto.getGender().getShortcut());
        person.setConfirmed(dto.isConfirmed());
        person.setRegistrationDate(new Timestamp(dto.getRegistrationDate().getMillis()));
        person.setEducationLevel(educationLevelDao.read(dto.getEducationLevel().getId()));
        person.setLaterality(dto.getLaterality());

        return person;
    }

    @Override
    @Transactional
    public void deletePerson(FullPersonDTO user) {
        personDAO.delete(personDAO.getPerson(user.getEmail()));
    }

    @Override
    @Transactional
    public void updatePerson(FullPersonDTO user) {
        personDAO.update(convertToEntity(user, personDAO.getPerson(user.getEmail())));
    }

}
