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
 *   SimpleStimulusDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Stimulus;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Matheo
 * Date: 18.4.13
 * Time: 15:27
 * To change this template use File | Settings | File Templates.
 */
public class SimpleStimulusDao extends SimpleGenericDao<Stimulus, Integer>
        implements StimulusDao {

    public SimpleStimulusDao() {
        super(Stimulus.class);
    }

    @Override
    public boolean canSaveDescription(String description) {
                String hqlQuery = "from Stimulus s where s.description = :description";
        List<Stimulus> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery)
                .setParameter("description", description)
                .list();
        return (list.size()==0);
    }
}
