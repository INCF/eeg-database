package cz.zcu.kiv.eegdatabase.data.pojo;

/**
 * @author František Liška
 */
public class ExperimentOptParamDefGroupRelId implements java.io.Serializable {

     private int experimentOptParamDefId;
     private int researchGroupId;

    public ExperimentOptParamDefGroupRelId() {
    }

    public ExperimentOptParamDefGroupRelId(int experimentOptParamDefId, int researchGroupId) {
       this.experimentOptParamDefId = experimentOptParamDefId;
       this.researchGroupId = researchGroupId;
    }
   
    public int getExperimentOptParamDefId() {
        return this.experimentOptParamDefId;
    }
    
    public void setExperimentOptParamDefId(int experimentOptParamDefId) {
        this.experimentOptParamDefId = experimentOptParamDefId;
    }
    public int getResearchGroupId() {
        return this.researchGroupId;
    }
    
    public void setResearchGroupId(int researchGroupId) {
        this.researchGroupId = researchGroupId;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof ExperimentOptParamDefGroupRelId) ) return false;
		 ExperimentOptParamDefGroupRelId castOther = (ExperimentOptParamDefGroupRelId) other;
         
		 return (this.getExperimentOptParamDefId()==castOther.getExperimentOptParamDefId())
 && (this.getResearchGroupId()==castOther.getResearchGroupId());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getExperimentOptParamDefId();
         result = 37 * result + this.getResearchGroupId();
         return result;
   }   


}
