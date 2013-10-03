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
 *   StimulusType.java, 2013/10/02 00:01 Jakub Rinkes
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
 * Date: 20.2.12
 * Time: 14:31
 * To change this template use File | Settings | File Templates.
 */
@Entity
@javax.persistence.Table(name="STIMULUS_TYPE")
public class StimulusType implements Serializable {
    @SolrId
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "STIMULUS_TYPE_ID")
    private int stimulusTypeId;
    @SolrField(name = IndexField.TEXT)
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "IS_DEFAULT")
    private int defaultNumber;
    @OneToMany(mappedBy = "stimulusType")
    private Set<StimulusRel> stimulusRels = new HashSet<StimulusRel>(0);
    private Set<ResearchGroup> researchGroups = new HashSet<ResearchGroup>(0);


    public StimulusType() {
    }

    public StimulusType(int stimulusTypeId, String description) {
        stimulusTypeId = stimulusTypeId;
        this.description = description;
    }

    public int getStimulusTypeId() {
        return stimulusTypeId;
    }

    public void setStimulusTypeId(int stimulusTypeId) {
        stimulusTypeId = stimulusTypeId;
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

    public Set<StimulusRel> getStimulusRels() {
        return stimulusRels;
    }

    public void setStimulusRels(Set<StimulusRel> stimulusRels) {
        this.stimulusRels = stimulusRels;
    }

    public Set<ResearchGroup> getResearchGroups() {
        return researchGroups;
    }

    public void setResearchGroups(Set<ResearchGroup> researchGroups) {
        this.researchGroups = researchGroups;
    }
}
