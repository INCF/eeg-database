package cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collections;
import java.util.List;

/**
 * Data container for collection of electrode fix data containers.
 * Able of XML marshaling.
 *
 * @author Petr Miko
 */
@XmlRootElement(name = "electrodeFixList")
public class ElectrodeFixDataList {

    @XmlElement(name = "electrodeFix")
    public List<ElectrodeFixData> electrodeFixList;

    public ElectrodeFixDataList(){
        this(Collections.<ElectrodeFixData>emptyList());
    }

    public ElectrodeFixDataList(List<ElectrodeFixData> electrodeFixList) {
        this.electrodeFixList = electrodeFixList;
    }
}
