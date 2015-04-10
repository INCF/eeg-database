package cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;


@XmlRootElement(name = "experiments")
public class ExperimentInfoList {
    private List<ExperimentInfo> experiments;


    public ExperimentInfoList() {
        this(new ArrayList<ExperimentInfo>());
    }

    public ExperimentInfoList(List<ExperimentInfo> experiments) {
        this.experiments = experiments;
    }


    @XmlElement(name = "experiment")
    public List<ExperimentInfo> getExperiments() {
        return experiments;
    }

    public void setExperiments(List<ExperimentInfo> experiments) {
        this.experiments = experiments;
    }
}