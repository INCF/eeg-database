package cz.zcu.kiv.eegdatabase.wui.core.file;

import cz.zcu.kiv.eegdatabase.wui.core.dto.IdentifiDTO;

/**
 * Object for download file from wicket. Files are in blobs and we use hibernate entities fetched 
 * still is here problem with access in blob without runnig transaction. I use this object for preparing
 * file for download or upload. 
 * 
 * @author Jakub Rinkes
 * 
 */
public class DataFileDTO extends IdentifiDTO {

    private static final long serialVersionUID = -6900316858954849742L;

    private String fileName;
    private String mimetype;
    byte[] fileContent = new byte[0];

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

}
