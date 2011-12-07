package cz.zcu.kiv.eegdatabase.data.pojo;

/**
 * @author František Liška
 */
public class ExperimentOptParamDefGroupRel implements java.io.Serializable {
  private ExperimentOptParamDefGroupRelId id;
  private ResearchGroup researchGroup;
  private ExperimentOptParamDef experimentOptParamDef;

  public ExperimentOptParamDefGroupRel() {
  }

  public ExperimentOptParamDefGroupRel(ExperimentOptParamDefGroupRelId id, ResearchGroup researchGroup, ExperimentOptParamDef experimentOptParamDef) {
    this.id = id;
    this.researchGroup = researchGroup;
    this.experimentOptParamDef = experimentOptParamDef;
  }

  public ExperimentOptParamDefGroupRelId getId() {
    return this.id;
  }

  public void setId(ExperimentOptParamDefGroupRelId id) {
    this.id = id;
  }

  public ResearchGroup getResearchGroup() {
    return this.researchGroup;
  }

  public void setResearchGroup(ResearchGroup researchGroup) {
    this.researchGroup = researchGroup;
  }

  public ExperimentOptParamDef getExperimentOptParamDef() {
    return this.experimentOptParamDef;
  }

  public void setExperimentOptParamDef(ExperimentOptParamDef experimentOptParamDef) {
    this.experimentOptParamDef = experimentOptParamDef;
  }

}

