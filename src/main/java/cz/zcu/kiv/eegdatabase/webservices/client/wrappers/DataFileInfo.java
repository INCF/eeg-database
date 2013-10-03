/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   DataFileInfo.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
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
