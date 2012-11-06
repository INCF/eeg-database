package cz.zcu.kiv.eegdatabase.wui.core.security;

import org.springframework.beans.factory.annotation.Required;

import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.core.dto.FullUserDTO;

public class SecurityFacadeImpl implements SecurityFacade {

    SecurityService service;

    @Required
    public void setService(SecurityService service) {
        this.service = service;
    }

    @Override
    public boolean authorization(String userName, String password) {
        return EEGDataBaseSession.get().signIn(userName, password);
    }

    @Override
    public void createUser(FullUserDTO user) {
        service.createUser(user);
    }

}
