package cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collections;
import java.util.List;

/**
 * Data container for list of pharmaceuticals.
 * Used for XML marshaling, uses JAXB.
 *
 * @author Petr Miko
 *         Date: 18.3.13
 */
@XmlRootElement(name = "pharmaceuticals")
public class PharmaceuticalDataList {

    @XmlElement(name = "pharmaceutical")
    public List<PharmaceuticalData> pharmaceuticals;

    public PharmaceuticalDataList(){
        this(Collections.<PharmaceuticalData>emptyList());
    }

    public PharmaceuticalDataList(List<PharmaceuticalData> pharmaceuticals) {
        this.pharmaceuticals = pharmaceuticals;
    }
}
