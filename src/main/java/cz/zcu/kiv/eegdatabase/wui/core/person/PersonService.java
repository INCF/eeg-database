package cz.zcu.kiv.eegdatabase.wui.core.person;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.controller.social.SocialUser;
import cz.zcu.kiv.eegdatabase.wui.core.dto.FullPersonDTO;

public interface PersonService {

    void createPerson(FullPersonDTO user);
    
    Person createPerson(SocialUser userFb, Integer educationLevelId);

    void deletePerson(FullPersonDTO user);

    void updatePerson(FullPersonDTO user);

    FullPersonDTO getPersonByHash(String hashCode);

    FullPersonDTO getPersonByUserName(String userName);

    public boolean usernameExists(String userName);

    void changeUserPassword(String userName, String password);

    boolean isPasswordEquals(String userName, String password);
    
    void forgottenPassword(FullPersonDTO person);
}
