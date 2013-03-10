package cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Petr Miko
 *         Date: 9.2.13
 */
@XmlRootElement(name = "diseases")
public class DiseaseDataList {

    @XmlElement(name = "disease")
    public List<DiseaseData> diseases;

    public DiseaseDataList() {
        this(Collections.<DiseaseData>emptyList());
    }

    public DiseaseDataList(List<DiseaseData> diseases) {
        this.diseases = diseases;
    }
}
