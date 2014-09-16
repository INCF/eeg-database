package cz.zcu.kiv.eegdatabase.data.xmlObjects.odMLSection;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

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
 * XMLTemplate, 2014/09/16 10:01 administrator
 * <p/>
 * ********************************************************************************************************************
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLTemplate implements Serializable {

    private String name;

    @XmlElementWrapper(name="Sections")
    @XmlElement(name="Section")
    private List<SectionType> sections;

    public XMLTemplate() { }

    public XMLTemplate(List<SectionType> sections, String name) {
        this.sections = sections;
        this.name = name;
    }

    public List<SectionType> getSections() {
        return sections;
    }

    public String getName() {
        return name;
    }

    public void setSections(List<SectionType> sections) {

        this.sections = sections;
    }

    public void setName(String name) {
        this.name = name;
    }
}
