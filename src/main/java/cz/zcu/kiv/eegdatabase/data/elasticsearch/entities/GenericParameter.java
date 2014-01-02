/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.data.elasticsearch.entities;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author bydga
 */
public class GenericParameter {

	private String name;
	private String valueString;
	private Integer valueInteger;
	private Map<String, String> attributes = new HashMap<String, String>();

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

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}
}
