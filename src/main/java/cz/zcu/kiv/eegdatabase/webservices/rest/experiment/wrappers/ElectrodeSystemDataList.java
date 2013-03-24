package cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collections;
import java.util.List;

/**
 * Data container for collection of electrode system data containers.
 * Able of XML marshaling.
 *
 * @author Petr Miko
 */
@XmlRootElement(name = "electrodeSystems")
public class ElectrodeSystemDataList {

    @XmlElement(name = "electrodeSystem")
    public List<ElectrodeSystemData> electrodeSystems;

    public ElectrodeSystemDataList(){
        this(Collections.<ElectrodeSystemData>emptyList());
    }

    public ElectrodeSystemDataList(List<ElectrodeSystemData> electrodeSystems) {
        this.electrodeSystems = electrodeSystems;
    }
}
