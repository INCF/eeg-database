package cz.zcu.kiv.eegdatabase.data.pojo;

import cz.zcu.kiv.eegdatabase.data.annotation.SolrField;
import cz.zcu.kiv.eegdatabase.data.annotation.SolrId;
import cz.zcu.kiv.eegdatabase.logic.indexing.IndexField;

import javax.persistence.*;

/**
 * * @author Najt
 */
@Entity
@javax.persistence.Table(name = "GROUP_PERMISSION_REQUEST")
public class GroupPermissionRequest implements java.io.Serializable {
    @SolrId
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "REQUEST_ID")
    private int requestId;
    @ManyToOne
    @JoinColumn(name = "PERSON_ID")
    private Person person;
    @ManyToOne
    @JoinColumn(name = "RESEARCH_GROUP_ID")
    private ResearchGroup researchGroup;
    @SolrField(name = IndexField.TEXT)
    @Column(name = "REQUESTED_PERMISSION")
    private String requestedPermission;
    @Column(name = "GRANTED")
    private boolean granted = false;

    public boolean isGranted() {
        return granted;
    }

    public void setGranted(boolean granted) {
        this.granted = granted;
    }

    public ResearchGroup getResearchGroup() {
        return researchGroup;
    }

    public void setResearchGroup(ResearchGroup group) {
        this.researchGroup = group;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getRequestedPermission() {
        return requestedPermission;
    }

    public void setRequestedPermission(String requestedPermission) {
        this.requestedPermission = requestedPermission;
    }


}


