package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentLicence;
import cz.zcu.kiv.eegdatabase.data.pojo.License;

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
 * SimpleExperimentLicenceDao, 2015/03/23 22:42 administrator
 * <p/>
 * ********************************************************************************************************************
 */
public class SimpleExperimentLicenceDao extends SimpleGenericDao<ExperimentLicence,Integer> implements ExperimentLicenceDao {

    @Override
    public void remove(Experiment experiment, License license) {
        String hqlQuery = "delete from ExperimentLicence el where el.experiment = :ex and el.license = :lic";
        this.getSession().createQuery(hqlQuery).setParameter("ex",experiment).setParameter("lic",license).executeUpdate();
    }

}

