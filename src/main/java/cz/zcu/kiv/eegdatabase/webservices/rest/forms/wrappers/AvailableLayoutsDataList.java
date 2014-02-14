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
 * AvailableLayoutsDataList.java, 14. 2. 2014 10:39:37, Jakub Krauz
 *
 **********************************************************************************************************************/
package cz.zcu.kiv.eegdatabase.webservices.rest.forms.wrappers;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Data container for list of available form-layouts.
 * Required for XML marshaling, supports also JSON format.
 * 
 * @author Jakub Krauz
 */
@XmlRootElement(name = "layouts")
public class AvailableLayoutsDataList {
	
	/** List of data wrappers. */
	@XmlElement(name = "layout")
	public List<AvailableLayoutsData> layouts;
	
	
	/**
	 * Implicit constructor.
	 */
	public AvailableLayoutsDataList() {
		this(new ArrayList<AvailableLayoutsData>());
	}
	
	
	/**
	 * Constructor.
	 * @param layouts - list of data wrappers
	 */
	public AvailableLayoutsDataList(List<AvailableLayoutsData> layouts) {
		this.layouts = layouts;
	}

}
