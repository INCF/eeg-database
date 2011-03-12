package cz.zcu.kiv.eegdatabase.webservices.dataDownload;

/**
 * @author: Petr Miko
 */
public class DataFileInfo {

    private int fileID;
    private String filename;
    private String mimeType;

    public DataFileInfo(){}

    public DataFileInfo(int fileID, String filename, String mimeType){
        this.fileID = fileID;
        this.filename = filename;
        this.mimeType = mimeType;
    }

    public int getFileID(){
        return fileID;
    }

    public void setFileID(int fileID){
        this.fileID = fileID;
    }

    public String getFilename(){
        return filename;
    }

    public void setFilename(String filename){
        this.filename = filename;
    }

    public String getMimeType(){
        return mimeType;
    }

    public void setMimeType(String mimeType){
        this.mimeType = mimeType;
    }
}
