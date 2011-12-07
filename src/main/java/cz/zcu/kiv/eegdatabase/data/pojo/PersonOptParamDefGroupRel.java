package cz.zcu.kiv.eegdatabase.data.pojo;

/**
 * @author František Liška
 */
public class PersonOptParamDefGroupRel implements java.io.Serializable {
  private PersonOptParamDefGroupRelId id;
  private ResearchGroup researchGroup;
  private PersonOptParamDef personOptParamDef;

  public PersonOptParamDefGroupRel() {
  }

  public PersonOptParamDefGroupRel(PersonOptParamDefGroupRelId id, ResearchGroup researchGroup, PersonOptParamDef personOptParamDef) {
    this.id = id;
    this.researchGroup = researchGroup;
    this.personOptParamDef = personOptParamDef;
  }

  public PersonOptParamDefGroupRelId getId() {
    return this.id;
  }

  public void setId(PersonOptParamDefGroupRelId id) {
    this.id = id;
  }

  public ResearchGroup getResearchGroup() {
    return this.researchGroup;
  }

  public void setResearchGroup(ResearchGroup researchGroup) {
    this.researchGroup = researchGroup;
  }

  public PersonOptParamDef getPersonOptParamDef() {
    return this.personOptParamDef;
  }

  public void setPersonOptParamDef(PersonOptParamDef personOptParamDef) {
    this.personOptParamDef = personOptParamDef;
  }

}

