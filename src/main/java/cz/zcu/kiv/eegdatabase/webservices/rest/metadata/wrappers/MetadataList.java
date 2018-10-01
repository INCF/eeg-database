package cz.zcu.kiv.eegdatabase.webservices.rest.metadata.wrappers;

import javax.xml.bind.annotation.*;
import java.util.List;

/***********************************************************************************************************************
 *
 * This file is part of the eegdatabase project

 * ==========================================
 *
 * Copyright (C) 2018 by Petr Jezek
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
 * MetadataList, 2018/09/10 14:19 petr-jezek
 *
 **********************************************************************************************************************/
@XmlRootElement(name = "metadataList")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "metadataList")
public class MetadataList {

    public MetadataList(List<Metadata> experimentData) {
        setMetadata(experimentData);
    }

    public MetadataList() {}

    @XmlElement(name = "experiment")
    private List<Metadata> metadata;


    public List<Metadata> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<Metadata> metadata) {
        this.metadata = metadata;
    }
}
