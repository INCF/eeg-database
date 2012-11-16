package cz.zcu.kiv.eegdatabase.wui.core.security;

import org.springframework.beans.factory.annotation.Required;

import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.core.dto.FullPersonDTO;

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
    public void createPerson(FullPersonDTO user) {
        service.createPerson(user);
    }

    @Override
    public FullPersonDTO getPersonByHash(String hashCode) {
        return service.getPersonByHash(hashCode);
    }

    @Override
    public void deletePerson(FullPersonDTO user) {
        service.deletePerson(user);
    }
    
    @Override
    public void updatePerson(FullPersonDTO user) {
        service.updatePerson(user);
    }

}
