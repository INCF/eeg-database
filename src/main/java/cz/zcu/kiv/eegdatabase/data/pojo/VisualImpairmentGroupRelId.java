package cz.zcu.kiv.eegdatabase.data.pojo;

/**
 * @author František Liška
 */
public class VisualImpairmentGroupRelId implements java.io.Serializable {

     private int visualImpairmentId;
     private int researchGroupId;

    public VisualImpairmentGroupRelId() {
    }

    public VisualImpairmentGroupRelId(int visualImpairmentId, int researchGroupId) {
       this.visualImpairmentId = visualImpairmentId;
       this.researchGroupId = researchGroupId;
    }
   
    public int getVisualImpairmentId() {
        return this.visualImpairmentId;
    }
    
    public void setVisualImpairmentId(int visualImpairmentId) {
        this.visualImpairmentId = visualImpairmentId;
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
		 if ( !(other instanceof VisualImpairmentGroupRelId) ) return false;
		 VisualImpairmentGroupRelId castOther = (VisualImpairmentGroupRelId) other;
         
		 return (this.getVisualImpairmentId()==castOther.getVisualImpairmentId())
 && (this.getResearchGroupId()==castOther.getResearchGroupId());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getVisualImpairmentId();
         result = 37 * result + this.getResearchGroupId();
         return result;
   }   


}
