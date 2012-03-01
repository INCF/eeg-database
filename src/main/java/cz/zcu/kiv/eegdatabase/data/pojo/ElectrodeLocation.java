package cz.zcu.kiv.eegdatabase.data.pojo;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 1.3.12
 * Time: 14:37
 * To change this template use File | Settings | File Templates.
 */
public class ElectrodeLocation implements Serializable {

    private int electrodeLocationId;
    private String title;
    private String description;
    private String shortcut;
    private int defaultNumber;
    private ElectrodeType electrodeType;
    private ElectrodeFix electrodeFix;
    private Set<ElectrodeConf> electrodeConfs = new HashSet<ElectrodeConf>(0);

    public ElectrodeLocation() {
    }

    public ElectrodeLocation(int electrodeLocationId, String title, String description, String shortcut, int defaultNumber,
                             ElectrodeType electrodeType, ElectrodeFix electrodeFix, Set<ElectrodeConf> electrodeConfs) {
        this.electrodeLocationId = electrodeLocationId;
        this.title = title;
        this.description = description;
        this.shortcut = shortcut;
        this.defaultNumber = defaultNumber;
        this.electrodeType = electrodeType;
        this.electrodeFix = electrodeFix;
        this.electrodeConfs = electrodeConfs;
    }

    public int getElectrodeLocationId() {
        return electrodeLocationId;
    }

    public void setElectrodeLocationId(int electrodeLocationId) {
        this.electrodeLocationId = electrodeLocationId;
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

    public String getShortcut() {
        return shortcut;
    }

    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }

    public int getDefaultNumber() {
        return defaultNumber;
    }

    public void setDefaultNumber(int defaultNumber) {
        this.defaultNumber = defaultNumber;
    }

    public ElectrodeType getElectrodeType() {
        return electrodeType;
    }

    public void setElectrodeType(ElectrodeType electrodeType) {
        this.electrodeType = electrodeType;
    }

    public ElectrodeFix getElectrodeFix() {
        return electrodeFix;
    }

    public void setElectrodeFix(ElectrodeFix electrodeFix) {
        this.electrodeFix = electrodeFix;
    }

    public Set<ElectrodeConf> getElectrodeConfs() {
        return electrodeConfs;
    }

    public void setElectrodeConfs(Set<ElectrodeConf> electrodeConfs) {
        this.electrodeConfs = electrodeConfs;
    }
}
