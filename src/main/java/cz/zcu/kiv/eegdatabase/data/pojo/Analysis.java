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
 *   Analysis.java, 2013/10/02 00:01 Jakub Rinkes
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
 * Date: 6.3.12
 * Time: 13:46
 * To change this template use File | Settings | File Templates.
 */
@Entity
@javax.persistence.Table(name="ANALYSIS")
public class Analysis implements Serializable {
    @Id
    @SolrId
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ANALYSIS_ID")
    private int analysisId;
    @Column(name = "EPOCH_NUM")
    private int epochNumber;
    @Column(name = "PRESTIMULUS_TIME")
    private int prestimulusTime;
    @Column(name = "POSTSTIMULUS_TIME")
    private int poststimulusTime;
    @SolrField(name = IndexField.TEXT)
    @Column(name = "DESCRIPTION")
    private String description;
    @OneToMany(mappedBy = "analysis")
    private Set<DataFile> dataFiles = new HashSet<DataFile>(0);
    private Set<ResearchGroup> researchGroups = new HashSet<ResearchGroup>(0);

    public Analysis() {
    }

    public Analysis(int analysisId, int epochNumber, int prestimulusTime, int poststimulusTime, String description) {
        this.analysisId = analysisId;
        this.epochNumber = epochNumber;
        this.prestimulusTime = prestimulusTime;
        this.poststimulusTime = poststimulusTime;
        this.description = description;
    }

    public int getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(int analysisId) {
        this.analysisId = analysisId;
    }

    public int getEpochNumber() {
        return epochNumber;
    }

    public void setEpochNumber(int epochNumber) {
        this.epochNumber = epochNumber;
    }

    public int getPrestimulusTime() {
        return prestimulusTime;
    }

    public void setPrestimulusTime(int prestimulusTime) {
        this.prestimulusTime = prestimulusTime;
    }

    public int getPoststimulusTime() {
        return poststimulusTime;
    }

    public void setPoststimulusTime(int poststimulusTime) {
        this.poststimulusTime = poststimulusTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<DataFile> getDataFiles() {
        return dataFiles;
    }

    public void setDataFiles(Set<DataFile> dataFiles) {
        this.dataFiles = dataFiles;
    }

    public Set<ResearchGroup> getResearchGroups() {
        return researchGroups;
    }

    public void setResearchGroups(Set<ResearchGroup> researchGroups) {
        this.researchGroups = researchGroups;
    }
}


