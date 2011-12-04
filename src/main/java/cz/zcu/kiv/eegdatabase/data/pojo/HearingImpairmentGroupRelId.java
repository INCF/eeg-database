package cz.zcu.kiv.eegdatabase.data.pojo;

/**
 * @author František Liška
 */
public class HearingImpairmentGroupRelId implements java.io.Serializable {

     private int hearingImpairmentId;
     private int researchGroupId;

    public HearingImpairmentGroupRelId() {
    }

    public HearingImpairmentGroupRelId(int hearingImpairmentId, int researchGroupId) {
       this.hearingImpairmentId = hearingImpairmentId;
       this.researchGroupId = researchGroupId;
    }
   
    public int getHearingImpairmentId() {
        return this.hearingImpairmentId;
    }
    
    public void setHearingImpairmentId(int hearingImpairmentId) {
        this.hearingImpairmentId = hearingImpairmentId;
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
		 if ( !(other instanceof HearingImpairmentGroupRelId) ) return false;
		 HearingImpairmentGroupRelId castOther = (HearingImpairmentGroupRelId) other;
         
		 return (this.getHearingImpairmentId()==castOther.getHearingImpairmentId())
 && (this.getResearchGroupId()==castOther.getResearchGroupId());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getHearingImpairmentId();
         result = 37 * result + this.getResearchGroupId();
         return result;
   }   


}
