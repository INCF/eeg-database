package cz.zcu.kiv.eegdatabase.data.pojo;

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
public class ElectrodeSystem implements Serializable {

    private int electrodeSystemId;
    private String title;
    private String description;
    private int defaultNumber;
    private Set<ElectrodeConf> electrodeConfs = new HashSet<ElectrodeConf>(0);

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
}
