package cz.zcu.kiv.eegdatabase.wui.core.security;


public interface SecurityFacade {

    boolean authorization(String userName, String password);
    
    void logout();
    
}
