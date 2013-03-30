package cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers;

import javax.xml.bind.annotation.*;

/**
 * @author Petr Miko
 *         Date: 9.3.13
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "subject", propOrder = {"personId", "name", "surname", "gender", "age", "leftHanded", "mailUsername", "mailDomain"})
public class SubjectData {

    private int personId;
    private String name, surname;
    @XmlElement(required = false)
    private String gender;
    @XmlElement(required = false)
    private int age;
    @XmlElement(required = false)
    private boolean leftHanded;
    @XmlElement(required = false)
    private String mailUsername;
    @XmlElement(required = false)
    private String mailDomain;

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
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

    public String getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = String.valueOf(gender);
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isLeftHanded() {
        return leftHanded;
    }

    public void setLeftHanded(boolean leftHanded) {
        this.leftHanded = leftHanded;
    }

    @XmlTransient
    public void setMail(String mail) {
        if (mail != null && mail.contains("@")) {
            String[] mailParts = mail.split("@");
            if (mailParts.length == 2) {
                mailUsername = mailParts[0];
                mailDomain = mailParts[1];
            }
        }
    }

    public String getMailUsername() {
        return mailUsername;
    }

    public void setMailUsername(String mailUsername) {
        this.mailUsername = mailUsername;
    }

    public String getMailDomain() {
        return mailDomain;
    }

    public void setMailDomain(String mailDomain) {
        this.mailDomain = mailDomain;
    }
}
