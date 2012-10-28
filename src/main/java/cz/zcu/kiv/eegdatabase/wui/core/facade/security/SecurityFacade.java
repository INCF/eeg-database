package cz.zcu.kiv.eegdatabase.wui.core.facade.security;

public interface SecurityFacade {
    
    boolean authorization(String userName, String password);
}
