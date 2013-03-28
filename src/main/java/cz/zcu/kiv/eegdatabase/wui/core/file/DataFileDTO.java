package cz.zcu.kiv.eegdatabase.wui.core.file;

import cz.zcu.kiv.eegdatabase.wui.core.dto.IdentifiDTO;

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
