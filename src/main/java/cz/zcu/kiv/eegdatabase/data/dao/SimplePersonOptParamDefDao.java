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
 *   SimplePersonOptParamDefDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamDefGroupRel;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

import java.util.List;

public class SimplePersonOptParamDefDao extends SimpleGenericDao<PersonOptParamDef, Integer> implements PersonOptParamDefDao {
    public SimplePersonOptParamDefDao() {
        super(PersonOptParamDef.class);
    }

    public List<PersonOptParamDef> getItemsForList() {
        String hqlQuery = "from PersonOptParamDef i order by i.paramName";
        List<PersonOptParamDef> list = getHibernateTemplate().find(hqlQuery);
        return list;
    }

    public boolean canDelete(int id) {
        String hqlQuery = "select def.personOptParamVals from PersonOptParamDef def where def.personOptParamDefId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<PersonOptParamDef> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return (list.size() == 0);
    }

    public List<PersonOptParamDef> getRecordsByGroup(int groupId) {
        String hqlQuery = "from PersonOptParamDef h inner join fetch h.researchGroups as rg where rg.researchGroupId = :groupId";
        List<PersonOptParamDef> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("groupId", groupId).list();
        return list;
    }

    public void createDefaultRecord(PersonOptParamDef personOptParamDef) {
        personOptParamDef.setDefaultNumber(1);
        create(personOptParamDef);
    }

    public List<PersonOptParamDef> getDefaultRecords() {
        String hqlQuery = "from PersonOptParamDef h where h.defaultNumber = 1";
        List<PersonOptParamDef> list = getHibernateTemplate().find(hqlQuery);
        return list;
    }

    public boolean hasGroupRel(int id) {
        String hqlQuery = "from PersonOptParamDefGroupRel r where r.id.personOptParamDefId = :id";
        List<PersonOptParamDefGroupRel> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("id", id).list();
        return (list.size() > 0);
    }

    public void deleteGroupRel(PersonOptParamDefGroupRel personOptParamDefGroupRel) {
        getHibernateTemplate().delete(personOptParamDefGroupRel);
    }

    public PersonOptParamDefGroupRel getGroupRel(int personOptParamDefId, int researchGroupId) {
        String hqlQuery = "from PersonOptParamDefGroupRel r where r.id.personOptParamDefId = :personOptParamDefId and r.id.researchGroupId = :researchGroupId";
        List<PersonOptParamDefGroupRel> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery)
                .setParameter("personOptParamDefId", personOptParamDefId)
                .setParameter("researchGroupId", researchGroupId)
                .list();
        return list.get(0);
    }

    public void createGroupRel(PersonOptParamDefGroupRel personOptParamDefGroupRel) {
        personOptParamDefGroupRel.getPersonOptParamDef().setDefaultNumber(0);
        getHibernateTemplate().save(personOptParamDefGroupRel);
    }

    public void createGroupRel(PersonOptParamDef personOptParamDef, ResearchGroup researchGroup) {
        personOptParamDef.getResearchGroups().add(researchGroup);
        researchGroup.getPersonOptParamDefs().add(personOptParamDef);
    }

    public boolean isDefault(int id) {
        String hqlQuery = "select h.defaultNumber from PersonOptParamDef h where h.personOptParamDefId = :id";
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
