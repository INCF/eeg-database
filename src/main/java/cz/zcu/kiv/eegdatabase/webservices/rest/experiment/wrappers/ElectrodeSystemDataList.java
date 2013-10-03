/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   ElectrodeSystemDataList.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data container for collection of electrode system data containers.
 * Able of XML marshaling.
 *
 * @author Petr Miko
 */
@XmlRootElement(name = "electrodeSystems")
public class ElectrodeSystemDataList {

    @XmlElement(name = "electrodeSystem")
    public List<ElectrodeSystemData> electrodeSystems;

    public ElectrodeSystemDataList(){
        this(new ArrayList<ElectrodeSystemData>());
    }

    public ElectrodeSystemDataList(List<ElectrodeSystemData> electrodeSystems) {
        this.electrodeSystems = electrodeSystems;
    }
}
