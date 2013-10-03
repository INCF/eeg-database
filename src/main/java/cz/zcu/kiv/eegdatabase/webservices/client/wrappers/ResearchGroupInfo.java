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
 *   ResearchGroupInfo.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.webservices.client.wrappers;

import java.util.LinkedList;
import java.util.List;

/**
 * Class for gathering important information about research group. Meant to be
 * sent between eegclient and portal's client service.
 * 
 * @author František Liška
 */
public class ResearchGroupInfo {
	private int researchGroupId;
	private int personOwner;
	private String title;
	private String description;
	private List<HardwareInfo> hardwares = new LinkedList<HardwareInfo>();
	private List<WeatherInfo> weathers = new LinkedList<WeatherInfo>();
	private List<PersonOptParamDefInfo> personOptParamDefInfos = new LinkedList<PersonOptParamDefInfo>();
	private List<ExperimentOptParamDefInfo> experimentOptParamDefInfos = new LinkedList<ExperimentOptParamDefInfo>();
	private List<FileMetadataParamDefInfo> fileMetadataParamDefInfos = new LinkedList<FileMetadataParamDefInfo>();
	private List<ResearchGroupMembershipInfo> researchGroupMembershipInfos = new LinkedList<ResearchGroupMembershipInfo>();

	public int getResearchGroupId() {
		return researchGroupId;
	}

	public void setResearchGroupId(int researchGroupId) {
		this.researchGroupId = researchGroupId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<HardwareInfo> getHardwares() {
		return hardwares;
	}

	public void setHardwares(List<HardwareInfo> hardwares) {
		this.hardwares = hardwares;
	}

	public List<WeatherInfo> getWeathers() {
		return weathers;
	}

	public void setWeathers(List<WeatherInfo> weathers) {
		this.weathers = weathers;
	}

	public List<PersonOptParamDefInfo> getPersonOptParamDefInfos() {
		return personOptParamDefInfos;
	}

	public void setPersonOptParamDefInfos(List<PersonOptParamDefInfo> personOptParamDefInfos) {
		this.personOptParamDefInfos = personOptParamDefInfos;
	}

	public List<ExperimentOptParamDefInfo> getExperimentOptParamDefInfos() {
		return experimentOptParamDefInfos;
	}

	public void setExperimentOptParamDefInfos(List<ExperimentOptParamDefInfo> experimentOptParamDefInfos) {
		this.experimentOptParamDefInfos = experimentOptParamDefInfos;
	}

	public List<FileMetadataParamDefInfo> getFileMetadataParamDefInfos() {
		return fileMetadataParamDefInfos;
	}

	public void setFileMetadataParamDefInfos(List<FileMetadataParamDefInfo> fileMetadataParamDefInfos) {
		this.fileMetadataParamDefInfos = fileMetadataParamDefInfos;
	}

	public int getPersonOwner() {
		return personOwner;
	}

	public void setPersonOwner(int personOwner) {
		this.personOwner = personOwner;
	}

	public List<ResearchGroupMembershipInfo> getResearchGroupMembershipInfos() {
		return researchGroupMembershipInfos;
	}

	public void setResearchGroupMembershipInfos(List<ResearchGroupMembershipInfo> researchGroupMembershipInfos) {
		this.researchGroupMembershipInfos = researchGroupMembershipInfos;
	}
}
