package cz.zcu.kiv.eegdatabase.webservices.rest.user.wrappers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collections;
import java.util.List;

/**
 * @author Petr Miko
 *         Date: 9.2.13
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
