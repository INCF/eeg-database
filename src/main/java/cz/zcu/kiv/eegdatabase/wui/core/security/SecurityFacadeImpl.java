package cz.zcu.kiv.eegdatabase.wui.core.security;

import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;

public class SecurityFacadeImpl implements SecurityFacade {
    

    @Override
    public boolean authorization(String userName, String password) {
        return EEGDataBaseSession.get().signIn(userName, password);
    }

    @Override
    public void logout() {
        EEGDataBaseSession.get().invalidate();
    }

}
