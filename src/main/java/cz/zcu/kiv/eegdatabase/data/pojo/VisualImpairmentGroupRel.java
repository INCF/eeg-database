package cz.zcu.kiv.eegdatabase.data.pojo;

/**
 * @author František Liška
 */
public class VisualImpairmentGroupRel implements java.io.Serializable {
  private VisualImpairmentGroupRelId id;
  private ResearchGroup researchGroup;
  private VisualImpairment visualImpairment;

  public VisualImpairmentGroupRel() {
  }

  public VisualImpairmentGroupRel(VisualImpairmentGroupRelId id, ResearchGroup researchGroup, VisualImpairment visualImpairment) {
    this.id = id;
    this.researchGroup = researchGroup;
    this.visualImpairment = visualImpairment;
  }

  public VisualImpairmentGroupRelId getId() {
    return this.id;
  }

  public void setId(VisualImpairmentGroupRelId id) {
    this.id = id;
  }

  public ResearchGroup getResearchGroup() {
    return this.researchGroup;
  }

  public void setResearchGroup(ResearchGroup researchGroup) {
    this.researchGroup = researchGroup;
  }

  public VisualImpairment getVisualImpairment() {
    return this.visualImpairment;
  }

  public void setVisualImpairment(VisualImpairment visualImpairment) {
    this.visualImpairment = visualImpairment;
  }

}

