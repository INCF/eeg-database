package cz.zcu.kiv.eegdatabase.data.pojo;

/**
 * @author František Liška
 */
public class FileMetadataParamDefGroupRel implements java.io.Serializable {
  private FileMetadataParamDefGroupRelId id;
  private ResearchGroup researchGroup;
  private FileMetadataParamDef fileMetadataParamDef;

  public FileMetadataParamDefGroupRel() {
  }

  public FileMetadataParamDefGroupRel(FileMetadataParamDefGroupRelId id, ResearchGroup researchGroup, FileMetadataParamDef fileMetadataParamDef) {
    this.id = id;
    this.researchGroup = researchGroup;
    this.fileMetadataParamDef = fileMetadataParamDef;
  }

  public FileMetadataParamDefGroupRelId getId() {
    return this.id;
  }

  public void setId(FileMetadataParamDefGroupRelId id) {
    this.id = id;
  }

  public ResearchGroup getResearchGroup() {
    return this.researchGroup;
  }

  public void setResearchGroup(ResearchGroup researchGroup) {
    this.researchGroup = researchGroup;
  }

  public FileMetadataParamDef getFileMetadataParamDef() {
    return this.fileMetadataParamDef;
  }

  public void setFileMetadataParamDef(FileMetadataParamDef fileMetadataParamDef) {
    this.fileMetadataParamDef = fileMetadataParamDef;
  }

}

