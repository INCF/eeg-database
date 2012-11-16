package cz.zcu.kiv.eegdatabase.wui.core.security;

import cz.zcu.kiv.eegdatabase.wui.core.dto.FullPersonDTO;

public interface SecurityFacade {

    boolean authorization(String userName, String password);
    
    void createPerson(FullPersonDTO user);
    void deletePerson(FullPersonDTO user);
    void updatePerson(FullPersonDTO user);
    
    FullPersonDTO getPersonByHash(String hashCode);
    
}
