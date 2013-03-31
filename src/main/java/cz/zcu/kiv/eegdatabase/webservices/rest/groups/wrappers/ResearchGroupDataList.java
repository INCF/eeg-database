package cz.zcu.kiv.eegdatabase.webservices.rest.groups.wrappers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collections;
import java.util.List;

/**
 * Data container for holding a list of research groups.
 *
 * @author Petr Miko
 */
@XmlRootElement(name = "researchGroups")
public class ResearchGroupDataList {

    @XmlElement(name = "researchGroup")
    public List<ResearchGroupData> researchGroups;

    public ResearchGroupDataList() {
        this(Collections.<ResearchGroupData>emptyList());
    }

    public ResearchGroupDataList(List<ResearchGroupData> researchGroups) {
        this.researchGroups = researchGroups;
    }
}
