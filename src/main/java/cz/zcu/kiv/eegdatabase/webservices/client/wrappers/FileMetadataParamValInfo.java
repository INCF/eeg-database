package cz.zcu.kiv.eegdatabase.webservices.client.wrappers;

import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamValId;

/**
 * @author František Liška
 */
public class FileMetadataParamValInfo {
    private FileMetadataParamValId id;
    private String metadataValue;

    public String getMetadataValue() {
        return metadataValue;
    }

    public void setMetadataValue(String metadataValue) {
        this.metadataValue = metadataValue;
    }

    public FileMetadataParamValId getId() {
        return id;
    }

    public void setId(FileMetadataParamValId id) {
        this.id = id;
    }
}
