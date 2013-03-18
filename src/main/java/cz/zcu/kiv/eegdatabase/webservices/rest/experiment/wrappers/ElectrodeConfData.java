package cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Data container for XML marshaling of electrode configuration data.
 *
 * @author Petr Miko
 */
@XmlType(propOrder = {"id", "impedance", "electrodeSystem", "electrodeLocations"})
@XmlRootElement(name = "electrodeLocation")
public class ElectrodeConfData {

    private int id;
    private int impedance;
    private ElectrodeSystemData electrodeSystem;
    private ElectrodeLocationDataList electrodeLocations;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImpedance() {
        return impedance;
    }

    public void setImpedance(int impedance) {
        this.impedance = impedance;
    }

    public ElectrodeSystemData getElectrodeSystem() {
        return electrodeSystem;
    }

    public void setElectrodeSystem(ElectrodeSystemData electrodeSystem) {
        this.electrodeSystem = electrodeSystem;
    }

    public ElectrodeLocationDataList getElectrodeLocations() {
        return electrodeLocations;
    }

    public void setElectrodeLocations(ElectrodeLocationDataList electrodeLocations) {
        this.electrodeLocations = electrodeLocations;
    }
}
