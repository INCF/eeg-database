package cz.zcu.kiv.eegdatabase.data.pojo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Arrays;

/**
 * ********************************************************************************************************************
 * <p/>
 * This file is part of the eegdatabase project
 * <p/>
 * ==========================================
 * <p/>
 * Copyright (C) 2014 by University of West Bohemia (http://www.zcu.cz/en/)
 * <p/>
 * **********************************************************************************************************************
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * <p/>
 * **********************************************************************************************************************
 * <p/>
 * Template, 2014/07/07 13:23 Prokop
 * <p/>
 * ********************************************************************************************************************
 */
@Entity
public class Template {
    private int templateId;
    private byte[] template;
    private Person personByPersonId;

    @GenericGenerator(name = "generator", strategy = "increment")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "template_id", nullable = false, insertable = true, updatable = true)
    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    @Basic
    @Column(name = "template", nullable = false, insertable = true, updatable = true)
    public byte[] getTemplate() {
        return template;
    }

    public void setTemplate(byte[] template) {
        this.template = template;
    }

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    public Person getPersonByPersonId() {
        return personByPersonId;
    }

    public void setPersonByPersonId(Person personByPersonId) {
        this.personByPersonId = personByPersonId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Template)) return false;

        Template template1 = (Template) o;

        if (templateId != template1.templateId) return false;
        if (personByPersonId != null ? !personByPersonId.equals(template1.personByPersonId) : template1.personByPersonId != null)
            return false;
        if (!Arrays.equals(template, template1.template)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = templateId;
        result = 31 * result + (template != null ? Arrays.hashCode(template) : 0);
        result = 31 * result + (personByPersonId != null ? personByPersonId.hashCode() : 0);
        return result;
    }
}
