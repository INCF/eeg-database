package cz.zcu.kiv.eegdatabase.wui.core.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import cz.zcu.kiv.eegdatabase.wui.core.educationlevel.EducationLevelDTO;
import cz.zcu.kiv.eegdatabase.wui.ui.security.Gender;

public class FullUserDTO implements Serializable {

    private static final long serialVersionUID = -5467478793603707659L;
    
    private String name;
    private String surname;
    private String dateOfBirth;
    private Gender gender;
    private String email;
    private String password;
    private String passwordVerify;
    private EducationLevelDTO educationLevel;
    private String controlText;
    private String captcha;
    
    public FullUserDTO() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordVerify() {
        return passwordVerify;
    }

    public void setPasswordVerify(String passwordVerify) {
        this.passwordVerify = passwordVerify;
    }

    public EducationLevelDTO getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(EducationLevelDTO educationLevel) {
        this.educationLevel = educationLevel;
    }

    public String getControlText() {
        return controlText;
    }

    public void setControlText(String controlText) {
        this.controlText = controlText;
    }
    
    public String getCaptcha() {
        return captcha;
    }
    
    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
    
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
    
    public boolean isPasswordValid(){
        return password.equals(passwordVerify);
    }
    
}
