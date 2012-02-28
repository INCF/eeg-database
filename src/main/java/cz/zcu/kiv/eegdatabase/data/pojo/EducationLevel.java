package cz.zcu.kiv.eegdatabase.data.pojo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 8.2.12
 * Time: 13:14
 * To change this template use File | Settings | File Templates.
 */
public class EducationLevel implements Serializable {

    private int educationLevelId;
    private String title;
    private int defaultNumber;
    private Set<Person> persons = new HashSet<Person>(0);

    public EducationLevel() {
    }

    public EducationLevel(int educationLevelId, String title) {
        this.educationLevelId = educationLevelId;
        this.title = title;
    }

    public int getEducationLevelId() {
        return educationLevelId;
    }

    public void setEducationLevelId(int educationLevelId) {
        this.educationLevelId = educationLevelId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Person> getPersons() {
        return persons;
    }

    public void setPersons(Set<Person> persons) {
        this.persons = persons;
    }

    public int getDefaultNumber() {
        return defaultNumber;
    }

    public void setDefaultNumber(int defaultNumber) {
        this.defaultNumber = defaultNumber;
    }
}
