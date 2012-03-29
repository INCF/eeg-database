package cz.zcu.kiv.eegdatabase.data.pojo;


import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


/**
 * @author František Liška
 */
@Entity
@javax.persistence.Table(name = "EXPERIMENT_OPT_PARAM_GROUP_REL")
public class ExperimentOptParamDefGroupRel implements java.io.Serializable {
    @EmbeddedId
    private ExperimentOptParamDefGroupRelId id;

    @ManyToOne
    @JoinColumn(name = "RESEARCH_GROUP_ID")
    private ResearchGroup researchGroup;
    @ManyToOne
    @JoinColumn(name = "EXPERIMENT_OPT_PARAM_DEF_ID")
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

