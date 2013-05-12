package cz.zcu.kiv.eegdatabase.webservices.client.wrappers;

import java.util.LinkedList;
import java.util.List;

/**
 * Class for gathering important information about metadata definition. Meant to
 * be sent between eegclient and portal's client service.
 * 
 * @author František Liška
 */
public class FileMetadataParamDefInfo {
	private int fileMetadataParamDefId;
	private String paramName;
	private String paramDataType;
	private int defaultNumber;
	private List<Integer> researchGroupIdList = new LinkedList<Integer>();

	public int getFileMetadataParamDefId() {
		return fileMetadataParamDefId;
	}

	public void setFileMetadataParamDefId(int fileMetadataParamDefId) {
		this.fileMetadataParamDefId = fileMetadataParamDefId;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamDataType() {
		return paramDataType;
	}

	public void setParamDataType(String paramDataType) {
		this.paramDataType = paramDataType;
	}

	public int getDefaultNumber() {
		return defaultNumber;
	}

	public void setDefaultNumber(int defaultNumber) {
		this.defaultNumber = defaultNumber;
	}

	public List<Integer> getResearchGroupIdList() {
		return researchGroupIdList;
	}

	public void setResearchGroupIdList(List<Integer> researchGroupIdList) {
		this.researchGroupIdList = researchGroupIdList;
	}
}
