package cz.zcu.kiv.eegdatabase.data.pojo;


import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 28.2.12
 * Time: 13:47
 * To change this template use File | Settings | File Templates.
 */
@Entity
@javax.persistence.Table(name="ELECTRODE_CONF")
public class ElectrodeConf implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ELECTRODE_CONF_ID")
    private int electrodeConfId;
    @Column(name = "IMPEDANCE")
    private int impedance;
    @ManyToOne
    @JoinColumn(name = "ELECTRODE_SYSTEM_ID")
    private ElectrodeSystem electrodeSystem;
    @ManyToOne
    @JoinColumn(name = "DESC_IMG_ID")
    private DataFile descImg;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<ElectrodeLocation> electrodeLocations = new HashSet<ElectrodeLocation>(0);
    @OneToMany(mappedBy = "electrodeConf")
    private Set<Experiment> experiments = new HashSet<Experiment>(0);

    public ElectrodeConf() {
    }

    public int getElectrodeConfId() {
        return electrodeConfId;
    }

    public void setElectrodeConfId(int electrodeConfId) {
        this.electrodeConfId = electrodeConfId;
    }

    public int getImpedance() {
        return impedance;
    }

    public void setImpedance(int impedance) {
        this.impedance = impedance;
    }

    public DataFile getDescImg() {
        return descImg;
    }

    public void setDescImg(DataFile descImg) {
        this.descImg = descImg;
    }

    public Set<Experiment> getExperiments() {
        return experiments;
    }

    public void setExperiments(Set<Experiment> experiments) {
        this.experiments = experiments;
    }

    public ElectrodeSystem getElectrodeSystem() {
        return electrodeSystem;
    }

    public void setElectrodeSystem(ElectrodeSystem electrodeSystem) {
        this.electrodeSystem = electrodeSystem;
    }

    public Set<ElectrodeLocation> getElectrodeLocations() {
        return electrodeLocations;
    }

    public void setElectrodeLocations(Set<ElectrodeLocation> electrodeLocations) {
        this.electrodeLocations = electrodeLocations;
    }
}
