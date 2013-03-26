package cz.zcu.kiv.eegdatabase.data.pojo;

import cz.zcu.kiv.eegdatabase.data.annotation.SolrField;
import cz.zcu.kiv.eegdatabase.data.annotation.SolrId;
import cz.zcu.kiv.eegdatabase.data.indexing.IndexField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 8.2.12
 * Time: 13:30
 * To change this template use File | Settings | File Templates.
 */
@Entity
@javax.persistence.Table(name="SUBJECT_GROUP")
public class SubjectGroup implements Serializable {
    @SolrId
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "SUBJECT_GROUP_ID")
    private int subjectGroupId;
    @SolrField(name = IndexField.TITLE)
    @Column(name = "TITLE")
    private String title;
    @SolrField(name = IndexField.TEXT)
    @Column(name = "DESCRIPTION")
    private String description;
    @OneToMany(mappedBy = "subjectGroup")
    private Set<Experiment> experiments = new HashSet<Experiment>(0);

    public SubjectGroup() {
    }

    public SubjectGroup(int subjectGroupId, String description, String name) {
        this.subjectGroupId = subjectGroupId;
        this.description = description;
        this.title = name;
    }

    public int getSubjectGroupId() {
        return subjectGroupId;
    }

    public void setSubjectGroupId(int subjectGroupId) {
        this.subjectGroupId = subjectGroupId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Experiment> getExperiments() {
        return experiments;
    }

    public void setExperiments(Set<Experiment> experiments) {
        this.experiments = experiments;
    }
}
