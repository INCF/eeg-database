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
 *   Stimulus.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.pojo;

import cz.zcu.kiv.eegdatabase.data.annotation.SolrField;
import cz.zcu.kiv.eegdatabase.data.annotation.SolrId;
import cz.zcu.kiv.eegdatabase.logic.indexing.IndexField;
import cz.zcu.kiv.eegdatabase.wui.core.common.StimulusFacade;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.IAutoCompletable;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 20.2.12
 * Time: 14:27
 * To change this template use File | Settings | File Templates.
 */
@Entity
@javax.persistence.Table(name="STIMULUS")
public class Stimulus implements Serializable, IAutoCompletable {

    @SolrId
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "STIMULUS_ID")
    private int stimulusId;
    @SolrField(name = IndexField.TEXT)
    @Column(name = "DESCRIPTION")
    private String description;
    @OneToMany(mappedBy = "stimulus")
    private Set<StimulusRel> stimulusRels = new HashSet<StimulusRel>(0);

    public Stimulus() {

    }

    public Stimulus(int stimulusId, String description) {
        this.stimulusId = stimulusId;
        this.description = description;
    }

    public int getStimulusId() {
        return stimulusId;
    }

    public void setStimulusId(int stimulusId) {
        this.stimulusId = stimulusId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<StimulusRel> getStimulusRels() {
        return stimulusRels;
    }

    public void setStimulusRels(Set<StimulusRel> stimulusRels) {
        this.stimulusRels = stimulusRels;
    }

    @Override
    public String getAutoCompleteData() {
        return description;
    }
}
