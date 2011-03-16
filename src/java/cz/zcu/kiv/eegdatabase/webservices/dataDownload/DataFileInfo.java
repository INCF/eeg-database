package cz.zcu.kiv.eegdatabase.webservices.dataDownload;

/**
 * @author: Petr Miko
 */
public class DataFileInfo {

    private int fileId;
    private int experimentId;
    private String scenarioName;
    private String filename;
    private String mimeType;
    private long length;

    public DataFileInfo() {
    }

    public DataFileInfo(int experimentId, String scenarioName, int fileId, String filename,
                        String mimeType, long length) {

        this.fileId = fileId;
        this.filename = filename;
        this.mimeType = mimeType;
        this.experimentId = experimentId;
        this.scenarioName = scenarioName;
        this.length = length;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    public int getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(int experimentId) {
        this.experimentId = experimentId;
    }


    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }
}
