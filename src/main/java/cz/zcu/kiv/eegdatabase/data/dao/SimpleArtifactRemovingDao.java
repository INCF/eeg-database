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
 *   SimpleArtifactRemovingDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ArtifactRemoveMethod;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 24.4.12
 * Time: 14:17
 * To change this template use File | Settings | File Templates.
 */
public class SimpleArtifactRemovingDao extends SimpleGenericDao<ArtifactRemoveMethod, Integer>
        implements GenericListDaoWithDefault<ArtifactRemoveMethod> {

    public SimpleArtifactRemovingDao() {
        super(ArtifactRemoveMethod.class);
    }

    @Override
    public void createGroupRel(ArtifactRemoveMethod persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().add(researchGroup);
        researchGroup.getArtifactRemoveMethods().add(persistent);
    }

    @Override
    public List<ArtifactRemoveMethod> getItemsForList() {
        String hqlQuery = "from ArtifactRemoveMethod ar order by ar.title";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).list();
    }

    @Override
    public List<ArtifactRemoveMethod> getRecordsByGroup(int groupId) {
        String hqlQuery = "from ArtifactRemoveMethod ar inner join fetch ar.researchGroups as rg where rg.researchGroupId = :groupId";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("groupId", groupId).list();
    }

    @Override
    public boolean canDelete(int id) {
        String hqlQuery = "select ar.experiments from ArtifactRemoveMethod ar where ar.artifactRemoveMethodId = :id";
        List<ArtifactRemoveMethod> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("id", id).list();
        return (list.size() == 0);
    }

    @Override
    public boolean hasGroupRel(int id) {
        String hqlQuery = "from ArtifactRemoveMethod ar where ar.artifactRemoveMethodId = :id";
        List<ArtifactRemoveMethod> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("id", id).list();
        return list.get(0).getResearchGroups().size() > 0;
    }

    @Override
    public void deleteGroupRel(ArtifactRemoveMethod persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().remove(researchGroup);
        researchGroup.getArtifactRemoveMethods().remove(persistent);
    }

    @Override
    public void createDefaultRecord(ArtifactRemoveMethod persistent) {
        persistent.setDefaultNumber(1);
        create(persistent);
    }

    @Override
    public List<ArtifactRemoveMethod> getDefaultRecords() {
        String hqlQuery = "from ArtifactRemoveMethod ar where ar.defaultNumber = 1";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).list();
    }

    @Override
    public boolean isDefault(int id) {
        String hqlQuery = "select ar.defaultNumber from ArtifactRemoveMethod ar where ar.educationLevelId = :id";
        List<Integer> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("id", id).list();
        if (list.isEmpty()) {
            return false;
        }
        return (list.get(0) == 1);
    }
}
