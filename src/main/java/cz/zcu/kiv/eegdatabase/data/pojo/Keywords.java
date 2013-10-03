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
 *   Keywords.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.data.pojo;

import cz.zcu.kiv.eegdatabase.data.annotation.SolrField;
import cz.zcu.kiv.eegdatabase.data.annotation.SolrId;
import cz.zcu.kiv.eegdatabase.logic.indexing.IndexField;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author Ladislav Janák
 */
@Entity
@javax.persistence.Table(name="KEYWORDS")
public class Keywords implements Serializable {
    @SolrId
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "KEYWORDS_ID")
    private int keywordsId;
    @ManyToOne
    @JoinColumn(name = "RESEARCH_GROUP_ID")
    private ResearchGroup researchGroup;
    @SolrField(name = IndexField.TEXT)
    @Column(name = "KEYWORDS_TEXT")
    private String keywordsText;
    
    public Keywords(){      
    }
    
    public Keywords(int keywordsId, ResearchGroup researchGroup, String keywordsText) {
        this.researchGroup = researchGroup;
        this.keywordsId = keywordsId;
        this.keywordsText = keywordsText;
    }

    /**
     * @return the keywordsId
     */
    public int getKeywordsId() {
        return keywordsId;
    }

    /**
     * @param keywordsId the keywordsId to set
     */
    public void setKeywordsId(int keywordsId) {
        this.keywordsId = keywordsId;
    }

    /**
     * @return the researchGroup
     */
    public ResearchGroup getResearchGroup() {
        return researchGroup;
    }

    /**
     * @param researchGroup the researchGroup to set
     */
    public void setResearchGroup(ResearchGroup researchGroup) {
        this.researchGroup = researchGroup;
    }

    /**
     * @return the keywords
     */
    public String getKeywordsText() {
        return keywordsText;
    }

    /**
     * @param keywords the keywords to set
     */
    public void setKeywordsText(String keywordsText) {
        this.keywordsText = keywordsText;
    }
    
    
}
