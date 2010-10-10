package cz.zcu.kiv.eegdatabase.data.pojo;

/**
 * * @author Najt
 */
public class GroupPermissionRequest implements java.io.Serializable {
  private int requestId;
  private Person person;
  private ResearchGroup researchGroup;
  private String requestedPermission;
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


