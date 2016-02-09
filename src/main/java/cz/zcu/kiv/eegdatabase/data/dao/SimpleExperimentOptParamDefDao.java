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
 *   SimpleExperimentOptParamDefDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDefGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

import java.util.List;

public class SimpleExperimentOptParamDefDao extends SimpleGenericDao<ExperimentOptParamDef, Integer> implements ExperimentOptParamDefDao {
    public SimpleExperimentOptParamDefDao() {
        super(ExperimentOptParamDef.class);
    }

    public List<ExperimentOptParamDef> getItemsForList() {
        String hqlQuery = "from ExperimentOptParamDef i order by i.paramName";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).list();
    }

    public boolean canDelete(int id) {
        String hqlQuery = "select def.experimentOptParamVals from ExperimentOptParamDef def where def.experimentOptParamDefId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<ExperimentOptParamDef> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("id", id).list();
        return (list.size() == 0);
    }

    public List<ExperimentOptParamDef> getRecordsByGroup(int groupId) {
        String hqlQuery = "from ExperimentOptParamDef h inner join fetch h.researchGroups as rg where rg.researchGroupId = :groupId";
        List<ExperimentOptParamDef> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("groupId", groupId).list();
        return list;
    }

    public void createDefaultRecord(ExperimentOptParamDef experimentOptParamDef) {
        experimentOptParamDef.setDefaultNumber(1);
        create(experimentOptParamDef);
    }

    public List<ExperimentOptParamDef> getDefaultRecords() {
        String hqlQuery = "from ExperimentOptParamDef h where h.defaultNumber = 1";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).list();
    }

    public boolean hasGroupRel(int id) {
        String hqlQuery = "from ExperimentOptParamDefGroupRel r where r.id.experimentOptParamDefId = :id";
        List<ExperimentOptParamDefGroupRel> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("id", id).list();
        return (list.size() > 0);
    }

    public void deleteGroupRel(ExperimentOptParamDefGroupRel experimentOptParamDefGroupRel) {
        getHibernateTemplate().delete(experimentOptParamDefGroupRel);
    }

    public ExperimentOptParamDefGroupRel getGroupRel(int experimentOptParamDefId, int researchGroupId) {
        String hqlQuery = "from ExperimentOptParamDefGroupRel r where r.id.experimentOptParamDefId = :experimentOptParamDefId and r.id.researchGroupId = :researchGroupId";
        List<ExperimentOptParamDefGroupRel> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery)
                .setParameter("experimentOptParamDefId", experimentOptParamDefId)
                .setParameter("researchGroupId", researchGroupId)
                .list();
        return list.get(0);
    }

    public void createGroupRel(ExperimentOptParamDefGroupRel experimentOptParamDefGroupRel) {
        experimentOptParamDefGroupRel.getExperimentOptParamDef().setDefaultNumber(0);
        getHibernateTemplate().save(experimentOptParamDefGroupRel);
    }

    public void createGroupRel(ExperimentOptParamDef experimentOptParamDef, ResearchGroup researchGroup) {
        experimentOptParamDef.getResearchGroups().add(researchGroup);
        researchGroup.getExperimentOptParamDefs().add(experimentOptParamDef);
    }

    public boolean isDefault(int id) {
        String hqlQuery = "select h.defaultNumber from ExperimentOptParamDef h where h.experimentOptParamDefId = :id";
        List<Integer> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("id", id).list();
        if (list.isEmpty()) {
            return false;
        }
        if (list.get(0) == 1) {
            return true;
        } else {
            return false;
        }
    }
}
