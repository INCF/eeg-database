package cz.zcu.kiv.eegdatabase.data.pojo;


import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author František Liška
 */
@Entity
@javax.persistence.Table(name = "PERSON_OPT_PARAM_GROUP_REL")
public class PersonOptParamDefGroupRel implements java.io.Serializable {
    @EmbeddedId
    private PersonOptParamDefGroupRelId id;
    @ManyToOne
    @JoinColumn(name = "RESEARCH_GROUP_ID")
    private ResearchGroup researchGroup;
    @ManyToOne
    @JoinColumn(name = "PERSON_OPT_PARAM_DEF_ID")
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

