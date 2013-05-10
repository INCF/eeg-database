package cz.zcu.kiv.eegdatabase.data.pojo;

import cz.zcu.kiv.eegdatabase.data.annotation.SolrField;
import cz.zcu.kiv.eegdatabase.data.annotation.SolrId;
import cz.zcu.kiv.eegdatabase.data.indexing.IndexField;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacade;
import cz.zcu.kiv.eegdatabase.wui.core.common.ProjectTypeFacade;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.IAutocompletable;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 1.3.12
 * Time: 11:41
 * To change this template use File | Settings | File Templates.
 */
@Entity
@javax.persistence.Table(name="PROJECT_TYPE")
public class ProjectType implements Serializable, IAutocompletable {
    @SolrId
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PROJECT_TYPE_ID")
    private int projectTypeId;
    @SolrField(name = IndexField.TITLE)
    @Column(name = "TITLE")
    private String title;
    @SolrField(name = IndexField.DESCRIPTION)
    @Column(name = "DESCRIPTION")
    private String description;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Experiment> experiments = new HashSet<Experiment>(0);
    private Set<ResearchGroup> researchGroups = new HashSet<ResearchGroup>(0);
    @Autowired
    @Transient
    private ProjectTypeFacade projectTypeFacade;

    public ProjectType() {

    }

    public ProjectType(int projectTypeId, String title, String description, Set<Experiment> experiments) {
        this.projectTypeId = projectTypeId;
        this.title = title;
        this.description = description;
        this.experiments = experiments;
    }

    public int getProjectTypeId() {
        return projectTypeId;
    }

    public void setProjectTypeId(int projectTypeId) {
        this.projectTypeId = projectTypeId;
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

    public Set<ResearchGroup> getResearchGroups() {
        return researchGroups;
    }

    public void setResearchGroups(Set<ResearchGroup> researchGroups) {
        this.researchGroups = researchGroups;
    }

    @Override
    public String getAutocompleteData() {
        return getTitle();
    }

    @Override
    public GenericFacade getFacade() {
        return projectTypeFacade;
    }

    @Override
    public boolean isValidOnChange() {
        return !(getTitle()==null || getTitle().equals(""));
    }
}
