package cz.zcu.kiv.eegdatabase.logic.controller.admin;

/**
 * @author Jindra
 */
public class ChangeUserRoleCommand {

    private String userName;
    private String userRole;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
