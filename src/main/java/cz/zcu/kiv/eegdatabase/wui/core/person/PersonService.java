package cz.zcu.kiv.eegdatabase.wui.core.person;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.controller.social.SocialUser;

public interface PersonService {

    void createPerson(Person user);
    
    Person createPerson(SocialUser userFb, Integer educationLevelId);

    void deletePerson(Person user);

    void updatePerson(Person user);

    Person getPersonByHash(String hashCode);

    Person getPersonByUserName(String userName);

    boolean usernameExists(String userName);

    void changeUserPassword(String userName, String password);

    boolean isPasswordEquals(String userName, String password);
    
    void forgottenPassword(Person person);
}
