package cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collections;
import java.util.List;

/**
 * Data container for collection of electrode location data containers.
 * Able of XML marshaling.
 *
 * @author Petr Miko
 */
@XmlRootElement(name = "electrodeLocations")
public class ElectrodeLocationDataList {

    @XmlElement(name = "electrodeLocation")
    public List<ElectrodeLocationData> electrodeLocations;

    public ElectrodeLocationDataList(){
        this(Collections.<ElectrodeLocationData>emptyList());
    }

    public ElectrodeLocationDataList(List<ElectrodeLocationData> electrodeLocations) {
        this.electrodeLocations = electrodeLocations;
    }
}
