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
 *   SimpleEducationLevelDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

import java.util.List;

/**
 * This class extends powers class SimpleGenericDao.
 * Class is determined only for EducationLevel.
 *
 * @author Jiri Novotny
 */
public class SimpleEducationLevelDao
        extends SimpleGenericDao<EducationLevel, Integer> implements EducationLevelDao {

    public SimpleEducationLevelDao() {
        super(EducationLevel.class);
    }

    /**
     * Finds all education levels with the specified title
     *
     * @param title - title property value
     * @return List of EducationLevel entities with searched title
     */
    public List<EducationLevel> getEducationLevels(String title) {
        String HQLselect = "from EducationLevel level where level.title = :title";
        return getHibernateTemplate().findByNamedParam(HQLselect, "title", title);
    }

    @Override
    public void createGroupRel(EducationLevel persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().add(researchGroup);
        researchGroup.getEducationLevels().add(persistent);
    }

    @Override
    public List<EducationLevel> getItemsForList() {
        String hqlQuery = "from EducationLevel ed order by ed.title";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).list();
    }

    @Override
    public List<EducationLevel> getRecordsByGroup(int groupId) {
        String hqlQuery = "from EducationLevel ed inner join fetch ed.researchGroups as rg where rg.researchGroupId = :groupId";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("groupId", groupId).list();
    }

    @Override
    public boolean canDelete(int id) {
        String hqlQuery = "select ed.persons from EducationLevel ed where ed.educationLevelId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<EducationLevel> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return (list.size() == 0);
    }

    @Override
    public boolean hasGroupRel(int id) {
        String hqlQuery = "from EducationLevel ed where ed.educationLevelId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<EducationLevel> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return list.get(0).getResearchGroups().size() > 0;
    }

    @Override
    public void deleteGroupRel(EducationLevel persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().remove(researchGroup);
        researchGroup.getEducationLevels().remove(persistent);
    }

    @Override
    public void createDefaultRecord(EducationLevel educationLevel) {
        educationLevel.setDefaultNumber(1);
        create(educationLevel);
    }

    @Override
    public List<EducationLevel> getDefaultRecords() {
        String hqlQuery = "from EducationLevel ed where ed.defaultNumber = 1";
        return getHibernateTemplate().find(hqlQuery);
    }

    @Override
    public boolean isDefault(int id) {
        String hqlQuery = "select ed.defaultNumber from EducationLevel ed where ed.educationLevelId = :id";
        List<Integer> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("id", id).list();
        if (list.isEmpty()) {
            return false;
        }
        return (list.get(0) == 1);
    }
}
