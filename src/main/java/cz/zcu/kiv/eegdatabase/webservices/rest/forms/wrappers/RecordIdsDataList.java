/***********************************************************************************************************************
 *
 * This file is part of the EEG-database project
 *
 * =============================================
 *
 * Copyright (C) 2014 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************
 *
 * RecordIdsDataList.java, 1. 5. 2014 10:15:18, Jakub Krauz
 *
 **********************************************************************************************************************/
package cz.zcu.kiv.eegdatabase.webservices.rest.forms.wrappers;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Data container for list of IDs of available data records.
 * Required for XML marshaling, supports also JSON format.
 * 
 * @author Jakub Krauz
 */
@XmlRootElement(name = "ids")
public class RecordIdsDataList {
	
	/** List of IDs. */
	private List<Integer> ids;
	
	
	/**
	 * Implicit constructor.
	 */
	public RecordIdsDataList() {
		this(new ArrayList<Integer>());
	}
	
	
	/**
	 * Initializing constructor.
	 * @param ids - list of IDs
	 */
	public RecordIdsDataList(List<Integer> ids) {
		this.ids = ids;
	}

	
	/**
	 * Gets the list of IDs.
	 * @return list of IDs
	 */
	@XmlElement(name = "id")
	public List<Integer> getIds() {
		return ids;
	}

	
	/**
	 * Sets the list of IDs.
	 * @param ids - list of IDs
	 */
	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

}
