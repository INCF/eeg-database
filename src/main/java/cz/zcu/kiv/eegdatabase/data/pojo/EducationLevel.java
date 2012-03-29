package cz.zcu.kiv.eegdatabase.data.pojo;

import javax.persistence.*;
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
@Entity
@javax.persistence.Table(name="EDUCATION_LEVEL")
public class EducationLevel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "EDUCATION_LEVEL_ID")
    private int educationLevelId;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "IS_DEFAULT")
    private int defaultNumber;
    @OneToMany(mappedBy = "educationLevel")
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
