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
 * FormLayout.java, 18. 2. 2014 17:42:59, Jakub Krauz
 *
 **********************************************************************************************************************/
package cz.zcu.kiv.eegdatabase.data.pojo;

import java.io.Serializable;
import java.sql.Blob;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "FORM_LAYOUT")
public class FormLayout implements Serializable {

	private static final long serialVersionUID = -1422069481513538122L;

	private int formLayoutId;
	
	private String formName;
	
	private String layoutName;
	
	private Blob content;
	
	private Person person;
	
	public FormLayout() {
	}
	
	public FormLayout(String formName, String layoutName, Blob content, Person person) {
		this.formName = formName;
		this.layoutName = layoutName;
		this.content = content;
		this.person = person;
	}

	@GenericGenerator(name = "generator", strategy = "increment")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "FORM_LAYOUT_ID", nullable = false, precision = 22, scale = 0)
	public int getFormLayoutId() {
		return formLayoutId;
	}

	public void setFormLayoutId(int formLayoutId) {
		this.formLayoutId = formLayoutId;
	}

	@Column(name = "FORM_NAME", nullable = false, length = 50)
	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	@Column(name = "LAYOUT_NAME", nullable = false, length = 50)
	public String getLayoutName() {
		return layoutName;
	}

	public void setLayoutName(String layoutName) {
		this.layoutName = layoutName;
	}

	@Basic(fetch = FetchType.LAZY)
	@Lob
	@Column(name = "CONTENT", nullable = false)
	public Blob getContent() {
		return content;
	}

	public void setContent(Blob content) {
		this.content = content;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSON_ID")
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	

}
