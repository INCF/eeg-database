package cz.zcu.kiv.eegdatabase.data.pojo;

import javax.persistence.Embeddable;

/**
 * @author František Liška
 */
@Embeddable
public class HardwareGroupRelId  implements java.io.Serializable {

     private int hardwareId;
     private int researchGroupId;

    public HardwareGroupRelId() {
    }

    public HardwareGroupRelId(int hardwareId, int researchGroupId) {
       this.hardwareId = hardwareId;
       this.researchGroupId = researchGroupId;
    }
   
    public int getHardwareId() {
        return this.hardwareId;
    }
    
    public void setHardwareId(int hardwareId) {
        this.hardwareId = hardwareId;
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
		 if ( !(other instanceof HardwareGroupRelId) ) return false;
		 HardwareGroupRelId castOther = ( HardwareGroupRelId ) other;
         
		 return (this.getHardwareId()==castOther.getHardwareId())
 && (this.getResearchGroupId()==castOther.getResearchGroupId());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getHardwareId();
         result = 37 * result + this.getResearchGroupId();
         return result;
   }   


}
