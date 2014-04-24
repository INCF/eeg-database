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
 *   SimpleDiseaseDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Disease;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 24.4.12
 * Time: 14:46
 * To change this template use File | Settings | File Templates.
 */
public class SimpleDiseaseDao extends SimpleGenericDao<Disease, Integer>
        implements GenericListDao<Disease> {

    public SimpleDiseaseDao() {
        super(Disease.class);
    }

    @Override
    public void createGroupRel(Disease persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().add(researchGroup);
        researchGroup.getDiseases().add(persistent);
    }

    @Override
    public List<Disease> getItemsForList() {
        String hqlQuery = "from Disease dis order by dis.title";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).list();
    }

    @Override
    public List<Disease> getRecordsByGroup(int groupId) {
        String hqlQuery = "from Disease dis inner join fetch dis.researchGroups as rg where rg.researchGroupId = :groupId";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("groupId", groupId).list();

    }

    @Override
    public boolean canDelete(int id) {
        String hqlQuery = "select dis.experiments from Disease dis where dis.diseaseId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<Disease> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return (list.size() == 0);
    }

    @Override
    public boolean hasGroupRel(int id) {
        String hqlQuery = "from Disease dis where dis.diseaseId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<Disease> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return list.get(0).getResearchGroups().size() > 0;
    }

    @Override
    public void deleteGroupRel(Disease persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().remove(researchGroup);
        researchGroup.getDiseases().remove(persistent);
    }

    public boolean canSaveTitle(String title, int groupId, int diseaseId) {
        String hqlQuery = "from Disease ds inner join fetch ds.researchGroups " +
                "as rg where rg.researchGroupId =:groupId and ds.title =:title and ds.diseaseId != :diseaseId";
        List<Disease> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery)
                .setParameter("title", title)
                .setParameter("groupId", groupId)
                .setParameter("diseaseId", diseaseId)
                .list();
        return (list.size() == 0);
    }
}
