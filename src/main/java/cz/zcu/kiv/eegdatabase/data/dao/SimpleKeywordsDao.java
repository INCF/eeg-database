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
 *   SimpleKeywordsDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.Keywords;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import java.io.Serializable;
import java.util.List;
import org.springframework.dao.support.DataAccessUtils;

/**
 * Simple Dao for class Keywords
 * Class is determined only for Keywords.
 * @author Ladislav Janák
 */
public class SimpleKeywordsDao extends SimpleGenericDao<Keywords, Integer> {
    
    public SimpleKeywordsDao() {
        super(Keywords.class);
    }
    
    /**
     * Selects keyword from DB for specific research group
     * @param groupId ID of the research group
     * @return keywords for researchgroup
     */
    public String getKeywords(int groupId){
        String hqlQuery = "select k.keywordsText " +"from Keywords k " + "left join k.researchGroup rg " + "where rg.researchGroupId = :groupId and k.keywordsText is not null" ;     
        List<String> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("groupId", groupId).list();
        if(!list.isEmpty()){
            return list.get(0);
        }else{
            return "No keywords defined!";
        }

    }

    public List<Integer> getKeywordsFromPackage(ExperimentPackage pck) {
        String hqlQuery = "select k.keywordsId from Keywords k left join k.experimentPackage pck where pck.experimentPackageId = :packageId" ;
        return  getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("packageId", pck.getExperimentPackageId()).list();
}
    
    /**
     * Gets ID of specific record in Keywords table by research group ID
     * @param groupId ID of the research group
     * @return 
     */
    public int getID(int groupId){
        String hqlQuery = "select k.keywordsId " +"from Keywords k " + "left join k.researchGroup rg " + "where rg.researchGroupId = :groupId" ;     
        List<Integer> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("groupId", groupId).list();
        if(!list.isEmpty()){
            return list.get(0);
        }else{
            return -1;
        }
    } 
}

