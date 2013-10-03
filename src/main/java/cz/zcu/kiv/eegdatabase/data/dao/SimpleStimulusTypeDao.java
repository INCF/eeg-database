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
 *   SimpleStimulusTypeDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.StimulusType;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 25.4.12
 * Time: 14:04
 * To change this template use File | Settings | File Templates.
 */
public class SimpleStimulusTypeDao extends SimpleGenericDao<StimulusType, Integer>
        implements GenericListDaoWithDefault<StimulusType> {

    public SimpleStimulusTypeDao() {
        super(StimulusType.class);
    }

    @Override
    public void createGroupRel(StimulusType persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().add(researchGroup);
        researchGroup.getStimulusTypes().add(persistent);
    }

    @Override
    public List<StimulusType> getItemsForList() {
        String hqlQuery = "from StimulusType st order by st.description";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).list();
    }

    @Override
    public List<StimulusType> getRecordsByGroup(int groupId) {
        String hqlQuery = "from StimulusType st inner join fetch st.researchGroups as rg where rg.researchGroupId = :groupId";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("groupId", groupId).list();
    }

    @Override
    public boolean canDelete(int id) {
        String hqlQuery = "select st.stimulusRels from StimulusType st where st.stimulusTypeId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<StimulusType> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return (list.size() == 0);
    }

    @Override
    public boolean hasGroupRel(int id) {
        String hqlQuery = "from StimulusType st where st.stimulusTypeId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<StimulusType> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return list.get(0).getResearchGroups().size() > 0;
    }

    @Override
    public void deleteGroupRel(StimulusType persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().remove(researchGroup);
        researchGroup.getStimulusTypes().remove(persistent);
    }

    @Override
    public void createDefaultRecord(StimulusType persistent) {
        persistent.setDefaultNumber(1);
        create(persistent);
    }

    @Override
    public List<StimulusType> getDefaultRecords() {
        String hqlQuery = "from StimulusType st where st.defaultNumber=1";
        return getHibernateTemplate().find(hqlQuery);
    }

    @Override
    public boolean isDefault(int id) {
        String hqlQuery = "select st.defaultNumber from StimulusType st where st.stimulusTypeId = :id";
        List<Integer> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("id", id).list();
        if (list.isEmpty()) {
            return false;
        }
        return (list.get(0) == 1);
    }
}
