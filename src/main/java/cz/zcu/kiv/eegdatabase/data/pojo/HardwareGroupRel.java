package cz.zcu.kiv.eegdatabase.data.pojo;


import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


/**
 * @author František Liška
 */
@Entity
@javax.persistence.Table(name = "HARDWARE_GROUP_REL")
public class HardwareGroupRel implements java.io.Serializable {
    @EmbeddedId
  private HardwareGroupRelId id;
    @ManyToOne
    @JoinColumn(name = "RESEARCH_GROUP_ID")
  private ResearchGroup researchGroup;
    @ManyToOne
    @JoinColumn(name = "HARDWARE_ID")
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

