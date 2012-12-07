package cz.zcu.kiv.eegdatabase.wui.ui.account.obj;

import java.io.Serializable;

public class ChangePasswordObj implements Serializable {

    private static final long serialVersionUID = 1L;

    private String oldPassword;
    private String newPassword;
    private String verPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getVerPassword() {
        return verPassword;
    }

    public void setVerPassword(String verPassword) {
        this.verPassword = verPassword;
    }

    public boolean isNewPasswordEqualsWithVerify() {
        return newPassword.equals(verPassword);
    }
}
