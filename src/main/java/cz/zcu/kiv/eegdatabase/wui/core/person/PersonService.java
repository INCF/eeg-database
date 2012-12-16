package cz.zcu.kiv.eegdatabase.wui.core.person;

import cz.zcu.kiv.eegdatabase.wui.core.dto.FullPersonDTO;

public interface PersonService {

    void createPerson(FullPersonDTO user);

    void deletePerson(FullPersonDTO user);

    void updatePerson(FullPersonDTO user);

    FullPersonDTO getPersonByHash(String hashCode);

    FullPersonDTO getPersonByUserName(String userName);

    public boolean usernameExists(String userName);

    void changeUserPassword(String userName, String password);

    boolean isPasswordEquals(String userName, String password);
    
    void forgottenPassword(FullPersonDTO person);
}
