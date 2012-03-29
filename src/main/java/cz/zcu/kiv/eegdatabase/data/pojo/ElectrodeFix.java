package cz.zcu.kiv.eegdatabase.data.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 1.3.12
 * Time: 14:20
 * To change this template use File | Settings | File Templates.
 */
@Entity
@javax.persistence.Table(name="ELECTRODE_FIX")
public class ElectrodeFix implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ELECTRODE_FIX_ID")
    private int electrodeFixId;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "IS_DEFAULT")
    private int defaultNumber;
    @OneToMany(mappedBy = "electrodeFix")
    private Set<ElectrodeLocation> electrodeLocations = new HashSet<ElectrodeLocation>(0);

    public ElectrodeFix() {
    }

    public int getElectrodeFixId() {
        return electrodeFixId;
    }

    public void setElectrodeFixId(int electrodeFixId) {
        this.electrodeFixId = electrodeFixId;
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
