package cz.zcu.kiv.eegdatabase.wui.core.person;

import cz.zcu.kiv.eegdatabase.wui.core.dto.FullPersonDTO;

public interface PersonFacade {

    void createPerson(FullPersonDTO user);

    void deletePerson(FullPersonDTO user);

    void updatePerson(FullPersonDTO user);

    FullPersonDTO getPersonByHash(String hashCode);

    FullPersonDTO getPersonByUserName(String userName);

    public boolean usernameExists(String userName);

}
