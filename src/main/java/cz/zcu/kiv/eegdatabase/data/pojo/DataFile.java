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
 *   DataFile.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.pojo;
// Generated 19.1.2010 23:18:53 by Hibernate Tools 3.2.1.GA

import cz.zcu.kiv.eegdatabase.data.annotation.SolrField;
import cz.zcu.kiv.eegdatabase.data.annotation.SolrId;
import cz.zcu.kiv.eegdatabase.logic.indexing.IndexField;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Blob;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.GenericGenerator;

/**
 * DataFile generated by hbm2java
 */
@Entity
@Table(name = "DATA_FILE")
public class DataFile implements java.io.Serializable {

	@SolrId
	private int dataFileId;
	private Experiment experiment;
	@SolrField(name = IndexField.TEXT)
	private String description;
	private byte[] fileContent;
	private String mimetype;
	private String filename;
	private Set<FileMetadataParamVal> fileMetadataParamVals = new HashSet<FileMetadataParamVal>(
			0);
	private Set<History> histories = new HashSet<History>(0);
	private Analysis analysis;

	public DataFile() {
	}

	public DataFile(Experiment experiment, byte[] fileContent, String mimetype,
			String filename, Analysis analysis) {
		this.experiment = experiment;
		this.fileContent = fileContent;
		this.mimetype = mimetype;
		this.filename = filename;
		this.analysis = analysis;
	}

	public DataFile(Experiment experiment, String description,
			byte[] fileContent, String mimetype, String filename,
			Set<FileMetadataParamVal> fileMetadataParamVals,
			Set<History> histories, Analysis analysis) {
		this.experiment = experiment;
		this.description = description;
		this.fileContent = fileContent;
		this.mimetype = mimetype;
		this.filename = filename;
		this.fileMetadataParamVals = fileMetadataParamVals;
		this.histories = histories;
		this.analysis = analysis;
	}

	@GenericGenerator(name = "generator", strategy = "increment")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "DATA_FILE_ID", nullable = false, precision = 22, scale = 0)
	public int getDataFileId() {
		return this.dataFileId;
	}

	public void setDataFileId(int dataFileId) {
		this.dataFileId = dataFileId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EXPERIMENT_ID", nullable = false)
	public Experiment getExperiment() {
		return this.experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "FILE_CONTENT", nullable = false)
	public byte[] getFileContent() {
		return this.fileContent;
	}

	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}

	@Column(name = "MIMETYPE", nullable = false, length = 40)
	public String getMimetype() {
		return this.mimetype;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	@Column(name = "FILENAME", nullable = false, length = 80)
	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "dataFile")
	public Set<FileMetadataParamVal> getFileMetadataParamVals() {
		return this.fileMetadataParamVals;
	}

	public void setFileMetadataParamVals(
			Set<FileMetadataParamVal> fileMetadataParamVals) {
		this.fileMetadataParamVals = fileMetadataParamVals;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "dataFile")
	public Set<History> getHistories() {
		return this.histories;
	}

	public void setHistories(Set<History> histories) {
		this.histories = histories;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ANALYSIS_ID", nullable = true)
	public Analysis getAnalysis() {
		return this.analysis;
	}

	public void setAnalysis(Analysis analysis) {
		this.analysis = analysis;
	}

}
