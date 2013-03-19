package cz.zcu.kiv.eegdatabase.webservices.rest.user.wrappers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Data container for XML marshaling of Person information.
 * Used for creating new person.
 *
 * @author Petr Miko
 */
@XmlType
@XmlRootElement(name = "person")
public class PersonData {

    private String name;
    private String surname;
    private String birthday;
    private String gender;
    private String email;
    private String leftHanded;
    private String notes;
    private String phone;

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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @XmlElement(required = false)
    public String getLeftHanded() {
        return leftHanded;
    }

    public void setLeftHanded(String leftHanded) {
        this.leftHanded = leftHanded;
    }

    @XmlElement(required = false)
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @XmlElement(required = false)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
