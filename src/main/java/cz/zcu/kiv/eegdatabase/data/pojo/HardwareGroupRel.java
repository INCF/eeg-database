package cz.zcu.kiv.eegdatabase.data.pojo;

/**
 * @author František Liška
 */
public class HardwareGroupRel implements java.io.Serializable {
  private HardwareGroupRelId id;
  private ResearchGroup researchGroup;
  private Hardware hardware;

  public HardwareGroupRel() {
  }

  public HardwareGroupRel(HardwareGroupRelId id, ResearchGroup researchGroup, Hardware hardware) {
    this.id = id;
    this.researchGroup = researchGroup;
    this.hardware = hardware;
  }

  public HardwareGroupRelId getId() {
    return this.id;
  }

  public void setId(HardwareGroupRelId id) {
    this.id = id;
  }

  public ResearchGroup getResearchGroup() {
    return this.researchGroup;
  }

  public void setResearchGroup(ResearchGroup researchGroup) {
    this.researchGroup = researchGroup;
  }

  public Hardware getHardware() {
    return this.hardware;
  }

  public void setHardware(Hardware hardware) {
    this.hardware = hardware;
  }

}

