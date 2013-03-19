package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class ObjectGenerator extends AbstractDataAccessTest {

  @Autowired
  protected EducationLevelDao educationLevelDao;

  public ObjectGenerator() {
  }

  public Person getPerson(){
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String password = ControllerUtils.getRandomPassword();
    Person person = new Person();
    person.setUsername("junit@test.reader");
    person.setAuthority(Util.ROLE_READER);
    person.setPassword(encoder.encode(password));
    person.setSurname("junit-test-surname");
    person.setGivenname("junit-test-name");
    person.setGender('M');
    person.setLaterality('X');

    //EducationLevel educationLevel = getEducationLevel();
    //educationLevelDao.create(educationLevel);
    //person.setEducationLevel(educationLevel);

    return person;
  }

  public EducationLevel getEducationLevel(){
    EducationLevel educationLevel = new EducationLevel();
    educationLevel.setDefaultNumber(0);
    educationLevel.setTitle("junit-education-level");
    educationLevel.setEducationLevelId(0);
    
    return educationLevel;
  }
  
  public Hardware getHardware(){
    Hardware hardware = new Hardware();
    hardware.setTitle("Title test");
    hardware.setDescription("Description test");
    hardware.setType("Type test");
    hardware.setDefaultNumber(0);

    return hardware;
  }
}
