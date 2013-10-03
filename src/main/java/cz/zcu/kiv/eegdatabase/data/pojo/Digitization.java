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
 *   Digitization.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 20.2.12
 * Time: 14:20
 * To change this template use File | Settings | File Templates.
 */
@Entity
@javax.persistence.Table(name="DIGITIZATION")
public class Digitization implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "DIGITIZATION_ID")
    private int digitizationId;
    @Column(name = "GAIN")
    private float gain;
    @Column(name = "FILTER")
    private String filter;
    @Column(name = "SAMPLING_RATE")
    private float samplingRate;
    @OneToMany(mappedBy = "digitization")
    private Set<Experiment> experiments = new HashSet<Experiment>(0);
    private Set<ResearchGroup> researchGroups = new HashSet<ResearchGroup>(0);

    public Digitization() {
    }

    public Digitization(int digitizationId, float gain, String filter, float samplingRate, Set<Experiment> experiments) {
        this.digitizationId = digitizationId;
        this.gain = gain;
        this.filter = filter;
        this.samplingRate = samplingRate;
        this.experiments = experiments;
    }

    public int getDigitizationId() {
        return digitizationId;
    }

    public void setDigitizationId(int digitizationId) {
        this.digitizationId = digitizationId;
    }

    public float getGain() {
        return gain;
    }

    public void setGain(float gain) {
        this.gain = gain;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public float getSamplingRate() {
        return samplingRate;
    }

    public void setSamplingRate(float samplingRate) {
        this.samplingRate = samplingRate;
    }

    public Set<Experiment> getExperiments() {
        return experiments;
    }

    public void setExperiments(Set<Experiment> experiments) {
        this.experiments = experiments;
    }

    public Set<ResearchGroup> getResearchGroups() {
        return researchGroups;
    }

    public void setResearchGroups(Set<ResearchGroup> researchGroups) {
        this.researchGroups = researchGroups;
    }
}
