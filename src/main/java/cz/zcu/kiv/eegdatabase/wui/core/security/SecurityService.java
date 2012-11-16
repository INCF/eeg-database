package cz.zcu.kiv.eegdatabase.wui.core.security;

import cz.zcu.kiv.eegdatabase.wui.core.dto.FullPersonDTO;

public interface SecurityService {

    void createPerson(FullPersonDTO user);
    void deletePerson(FullPersonDTO user);
    void updatePerson(FullPersonDTO user);
    
    FullPersonDTO getPersonByHash(String hashCode);
    

}
