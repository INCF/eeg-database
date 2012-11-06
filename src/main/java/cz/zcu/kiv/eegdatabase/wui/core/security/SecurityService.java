package cz.zcu.kiv.eegdatabase.wui.core.security;

import cz.zcu.kiv.eegdatabase.wui.core.dto.FullUserDTO;

public interface SecurityService {

    void createUser(FullUserDTO user);

}
