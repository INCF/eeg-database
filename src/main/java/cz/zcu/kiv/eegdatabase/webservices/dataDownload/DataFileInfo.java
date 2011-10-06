package cz.zcu.kiv.eegdatabase.webservices.dataDownload;

/**
 * Class for gathering few important information about data file.
 * Meant to be sent to user.
 *
 * @author: Petr Miko (miko.petr at gmail.com)
 */
public class DataFileInfo {

    private int fileId;
	private double samplingRate;
	private String fileName;
	private long fileLength;
	private int experimentId;
	private String mimeType;

	public DataFileInfo() {}

	public int getExperimentId() {
		return experimentId;
	}

	public int getFileId() {
		return fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public long getFileLength() {
		return fileLength;
	}

	public double getSamplingRate() {
		return samplingRate;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setExperimentId(int experimentId) {
		this.experimentId = experimentId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}

	public void setSamplingRate(double samplingRate) {
		this.samplingRate = samplingRate;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
}
