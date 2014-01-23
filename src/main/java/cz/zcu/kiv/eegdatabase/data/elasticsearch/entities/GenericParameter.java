/**
 * *****************************************************************************
 * This file is part of the EEG-database project
 *
 * ==========================================
 *
 * Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 *  ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 *  ***********************************************************************************************************************
 *
 * GenericParameter.java, 2013/10/02 00:01 Martin Bydzovsky
 * ****************************************************************************
 */
package cz.zcu.kiv.eegdatabase.data.elasticsearch.entities;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bydga
 */
public class GenericParameter {

	private String name;
	private String valueString;
	private Integer valueInteger;
	private List<ParameterAttribute> attributes = new ArrayList<ParameterAttribute>();

	public GenericParameter() {
	}

	public GenericParameter(String name, Integer valueInteger) {
		this.setName(name);
		this.setValueInteger(valueInteger);
	}

	public GenericParameter(String name, String valueString) {
		this.setName(name);
		this.setValueString(valueString);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValueString() {
		return valueString;
	}

	public void setValueString(String valueString) {
		this.valueString = valueString;
	}

	public Integer getValueInteger() {
		return valueInteger;
	}

	public void setValueInteger(Integer valueInteger) {
		this.valueInteger = valueInteger;
	}

	public List<ParameterAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<ParameterAttribute> attributes) {
		this.attributes = attributes;
	}
}
