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
 *   SimpleProjectTypeDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ProjectType;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 25.4.12
 * Time: 12:49
 * To change this template use File | Settings | File Templates.
 */
public class SimpleProjectTypeDao extends SimpleGenericDao<ProjectType, Integer>
        implements GenericListDao<ProjectType> {

    public SimpleProjectTypeDao() {
        super(ProjectType.class);
    }

    @Override
    public void createGroupRel(ProjectType persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().add(researchGroup);
        researchGroup.getProjectTypes().add(persistent);
    }

    @Override
    public List<ProjectType> getItemsForList() {
        String hqlQuery = "from ProjectType pr order by pr.title";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).list();
    }

    @Override
    public List<ProjectType> getRecordsByGroup(int groupId) {
        String hqlQuery = "from ProjectType pr inner join fetch pr.researchGroups as rg where rg.researchGroupId = :groupId";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("groupId", groupId).list();
    }

    @Override
    public boolean canDelete(int id) {
        String hqlQuery = "select pr.experiments from ProjectType pr where pr.projectTypeId = :id";
        List<ProjectType> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("id", id).list();
        return (list.size() == 0);
    }

    @Override
    public boolean hasGroupRel(int id) {
        String hqlQuery = "from ProjectType pr where pr.projectTypeId = :id";
        List<ProjectType> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("id", id).list();
        return list.get(0).getResearchGroups().size() > 0;
    }

    @Override
    public void deleteGroupRel(ProjectType persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().remove(researchGroup);
        researchGroup.getProjectTypes().remove(persistent);
    }

    public boolean canSaveTitle(String title, int groupId, int projectId) {
        String hqlQuery = "from ProjectType pr inner join fetch pr.researchGroups " +
                "as rg where rg.researchGroupId =:groupId and pr.title =:title and pr.projectTypeId != :projectId ";
        List<ProjectType> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery)
                .setParameter("title", title)
                .setParameter("groupId", groupId)
                .setParameter("projectId", projectId)
                .list();
        return (list.size() == 0);
    }

}
