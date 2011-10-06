package cz.zcu.kiv.eegdatabase.logic.controller.myaccount;

/**
 * @author Jindra
 */
public class ChangePasswordCommand {

    private String oldPassword;
    private String newPassword;
    private String newPassword2;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword2() {
        return newPassword2;
    }

    public void setNewPassword2(String newPassword2) {
        this.newPassword2 = newPassword2;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
