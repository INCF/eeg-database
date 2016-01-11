package cz.zcu.kiv.eegdatabase.data.pojo;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * ********************************************************************************************************************
 * <p/>
 * This file is part of the eegdatabase project
 * <p/>
 * ==========================================
 * <p/>
 * Copyright (C) 2015 by University of West Bohemia (http://www.zcu.cz/en/)
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
 * ExperimentLicence, 2015/03/22 17:49 administrator
 * <p/>
 * ********************************************************************************************************************
 */

@Entity
@Table(name = "EXPERIMENT_LICENSE")
public class ExperimentLicence implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "EXPERIMENT_LICENSE_ID")
    private int experimentLicenseId;

    @ManyToOne
    @JoinColumn(name = "EXPERIMENT")
    private Experiment experiment;

    @ManyToOne
    @JoinColumn(name = "LICENSE")
    private License license;
    
    @Column(name = "PRICE", precision = 19, scale = 2)
    private BigDecimal price;

    public int getExperimentLicenseId() {
        return experimentLicenseId;
    }

    public License getLicense() {
        return license;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setExperimentLicenseId(int experimentLicenseId) {
        this.experimentLicenseId = experimentLicenseId;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    public void setLicense(License license) {
        this.license = license;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
}



