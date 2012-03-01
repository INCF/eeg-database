package cz.zcu.kiv.eegdatabase.data.pojo;


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
public class ElectrodeConf implements Serializable {

    private int electrodeConfId;
    private int impedance;
    private ElectrodeSystem electrodeSystem;
    private Set<ElectrodeLocation> electrodeLocations = new HashSet<ElectrodeLocation>(0);
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
