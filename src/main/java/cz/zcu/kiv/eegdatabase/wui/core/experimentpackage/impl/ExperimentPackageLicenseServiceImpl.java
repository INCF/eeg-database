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
 *   ExperimentPackageLicenseServiceImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.impl;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import cz.zcu.kiv.eegdatabase.data.dao.ExperimentPackageLicenseDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackageLicense;
import cz.zcu.kiv.eegdatabase.wui.core.GenericServiceImpl;
import cz.zcu.kiv.eegdatabase.wui.core.experimentpackage.ExperimentPackageLicenseService;

/**
 *
 * @author Jakub Danek
 */
public class ExperimentPackageLicenseServiceImpl extends GenericServiceImpl<ExperimentPackageLicense, Integer> implements ExperimentPackageLicenseService{

    private ExperimentPackageLicenseDao dao;

    public ExperimentPackageLicenseServiceImpl(ExperimentPackageLicenseDao dao) {
        super(dao);
        setDao(dao);
    }

    public void setDao(ExperimentPackageLicenseDao dao) {
        this.dao = dao;
    }

    
    @Override
    @Transactional(readOnly = true)
    public List<ExperimentPackageLicense> getExperimentPackageLicensesForPackage(ExperimentPackage pckg) {
        return dao.readByParameter("experimentPackage.experimentPackageId", pckg.getExperimentPackageId());
    }

    
}
