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
 *   ElectrodeConf.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.pojo;


import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 28.2.12
 * Time: 13:47
 * To change this template use File | Settings | File Templates.
 */
@Entity
@javax.persistence.Table(name="ELECTRODE_CONF")
public class ElectrodeConf implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ELECTRODE_CONF_ID")
    private int electrodeConfId;
    @Column(name = "IMPEDANCE")
    private int impedance;
    @ManyToOne
    @JoinColumn(name = "ELECTRODE_SYSTEM_ID")
    private ElectrodeSystem electrodeSystem;
    @ManyToOne
    @JoinColumn(name = "DESC_IMG_ID")
    private DataFile descImg;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<ElectrodeLocation> electrodeLocations = new HashSet<ElectrodeLocation>(0);
    @OneToMany(mappedBy = "electrodeConf")
    private Set<Experiment> experiments = new HashSet<Experiment>(0);

    public ElectrodeConf() {
    }

    public int getElectrodeConfId() {
        return electrodeConfId;
    }

    public void setElectrodeConfId(int electrodeConfId) {
        this.electrodeConfId = electrodeConfId;
    }

    public int getImpedance() {
        return impedance;
    }

    public void setImpedance(int impedance) {
        this.impedance = impedance;
    }

    public DataFile getDescImg() {
        return descImg;
    }

    public void setDescImg(DataFile descImg) {
        this.descImg = descImg;
    }

    public Set<Experiment> getExperiments() {
        return experiments;
    }

    public void setExperiments(Set<Experiment> experiments) {
        this.experiments = experiments;
    }

    public ElectrodeSystem getElectrodeSystem() {
        return electrodeSystem;
    }

    public void setElectrodeSystem(ElectrodeSystem electrodeSystem) {
        this.electrodeSystem = electrodeSystem;
    }

    public Set<ElectrodeLocation> getElectrodeLocations() {
        return electrodeLocations;
    }

    public void setElectrodeLocations(Set<ElectrodeLocation> electrodeLocations) {
        this.electrodeLocations = electrodeLocations;
    }
}
