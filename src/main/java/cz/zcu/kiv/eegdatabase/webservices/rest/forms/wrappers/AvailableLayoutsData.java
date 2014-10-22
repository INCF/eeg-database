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
 * AvailableLayoutsData.java, 14. 2. 2014 10:25:55, Jakub Krauz
 *
 **********************************************************************************************************************/
package cz.zcu.kiv.eegdatabase.webservices.rest.forms.wrappers;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Wrapper for available form-layouts data.
 * Required for XML marshalling and/or JSON format.
 * 
 * @author Jakub Krauz
 */
@XmlRootElement(name = "layout")
public class AvailableLayoutsData {
	
	/** Name of the form. */
	private String formName;
	
	/** Name of the layout. */
	private String layoutName;
	
	/** Type of the template. */
	private String type;
	
	
	/**
	 * Implicit constructor.
	 */
	public AvailableLayoutsData() {
		this(null, null, null);
	}
	
	
	/**
	 * Initializing constructor.
	 * @param formName - name of the form
	 * @param layoutName - name of the layout
	 */
	public AvailableLayoutsData(String formName, String layoutName, String type) {
		this.formName = formName;
		this.layoutName = layoutName;
		this.type = type;
	}

	
	/**
	 * Gets the name of the form.
	 * @return name of the form
	 */
	public String getFormName() {
		return formName;
	}

	
	/**
	 * Sets the name of the form.
	 * @param formName - the name of the form
	 */
	public void setFormName(String formName) {
		this.formName = formName;
	}

	
	/**
	 * Gets the name of the layout.
	 * @return name of the layout
	 */
	public String getLayoutName() {
		return layoutName;
	}

	
	/**
	 * Sets the name of the layout.
	 * @param layoutName - the name of the layout
	 */
	public void setLayoutName(String layoutName) {
		this.layoutName = layoutName;
	}


    /**
     * Gets the type of the template.
     * @return the type
     */
    public String getType() {
        return type;
    }


    /**
     * Sets the type of the template.
     * @param type - the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

}
