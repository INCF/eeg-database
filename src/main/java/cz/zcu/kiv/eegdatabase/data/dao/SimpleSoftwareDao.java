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
 *   SimpleSoftwareDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import java.util.List;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Software;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 25.4.12
 * Time: 14:13
 * To change this template use File | Settings | File Templates.
 */
public class SimpleSoftwareDao extends SimpleGenericDao<Software, Integer>
        implements GenericListDaoWithDefault<Software> {

    public SimpleSoftwareDao() {
        super(Software.class);
    }

    @Override
    public void createGroupRel(Software persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().add(researchGroup);
    }

    @Override
    public List<Software> getItemsForList() {
        String hqlQuery = "from Software st order by st.description";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).list();
    }

    @Override
    public List<Software> getRecordsByGroup(int groupId) {
        String hqlQuery = "from Software st inner join fetch st.researchGroups as rg where rg.researchGroupId = :groupId";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("groupId", groupId).list();
    }

    @Override
    public boolean canDelete(int id) {
        String hqlQuery = "select st.experiments from Software st where st.softwareId = :id";
        List<Software> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("id", id).list();
        return (list.size() == 0);
    }

    @Override
    public boolean hasGroupRel(int id) {
        String hqlQuery = "from Software st where st.softwareId = :id";
        List<Software> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("id", id).list();
        return list.get(0).getResearchGroups().size() > 0;
    }

    @Override
    public void deleteGroupRel(Software persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().remove(researchGroup);
        researchGroup.getSoftwares().remove(persistent);
    }

    @Override
    public void createDefaultRecord(Software persistent) {
        persistent.setDefaultNumber(1);
        create(persistent);
    }

    @Override
    public List<Software> getDefaultRecords() {
        String hqlQuery = "from Software st where st.defaultNumber = 1";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).list();
    }

    @Override
    public boolean isDefault(int id) {
        String hqlQuery = "select st.defaultNumber from Software st where st.softwareId = :id";
        List<Integer> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("id", id).list();
        if (list.isEmpty()) {
            return false;
        }
        return (list.get(0) == 1);
    }

    public boolean canSaveDefaultTitle(String title, int swId) {
        String hqlQuery = "from Software s where s.title = :title and s.defaultNumber = 1 and s.softwareId <> :swId";
        List<Software> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery)
                .setParameter("title", title)
                .setParameter("swId", swId)
                .list();
        return (list.size() == 0);
    }
    
    public boolean canSaveTitle(String title, int groupId, int softwareId) {
        String hqlQuery = "from Software sw inner join fetch sw.researchGroups " +
                                "as rg where rg.researchGroupId =:groupId and sw.title =:title and sw.softwareId != :softwareId";
        List<Software> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery)
                .setParameter("title", title)
                .setParameter("groupId", groupId)
                .setParameter("softwareId", softwareId)
                .list();
        return (list.size() == 0);
    }
}
