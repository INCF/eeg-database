package cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Petr Miko
 *         Date: 9.2.13
 */
@XmlRootElement(name = "experiments")
public class ExperimentDataList {

    @XmlElement(name = "experiment")
    public List<ExperimentData> experiments;

    public ExperimentDataList() {
        this(new ArrayList<ExperimentData>());
    }

    public ExperimentDataList(List<ExperimentData> experiments) {
        this.experiments = experiments;
    }
}
