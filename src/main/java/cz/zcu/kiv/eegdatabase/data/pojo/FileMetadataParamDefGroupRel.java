package cz.zcu.kiv.eegdatabase.data.pojo;


import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


/**
 * @author František Liška
 */
@Entity
@javax.persistence.Table(name = "FILE_METADATA_PARAM_GROUP_REL")
public class FileMetadataParamDefGroupRel implements java.io.Serializable {

    @EmbeddedId
    private FileMetadataParamDefGroupRelId id;

    @ManyToOne
    @JoinColumn(name = "RESEARCH_GROUP_ID")
    private ResearchGroup researchGroup;
    @ManyToOne
    @JoinColumn(name = "FILE_METADATA_PARAM_DEF_ID")
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

