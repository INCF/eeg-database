package cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data container for list of digitizations.
 * Required for XML marshaling.
 *
 * @author Petr Miko
 */
@XmlRootElement(name = "digitizations")
public class DigitizationDataList {

    @XmlElement(name = "digitization")
    public List<DigitizationData> digitizations;

    public DigitizationDataList() {
        this(new ArrayList<DigitizationData>());
    }

    public DigitizationDataList(List<DigitizationData> digitizations) {
        this.digitizations = digitizations;
    }
}
