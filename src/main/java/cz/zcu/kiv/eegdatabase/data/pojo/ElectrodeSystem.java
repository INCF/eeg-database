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
 *   ElectrodeSystem.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.pojo;

import cz.zcu.kiv.eegdatabase.data.annotation.SolrField;
import cz.zcu.kiv.eegdatabase.data.annotation.SolrId;
import cz.zcu.kiv.eegdatabase.logic.indexing.IndexField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 1.3.12
 * Time: 14:32
 * To change this template use File | Settings | File Templates.
 */
@Entity
@javax.persistence.Table(name="ELECTRODE_SYSTEM")
public class ElectrodeSystem implements Serializable {
    @SolrId
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ELECTRODE_SYSTEM_ID")
    private int electrodeSystemId;
    @Column(name = "TITLE")
    @SolrField(name = IndexField.TITLE)
    private String title;
    @Column(name = "DESCRIPTION")
    @SolrField(name = IndexField.TEXT)
    private String description;
    @Column(name = "IS_DEFAULT")
    private int defaultNumber;
    @OneToMany(mappedBy = "electrodeSystem")
    private Set<ElectrodeConf> electrodeConfs = new HashSet<ElectrodeConf>(0);
    private Set<ResearchGroup> researchGroups = new HashSet<ResearchGroup>(0);

    public ElectrodeSystem() {
    }

    public ElectrodeSystem(int electrodeSystemId, String title, String description, int defaultNumber, Set<ElectrodeConf> electrodeConfs) {
        this.electrodeSystemId = electrodeSystemId;
        this.title = title;
        this.description = description;
        this.defaultNumber = defaultNumber;
        this.electrodeConfs = electrodeConfs;
    }

    public int getElectrodeSystemId() {
        return electrodeSystemId;
    }

    public void setElectrodeSystemId(int electrodeSystemId) {
        this.electrodeSystemId = electrodeSystemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDefaultNumber() {
        return defaultNumber;
    }

    public void setDefaultNumber(int defaultNumber) {
        this.defaultNumber = defaultNumber;
    }

    public Set<ElectrodeConf> getElectrodeConfs() {
        return electrodeConfs;
    }

    public void setElectrodeConfs(Set<ElectrodeConf> electrodeConfs) {
        this.electrodeConfs = electrodeConfs;
    }

    public Set<ResearchGroup> getResearchGroups() {
        return researchGroups;
    }

    public void setResearchGroups(Set<ResearchGroup> researchGroups) {
        this.researchGroups = researchGroups;
    }
}
