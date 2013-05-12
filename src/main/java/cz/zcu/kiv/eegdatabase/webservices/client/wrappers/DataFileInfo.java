package cz.zcu.kiv.eegdatabase.webservices.client.wrappers;

/**
 * Class for gathering important information about data file. Meant to be sent
 * between eegclient and portal's client service.
 * 
 * @author František Liška
 */
public class DataFileInfo {
	private int dataFileId;
	private String filename;
	private long fileLength;
	private int experimentId;
	private String mimetype;
	private String description;

	public int getExperimentId() {
		return experimentId;
	}

	public int getDataFileId() {
		return dataFileId;
	}

	public String getFilename() {
		return filename;
	}

	public long getFileLength() {
		return fileLength;
	}

	public String getMimetype() {
		return mimetype;
	}

	public void setExperimentId(int experimentId) {
		this.experimentId = experimentId;
	}

	public void setDataFileId(int fileId) {
		this.dataFileId = fileId;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
