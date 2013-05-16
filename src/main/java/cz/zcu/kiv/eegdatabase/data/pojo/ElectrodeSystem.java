package cz.zcu.kiv.eegdatabase.data.pojo;

import cz.zcu.kiv.eegdatabase.data.annotation.SolrField;
import cz.zcu.kiv.eegdatabase.data.annotation.SolrId;
import cz.zcu.kiv.eegdatabase.logic.indexing.IndexField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 1.3.12
 * Time: 14:32
 * To change this template use File | Settings | File Templates.
 */
@Entity
@javax.persistence.Table(name="ELECTRODE_SYSTEM")
public class ElectrodeSystem implements Serializable {
    @SolrId
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ELECTRODE_SYSTEM_ID")
    private int electrodeSystemId;
    @Column(name = "TITLE")
    @SolrField(name = IndexField.TITLE)
    private String title;
    @Column(name = "DESCRIPTION")
    @SolrField(name = IndexField.TEXT)
    private String description;
    @Column(name = "IS_DEFAULT")
    private int defaultNumber;
    @OneToMany(mappedBy = "electrodeSystem")
    private Set<ElectrodeConf> electrodeConfs = new HashSet<ElectrodeConf>(0);
    private Set<ResearchGroup> researchGroups = new HashSet<ResearchGroup>(0);

    public ElectrodeSystem() {
    }

    public ElectrodeSystem(int electrodeSystemId, String title, String description, int defaultNumber, Set<ElectrodeConf> electrodeConfs) {
        this.electrodeSystemId = electrodeSystemId;
        this.title = title;
        this.description = description;
        this.defaultNumber = defaultNumber;
        this.electrodeConfs = electrodeConfs;
    }

    public int getElectrodeSystemId() {
        return electrodeSystemId;
    }

    public void setElectrodeSystemId(int electrodeSystemId) {
        this.electrodeSystemId = electrodeSystemId;
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

    public int getDefaultNumber() {
        return defaultNumber;
    }

    public void setDefaultNumber(int defaultNumber) {
        this.defaultNumber = defaultNumber;
    }

    public Set<ElectrodeConf> getElectrodeConfs() {
        return electrodeConfs;
    }

    public void setElectrodeConfs(Set<ElectrodeConf> electrodeConfs) {
        this.electrodeConfs = electrodeConfs;
    }

    public Set<ResearchGroup> getResearchGroups() {
        return researchGroups;
    }

    public void setResearchGroups(Set<ResearchGroup> researchGroups) {
        this.researchGroups = researchGroups;
    }
}
