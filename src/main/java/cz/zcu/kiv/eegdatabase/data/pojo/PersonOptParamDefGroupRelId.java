package cz.zcu.kiv.eegdatabase.data.pojo;

/**
 * @author František Liška
 */
public class PersonOptParamDefGroupRelId implements java.io.Serializable {

     private int personOptParamDefId;
     private int researchGroupId;

    public PersonOptParamDefGroupRelId() {
    }

    public PersonOptParamDefGroupRelId(int personOptParamDefId, int researchGroupId) {
       this.personOptParamDefId = personOptParamDefId;
       this.researchGroupId = researchGroupId;
    }
   
    public int getPersonOptParamDefId() {
        return this.personOptParamDefId;
    }
    
    public void setPersonOptParamDefId(int personOptParamDefId) {
        this.personOptParamDefId = personOptParamDefId;
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
		 if ( !(other instanceof PersonOptParamDefGroupRelId) ) return false;
		 PersonOptParamDefGroupRelId castOther = (PersonOptParamDefGroupRelId) other;
         
		 return (this.getPersonOptParamDefId()==castOther.getPersonOptParamDefId())
 && (this.getResearchGroupId()==castOther.getResearchGroupId());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getPersonOptParamDefId();
         result = 37 * result + this.getResearchGroupId();
         return result;
   }   


}
