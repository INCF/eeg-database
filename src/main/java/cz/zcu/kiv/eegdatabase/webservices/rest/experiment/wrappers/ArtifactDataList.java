package cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data container for list of artifacts.
 * Required for XML marshaling.
 *
 * @author Petr Miko
 */
@XmlRootElement(name = "artifacts")
public class ArtifactDataList {

    @XmlElement(name = "artifact")
    public List<ArtifactData> artifacts;

    public ArtifactDataList() {
        this(new ArrayList<ArtifactData>());
    }

    public ArtifactDataList(List<ArtifactData> artifacts) {
        this.artifacts = artifacts;
    }
}
