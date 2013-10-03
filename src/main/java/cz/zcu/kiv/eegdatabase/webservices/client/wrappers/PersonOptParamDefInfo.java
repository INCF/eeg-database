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
 *   PersonOptParamDefInfo.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.webservices.client.wrappers;

import java.util.LinkedList;
import java.util.List;

/**
 * Class for gathering important information about optional parameter definition
 * for people. Meant to be sent between eegclient and portal's client service.
 * 
 * @author František Liška
 */
public class PersonOptParamDefInfo {
	private int personOptParamDefId;
	private String paramName;
	private String paramDataType;
	private int defaultNumber;
	private List<Integer> researchGroupIdList = new LinkedList<Integer>();

	public int getPersonOptParamDefId() {
		return personOptParamDefId;
	}

	public void setPersonOptParamDefId(int personOptParamDefId) {
		this.personOptParamDefId = personOptParamDefId;
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
