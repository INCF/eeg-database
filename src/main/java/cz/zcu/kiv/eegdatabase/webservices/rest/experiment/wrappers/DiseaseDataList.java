package cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
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
        this(new ArrayList<DiseaseData>());
    }

    public DiseaseDataList(List<DiseaseData> diseases) {
        this.diseases = diseases;
    }
}
