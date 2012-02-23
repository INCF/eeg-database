package cz.zcu.kiv.eegdatabase.webservices.dataDownload.wrappers;

/**
 * Class for gathering few important information about data file.
 *
 * Meant to be sent to user.
 *
 * @author: Petr Miko (miko.petr at gmail.com)
 */
public class DataFileInfo {

    private int fileId;
   // private double samplingRate;
    private String fileName;
    private long fileLength;
    private int experimentId;
    private String mimeType;
    private boolean added;
    private boolean changed;
    private long scn;

    /**
     * Getter of experiment identifier.
     *
     * @return experiment identifier
     */
    public int getExperimentId() {
        return experimentId;
    }

    /**
     * Getter file identifier.
     *
     * @return file identifier
     */
    public int getFileId() {
        return fileId;
    }

    /**
     * Getter of file's name.
     *
     * @return file's name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Getter of file's size in bytes.
     *
     * @return file's size in bytes
     */
    public long getFileLength() {
        return fileLength;
    }

    /**
     * Getter of file's sampling rate.
     *
     * @return file's sampling rate
     */
  //  public double getSamplingRate() {
  //      return samplingRate;
  //  }

    /**
     * Getter of MIME type String.
     *
     * @return MIME type
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * Setter of experiment identifier.
     *
     * @param experimentId experiment identifier
     */
    public void setExperimentId(int experimentId) {
        this.experimentId = experimentId;
    }

    /**
     * Setter of file's identifier.
     *
     * @param fileId file's identifier
     */
    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    /**
     * Setter of file's name.
     *
     * @param fileName file name String
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Setter of file's size in bytes.
     *
     * @param fileLength file's size in bytes
     */
    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    /**
     * Setter of file's sampling rate.
     *
     * @param samplingRate file's sampling rate
     */
   // public void setSamplingRate(double samplingRate) {
   //     this.samplingRate = samplingRate;
   // }

    /**
     * Setter of file's MIME type.
     *
     * @param mimeType file's MIME type
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * Object is meant to create new record.
     * @return new record
     */
    public boolean isAdded() {
        return added;
    }

    /**
     * Mark object to create new record.
     * @param added new record
     */
    public void setAdded(boolean added) {
        this.added = added;
    }

    /**
     * Object is meant to update existing record.
     * @return updated record
     */
    public boolean isChanged() {
        return changed;
    }

    /**
     * Mark object to update an existing object.
     * @param changed updated record
     */
    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    /**
     * Getter of revision number (oracle scn).
     * @return revision number
     */
    public long getScn() {
        return scn;
    }

    /**
     * Setter of revision number (oracle scn).
     *
     * @param scn revision number
     */
    public void setScn(long scn) {
        this.scn = scn;
    }
}
