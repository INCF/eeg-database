package cz.zcu.kiv.eegdatabase.wui.core.security;

import java.io.Serializable;

public class LoginUserDTO implements Serializable {

    private static final long serialVersionUID = -1533766468013302891L;
    
    private String userName;
    private String password;
    
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    
    
}
