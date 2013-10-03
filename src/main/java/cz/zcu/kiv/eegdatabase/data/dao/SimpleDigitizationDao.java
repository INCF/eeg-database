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
 *   SimpleDigitizationDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Digitization;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Honza
 * Date: 24.2.12
 * Time: 14:33
 * To change this template use File | Settings | File Templates.
 */
public class SimpleDigitizationDao
        extends SimpleGenericDao<Digitization, Integer> implements DigitizationDao {

    public SimpleDigitizationDao() {
        super(Digitization.class);
    }

    @Override
    public Digitization getDigitizationByParams(float samplingRate, float gain, String filter) {
        String[] paramNames = {"samplingRate", "gain", "filter"};
        Object[] values = {samplingRate, gain, filter};
        String HQLQuery = "from Digitization d where d.samplingRate = :samplingRate and d.gain = :gain and d.filter = :filter";
        List<Digitization> list = getHibernateTemplate().findByNamedParam(HQLQuery, paramNames, values);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public void createGroupRel(Digitization persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().add(researchGroup);
        researchGroup.getDigitizations().add(persistent);
    }

    @Override
    public List<Digitization> getItemsForList() {
        String hqlQuery = "from Digitization dig order by dig.samplingRate";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).list();
    }

    @Override
    public List<Digitization> getRecordsByGroup(int groupId) {
        String hqlQuery = "from Digitization dig inner join fetch dig.researchGroups as rg where rg.researchGroupId = :groupId";
        return getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("groupId", groupId).list();

    }

    @Override
    public boolean canDelete(int id) {
        String hqlQuery = "select dig.experiments from Digitization dig where dig.digitizationId = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<Digitization> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return (list.size() == 0);
    }

    @Override
    public boolean hasGroupRel(int id) {
        String hqlQuery = "from Digitization dig where dig.d = :id";
        String[] names = {"id"};
        Object[] values = {id};
        List<Digitization> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return list.get(0).getResearchGroups().size() > 0;
    }

    @Override
    public void deleteGroupRel(Digitization persistent, ResearchGroup researchGroup) {
        persistent.getResearchGroups().remove(researchGroup);
        researchGroup.getDigitizations().remove(persistent);
    }
}
