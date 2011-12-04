package cz.zcu.kiv.eegdatabase.data.pojo;

/**
 * @author František Liška
 */
public class HearingImpairmentGroupRel implements java.io.Serializable {
  private HearingImpairmentGroupRelId id;
  private ResearchGroup researchGroup;
  private HearingImpairment hearingImpairment;

  public HearingImpairmentGroupRel() {
  }

  public HearingImpairmentGroupRel(HearingImpairmentGroupRelId id, ResearchGroup researchGroup, HearingImpairment hearingImpairment) {
    this.id = id;
    this.researchGroup = researchGroup;
    this.hearingImpairment = hearingImpairment;
  }

  public HearingImpairmentGroupRelId getId() {
    return this.id;
  }

  public void setId(HearingImpairmentGroupRelId id) {
    this.id = id;
  }

  public ResearchGroup getResearchGroup() {
    return this.researchGroup;
  }

  public void setResearchGroup(ResearchGroup researchGroup) {
    this.researchGroup = researchGroup;
  }

  public HearingImpairment getHearingImpairment() {
    return this.hearingImpairment;
  }

  public void setHearingImpairment(HearingImpairment hearingImpairment) {
    this.hearingImpairment = hearingImpairment;
  }

}

