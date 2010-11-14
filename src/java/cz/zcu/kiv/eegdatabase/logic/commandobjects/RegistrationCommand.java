package cz.zcu.kiv.eegdatabase.logic.commandobjects;

/**
 *
 * @author Jindra
 */
public class RegistrationCommand {

    private String givenname;
    private String surname;
    private String email;
    private String username;
    private String gender;
    private String dateOfBirth;
    private String password;
    private String password2;
    private String controlText;
    private String captchaId;

  public String getControlText() {
    return controlText;
  }

  public void setControlText(String controlText) {
    this.controlText = controlText;
  }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGivenname() {
        return givenname;
    }

    public void setGivenname(String givenname) {
        this.givenname = givenname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

  /**
   * @return the captchaId
   */
  public String getCaptchaId() {
    return captchaId;
  }

  /**
   * @param captchaId the captchaId to set
   */
  public void setCaptchaId(String captchaId) {
    this.captchaId = captchaId;
  }
}
