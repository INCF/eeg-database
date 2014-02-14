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
 * AvailableFormsDataList.java, 14. 2. 2014 18:23:48, Jakub Krauz
 *
 **********************************************************************************************************************/
package cz.zcu.kiv.eegdatabase.webservices.rest.forms.wrappers;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Data container for list of available forms.
 * Required for XML marshaling, supports also JSON format.
 * 
 * @author Jakub Krauz
 */
@XmlRootElement(name = "forms")
public class AvailableFormsDataList {
	
	/** List of form names. */
	private List<String> forms;
	
	
	/**
	 * Implicit constructor.
	 */
	public AvailableFormsDataList() {
		this(new ArrayList<String>());
	}
	
	
	/**
	 * Initializing constructor.
	 * @param forms - list of form names
	 */
	public AvailableFormsDataList(List<String> forms) {
		this.forms = forms;
	}

	
	/**
	 * Gets the list of form names.
	 * @return list of form names
	 */
	@XmlElement(name = "form")
	public List<String> getForms() {
		return forms;
	}


	/**
	 * Sets the list of form names.
	 * @param forms - the list of form names
	 */
	public void setForms(List<String> forms) {
		this.forms = forms;
	}

}
