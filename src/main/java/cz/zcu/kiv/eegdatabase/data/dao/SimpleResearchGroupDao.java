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
 *   SimpleResearchGroupDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupAccountInfo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of DAO for accessing ResearchGroup entities. This class will use
 * only one specific couple of classes, so it doesn't need to be generic.
 *
 * @author Jindrich Pergler
 */
public class SimpleResearchGroupDao
        extends SimpleGenericDao<ResearchGroup, Integer>
        implements ResearchGroupDao {

    public SimpleResearchGroupDao() {
        super(ResearchGroup.class);
    }

    /**
     * Returns list of research groups, which the person is member of.
     *
     * @param person Object of class Person
     * @return The list with groups
     */
    public List<ResearchGroup> getResearchGroupsWhereMember(Person person) {
        String hqlQuery = "from ResearchGroup researchGroup "
                + "where researchGroupId in (select rgm.id.researchGroupId from ResearchGroupMembership rgm where id.personId = :personId) "
                + "order by researchGroup.title";
        return getHibernateTemplate().findByNamedParam(hqlQuery, "personId", person.getPersonId());
    }

    public List<ResearchGroup> getResearchGroupsWhereMember(Person person, int limit) {
        String hqlQuery = "from ResearchGroup researchGroup "
                + "where researchGroupId in (select rgm.id.researchGroupId from ResearchGroupMembership rgm where id.personId = :personId) "
                + "order by researchGroup.title";

        getHibernateTemplate().setMaxResults(limit);
        List<ResearchGroup> list = getHibernateTemplate().findByNamedParam(hqlQuery, "personId", person.getPersonId());
        getHibernateTemplate().setMaxResults(0);
        return list;
    }

    /**
     * Returns list of research groups, which the person owns.
     *
     * @param person Owner of the searched groups
     * @return The list with groups
     */
    public List<ResearchGroup> getResearchGroupsWhereOwner(Person person) {
        String hqlQuery = "from ResearchGroup researchGroup "
                + "where researchGroup.person.personId = :ownerId";
        return getHibernateTemplate().findByNamedParam(hqlQuery, "ownerId", person.getPersonId());
    }

    public List<ResearchGroup> getResearchGroupsWhereAbleToWriteInto(Person person) {
        String hqlQuery = "from ResearchGroup researchGroup "
                + "left join fetch researchGroup.researchGroupMemberships as membership "
                + "where membership.person.personId = :personId "
                + "and (membership.authority = :experimenter or membership.authority = :admin) "
                + "order by researchGroup.title";

        String[] names = {"personId", "experimenter", "admin"};
        Object[] params = {person.getPersonId(), Util.GROUP_EXPERIMENTER, Util.GROUP_ADMIN};
        List<ResearchGroup> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, params);
        return list;
    }

    public List<ResearchGroupAccountInfo> getGroupDataForAccountOverview(Person person) {
        String hqlQuery = "select new cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupAccountInfo("
                + "g.researchGroupId as groupId, "
                + "g.title as title, "
                + "m.authority as authority) "
                + "from ResearchGroup g "
                + "left join g.researchGroupMemberships m "
                + "where m.person.personId = :personId";
        List<ResearchGroupAccountInfo> list = getHibernateTemplate().findByNamedParam(hqlQuery, "personId", person.getPersonId());
        return list;
    }

    public List getListOfGroupMembers(int groupId) {
        String hqlQuery = "select new map("
                + "p.personId as personId, "
                + "p.givenname as givenname, "
                + "p.surname as surname, "
                + "p.username as username, "
                + "rgm.authority as authority"
                + ")"
                + "from Person p "
                + "left join p.researchGroupMemberships rgm "
                + "where rgm.researchGroup.researchGroupId = :groupId "
                + "order by surname, givenname";
        List list = getHibernateTemplate().findByNamedParam(hqlQuery, "groupId", groupId);
        return list;
    }

    public List<ResearchGroup> getResearchGroupsWhereUserIsGroupAdmin(Person person) {
        String hqlQuery = "from ResearchGroup researchGroup "
                + "left join fetch researchGroup.researchGroupMemberships as membership "
                + "where membership.person.personId = :personId "
                + "and (membership.authority = :admin) "
                + "order by researchGroup.title";

        String[] names = {"personId", "admin"};
        Object[] params = {person.getPersonId(), Util.GROUP_ADMIN};
        List<ResearchGroup> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, params);
        return list;
    }

    public String getResearchGroupTitle(int groupId) {
        String hqlQuery = "select title from ResearchGroup r where r.researchGroupId = :groupId";
        List<String> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).setParameter("groupId", groupId).list();
        if (!list.isEmpty()) {
            return list.get(0);
        } else {
            return "";
        }
    }


    public boolean canSaveTitle(String title, int id) {
        String hqlQuery = "from ResearchGroup g where g.title = :title and g.researchGroupId != :id";
        String[] names = {"title", "id"};
        Object[] values = {title, id};
        List<ResearchGroup> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
        return (list.size() == 0);
    }

    @Override
    public int getCountForList() {
        String query = "select count(g) from ResearchGroup g";
        return ((Long) getSessionFactory().getCurrentSession().createQuery(query).uniqueResult()).intValue();
    }

    @Override
    public List getGroupsForList(int start, int limit) {
        String query = "from ResearchGroup g order by g.title asc";
        return getSessionFactory().getCurrentSession().createQuery(query).setFirstResult(start).setMaxResults(limit).list();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ResearchGroup> getAllRecordsFull() {
        return super.getAllRecordsFull();
    }
}
