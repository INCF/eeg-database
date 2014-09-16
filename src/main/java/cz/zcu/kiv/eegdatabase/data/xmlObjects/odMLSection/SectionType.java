package cz.zcu.kiv.eegdatabase.data.xmlObjects.odMLSection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.io.Serializable;
import java.util.List;

/**
 * ********************************************************************************************************************
 * <p/>
 * This file is part of the ${PROJECT_NAME} project
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
 * ${NAME}, 2014/07/01 11:57 Prokop
 * <p/>
 * ********************************************************************************************************************
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SectionType implements Serializable {

    private String name;

    private boolean required;

    private boolean sectionUsed;

    private int max;

    private int min;

    @XmlElementWrapper(name="Subsections")
    @XmlElement(name="Subsection")
    private List<SectionType> subsections;

    public SectionType(String name, boolean required, List<SectionType> subsections,
                       int min, int max, boolean sectionUsed) {
        this.name = name;
        this.required = required;
        this.max = max;
        this.subsections = subsections;
        this.min = min;
        this.sectionUsed = sectionUsed;
    }

    public SectionType() {
    }

    /*
    * Checks if list of subsections is null or empty.
    *
    * @return true if list of subsections is null or empty
    */
    public boolean hasEmptyOrNullSubsections() {
        return subsections == null || subsections.isEmpty();
    }

    public String getName() {
        return name;
    }

    public boolean isRequired() {
        return required;
    }

    public int getMax() {
        return max;
    }

    public List<SectionType> getSubsections() {
        return subsections;
    }

    public int getMin() {
        return min;
    }

    public boolean isSectionUsed() {
        return sectionUsed;
    }

    public void setSectionUsed(boolean sectionUsed) {
        this.sectionUsed = sectionUsed;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setSubsections(List<SectionType> subsections) {
        this.subsections = subsections;
    }
}
