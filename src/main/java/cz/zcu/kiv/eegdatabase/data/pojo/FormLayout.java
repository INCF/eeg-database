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

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity representing a form-layout object.
 *
 * @author Jakub Krauz
 */
@Entity
@Table(name = "FORM_LAYOUT")
public class FormLayout implements Serializable {

    private static final long serialVersionUID = -1422069481513538122L;

    private int formLayoutId;

    private String formName;

    private String layoutName;

    private byte[] content;

    private Person person;

    private FormLayoutType type;

    public FormLayout() {
    }

    public FormLayout(String formName, String layoutName, byte[] content,
            Person person, FormLayoutType type) {
        this.formName = formName;
        this.layoutName = layoutName;
        this.content = content;
        this.person = person;
        this.type = type;
    }

    @Id
    @GenericGenerator(name = "generator", strategy = "increment")
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

    // @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "CONTENT", nullable = false, columnDefinition = "bytea")
    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
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

    @Column(name = "TYPE", nullable = true, precision = 22, scale = 0)
    @Enumerated(EnumType.STRING)
    public FormLayoutType getType() {
        return this.type;
    }

    public void setType(FormLayoutType type) {
        this.type = type;
    }

}
