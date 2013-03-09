package cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Petr Miko
 *         Date: 9.3.13
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "subject", propOrder = {"personId", "name", "surname", "gender", "age", "leftHanded" })
public class SubjectData {

    private int personId;
    private String name, surname;
    private String gender;
    private int age;
    private boolean leftHanded;

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
}
