package cz.zcu.kiv.eegdatabase.data.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 1.3.12
 * Time: 14:04
 * To change this template use File | Settings | File Templates.
 */
@Entity
@javax.persistence.Table(name="ELECTRODE_TYPE_ID")
public class ElectrodeType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ELECTRODE_TYPE_ID")
    private int electrodeTypeId;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "IS_DEFAULT")
    private int defaultNumber;
    @OneToMany(mappedBy = "electrodeType")
    private Set<ElectrodeLocation> electrodeLocations = new HashSet<ElectrodeLocation>(0);

    public ElectrodeType() {
    }

    public int getElectrodeTypeId() {
        return electrodeTypeId;
    }

    public void setElectrodeTypeId(int electrodeTypeId) {
        this.electrodeTypeId = electrodeTypeId;
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

    public Set<ElectrodeLocation> getElectrodeLocations() {
        return electrodeLocations;
    }

    public void setElectrodeLocations(Set<ElectrodeLocation> electrodeLocations) {
        this.electrodeLocations = electrodeLocations;
    }
}
