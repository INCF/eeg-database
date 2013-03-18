package cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collections;
import java.util.List;

/**
 * Data container for marshaling list of hardware containers.
 * Allows XML marshaling using JAXB.
 *
 * @author Petr Miko
 *         Date: 9.2.13
 */
@XmlRootElement(name = "hardwareList")
public class HardwareDataList {

    @XmlElement(name = "hardware")
    public List<HardwareData> hardwares;

    public HardwareDataList() {
        this(Collections.<HardwareData>emptyList());
    }

    public HardwareDataList(List<HardwareData> hardwares) {
        this.hardwares = hardwares;
    }
}
