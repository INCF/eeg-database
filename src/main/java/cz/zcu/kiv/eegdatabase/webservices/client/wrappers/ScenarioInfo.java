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
 *   ScenarioInfo.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.webservices.client.wrappers;

/**
 * Class for gathering important information about scenario. Meant to be sent
 * between eegclient and portal's client service.
 * 
 * @author František Liška
 */
public class ScenarioInfo {
	private int scenarioId;
	private int ownerPersonId;
	private String title;
	private int scenarioLength;
	private long fileLength;
	private boolean privateScenario;
	private int researchGroupId;
	private String description;
	private String scenarioName;
	private String mimetype;

	public int getScenarioId() {
		return scenarioId;
	}

	public void setScenarioId(int scenarioId) {
		this.scenarioId = scenarioId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getScenarioLength() {
		return scenarioLength;
	}

	public void setScenarioLength(int scenarioLength) {
		this.scenarioLength = scenarioLength;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getScenarioName() {
		return scenarioName;
	}

	public void setScenarioName(String scenarioName) {
		this.scenarioName = scenarioName;
	}

	public String getMimetype() {
		return mimetype;
	}

	public long getFileLength() {
		return fileLength;
	}

	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	public boolean isPrivateScenario() {
		return privateScenario;
	}

	public void setPrivateScenario(boolean privateScenario) {
		this.privateScenario = privateScenario;
	}

	public int getResearchGroupId() {
		return researchGroupId;
	}

	public void setResearchGroupId(int researchGroupId) {
		this.researchGroupId = researchGroupId;
	}

	public int getOwnerPersonId() {
		return ownerPersonId;
	}

	public void setOwnerPersonId(int ownerPersonId) {
		this.ownerPersonId = ownerPersonId;
	}
}
