/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.commandobjects;

/**
 *
 * @author pbruha
 */
public class PeopleSearcherCommand {

  private int givennameOption;
  private String givenname;
  private int surnameOption;
  private String surname;
  private int emailOption;
  private String email;
  private int genderOption;
  private String gender;
  private int ageOption;
  private String fromDate;
  private String toDate;
  private int withoutDefectOption;

  public int getAgeOption() {
    return ageOption;
  }

  public void setAgeOption(int ageOption) {
    this.ageOption = ageOption;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public int getEmailOption() {
    return emailOption;
  }

  public void setEmailOption(int emailOption) {
    this.emailOption = emailOption;
  }

  public String getFromDate() {
    return fromDate;
  }

  public void setFromDate(String fromDate) {
    this.fromDate = fromDate;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public int getGenderOption() {
    return genderOption;
  }

  public void setGenderOption(int genderOption) {
    this.genderOption = genderOption;
  }

  public String getGivenname() {
    return givenname;
  }

  public void setGivenname(String givenname) {
    this.givenname = givenname;
  }

  public int getGivennameOption() {
    return givennameOption;
  }

  public void setGivennameOption(int givennameOption) {
    this.givennameOption = givennameOption;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public int getSurnameOption() {
    return surnameOption;
  }

  public void setSurnameOption(int surnameOption) {
    this.surnameOption = surnameOption;
  }

  public String getToDate() {
    return toDate;
  }

  public void setToDate(String toDate) {
    this.toDate = toDate;
  }

  public int getWithoutDefectOption() {
    return withoutDefectOption;
  }

  public void setWithoutDefectOption(int withoutDefectOption) {
    this.withoutDefectOption = withoutDefectOption;
  }
}
