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
 *   SimpleArtifactDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Artifact;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 24.4.12
 * Time: 13:46
 * To change this template use File | Settings | File Templates.
 */
public class SimpleArtifactDao extends SimpleGenericDao<Artifact, Integer>
        implements GenericListDao<Artifact> {

    public SimpleArtifactDao() {
        super(Artifact.class);
    }

    @Override
    public void createGroupRel(Artifact persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().add(researchGroup);
        researchGroup.getArtifacts().add(persistent);
    }

    @Override
    public List<Artifact> getItemsForList() {
        String hqlQuery = "from Artifact ar order by ar.compensation";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).list();
    }

    @Override
    public List<Artifact> getRecordsByGroup(int groupId) {
        String hqlQuery = "from Artifact ar left join fetch ar.researchGroups rg where rg.researchGroupId = :groupId";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("groupId", groupId).list();
    }

    @Override
    public boolean canDelete(int id) {
        String hqlQuery = "select ar.experiments from Artifact ar where ar.artifactId = :id";
        List<Artifact> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("id", id).list();
        return (list.size() == 0);
    }

    @Override
    public boolean hasGroupRel(int id) {
        String hqlQuery = "from Artifact ar where ar.artifactId = :id";
        List<Artifact> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("id", id).list();
        return list.get(0).getResearchGroups().size() > 0;
    }

    @Override
    public void deleteGroupRel(Artifact persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().remove(researchGroup);
        researchGroup.getArtifacts().remove(persistent);
    }
}
