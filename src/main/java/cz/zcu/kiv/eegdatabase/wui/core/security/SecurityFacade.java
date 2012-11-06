package cz.zcu.kiv.eegdatabase.wui.core.security;

import cz.zcu.kiv.eegdatabase.wui.core.dto.FullUserDTO;

public interface SecurityFacade {

    boolean authorization(String userName, String password);
    
    void createUser(FullUserDTO user);
}
