package cz.zcu.kiv.eegdatabase.data.pojo;

/**
 * @author František Liška
 */
public class FileMetadataParamDefGroupRelId implements java.io.Serializable {

     private int fileMetadataParamDefId;
     private int researchGroupId;

    public FileMetadataParamDefGroupRelId() {
    }

    public FileMetadataParamDefGroupRelId(int fileMetadataParamDefId, int researchGroupId) {
       this.fileMetadataParamDefId = fileMetadataParamDefId;
       this.researchGroupId = researchGroupId;
    }
   
    public int getFileMetadataParamDefId() {
        return this.fileMetadataParamDefId;
    }
    
    public void setFileMetadataParamDefId(int fileMetadataParamDefId) {
        this.fileMetadataParamDefId = fileMetadataParamDefId;
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
		 if ( !(other instanceof FileMetadataParamDefGroupRelId) ) return false;
		 FileMetadataParamDefGroupRelId castOther = (FileMetadataParamDefGroupRelId) other;
         
		 return (this.getFileMetadataParamDefId()==castOther.getFileMetadataParamDefId())
 && (this.getResearchGroupId()==castOther.getResearchGroupId());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getFileMetadataParamDefId();
         result = 37 * result + this.getResearchGroupId();
         return result;
   }   


}
