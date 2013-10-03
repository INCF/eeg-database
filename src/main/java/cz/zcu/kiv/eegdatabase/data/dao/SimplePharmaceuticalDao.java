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
 *   SimplePharmaceuticalDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Pharmaceutical;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import org.apache.cxf.service.invoker.SessionFactory;

import javax.mail.Session;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: stebjan
 * Date: 25.4.12
 * Time: 10:55
 * To change this template use File | Settings | File Templates.
 */
public class SimplePharmaceuticalDao extends SimpleGenericDao<Pharmaceutical, Integer>
        implements GenericListDao<Pharmaceutical> {

    public SimplePharmaceuticalDao() {
        super(Pharmaceutical.class);
    }

    @Override
    public void createGroupRel(Pharmaceutical persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().add(researchGroup);
        researchGroup.getPharmaceuticals().add(persistent);
    }

    @Override
    public List<Pharmaceutical> getItemsForList() {
        String hqlQuery = "from Pharmaceutical ph order by ph.title";
        return getHibernateTemplate().find(hqlQuery);
    }

    @Override
    public List<Pharmaceutical> getRecordsByGroup(int groupId) {
        String hqlQuery = "from Pharmaceutical ph inner join fetch ph.researchGroups as rg where rg.researchGroupId = :groupId";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("groupId", groupId).list();
    }

    @Override
    public boolean canDelete(int id) {
        String hqlQuery = "select ph.experiments from Pharmaceutical ph where ph.pharmaceuticalId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<Pharmaceutical> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return (list.size() == 0);
    }

    @Override
    public boolean hasGroupRel(int id) {
        String hqlQuery = "from Pharmaceutical ph where ph.pharmaceuticalId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<Pharmaceutical> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return list.get(0).getResearchGroups().size() > 0;
    }

    @Override
    public void deleteGroupRel(Pharmaceutical persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().remove(researchGroup);
        researchGroup.getPharmaceuticals().remove(persistent);
    }


    public boolean canSaveTitle(String title) {
        String hqlQuery = "from Pharmaceutical p where p.title = :title";
        List<Pharmaceutical> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery)
                .setParameter("title", title)
                .list();
        return (list.size() == 0);
    }
}
