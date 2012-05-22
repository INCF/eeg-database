package cz.zcu.kiv.eegdatabase.webservices.client.wrappers;

import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;

/**
 * @author František Liška
 */
public class PersonInfo {
    private int personId;
    private String givenname;
    private String surname;
    private long dateOfBirthInMillis;
    private char gender;
    private String phoneNumber;
    private long registrationDateInMillis;
    private String note;
    private String username;
    private char laterality;
    private String educationLevelTitle;

    public PersonInfo() {
    }

    public PersonInfo(int personId, String surname, char gender) {
        this.personId = personId;
        this.surname = surname;
        this.gender = gender;
    }

    public PersonInfo(int personId, String givenname, String surname, long dateOfBirthInMillis, char gender, String phoneNumber, String note, String username) {
        this.personId = personId;
        this.givenname = givenname;
        this.surname = surname;
        this.dateOfBirthInMillis = dateOfBirthInMillis;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.note = note;
        this.username = username;
    }


    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getGivenname() {
        return givenname;
    }

    public void setGivenname(String givenname) {
        this.givenname = givenname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getDateOfBirthInMillis() {
        return dateOfBirthInMillis;
    }

    public void setDateOfBirthInMillis(long dateOfBirthInMillis) {
        this.dateOfBirthInMillis = dateOfBirthInMillis;
    }

    public long getRegistrationDateInMillis() {
        return registrationDateInMillis;
    }

    public void setRegistrationDateInMillis(long registrationDateInMillis) {
        this.registrationDateInMillis = registrationDateInMillis;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public char getLaterality() {
        return laterality;
    }

    public void setLaterality(char laterality) {
        this.laterality = laterality;
    }

    public String getEducationLevelTitle() {
        return educationLevelTitle;
    }

    public void setEducationLevelTitle(String  educationLevelTitle) {
        this.educationLevelTitle = educationLevelTitle;
    }
}

