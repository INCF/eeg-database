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
 *   SimpleAuthorizationManager.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.Util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author Jindra
 */
public class SimpleAuthorizationManager extends HibernateDaoSupport implements AuthorizationManager {

    private PersonDao personDao;

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    public boolean personAbleToWriteIntoGroup(int researchGroupId) {
        String hqlQuery = "select p.personId from Person as p "
                + "left join p.researchGroupMemberships m "
                + "where p.personId = :personId "
                + "and m.researchGroup.researchGroupId = :researchGroupId "
                + "and m.authority in (:authoritiesList)";
        List authorities = new ArrayList();
        authorities.add(Util.GROUP_EXPERIMENTER);
        authorities.add(Util.GROUP_ADMIN);
        String[] paramNames = {"personId", "researchGroupId", "authoritiesList"};
        Object[] values = {getLoggedPersonId(), researchGroupId, authorities};
        List list = getHibernateTemplate().findByNamedParam(hqlQuery, paramNames, values);
        return (list.size() == 1);
    }

    private int getLoggedPersonId() {
        return personDao.getLoggedPerson().getPersonId();
    }

    public boolean isAdmin() {
        Person p = personDao.getLoggedPerson();
        return (p.getAuthority().equals(Util.ROLE_ADMIN));

    }

    public boolean userIsExperimenter() {
        String hqlQuery = "select p.personId "
                + "from Person as p "
                + "left join p.researchGroupMemberships m "
                + "where p.personId = :personId "
                + "and m.authority in (:authoritiesList)";

        List authorities = new ArrayList();
        authorities.add(Util.GROUP_EXPERIMENTER);
        authorities.add(Util.GROUP_ADMIN);
        String[] paramNames = {"personId", "authoritiesList"};
        Object[] values = {getLoggedPersonId(), authorities};

        List list = getHibernateTemplate().findByNamedParam(hqlQuery, paramNames, values);

        return (list.size() > 0);
    }

    public boolean userIsGroupAdmin() {
        String hqlQuery = "select p.personId "
                + "from Person as p "
                + "left join p.researchGroupMemberships m "
                + "where p.personId = :personId "
                + "and m.authority in (:authoritiesList)";

        List authorities = new ArrayList();
        authorities.add(Util.GROUP_ADMIN);
        String[] paramNames = {"personId", "authoritiesList"};
        Object[] values = {getLoggedPersonId(), authorities};

        List list = getHibernateTemplate().findByNamedParam(hqlQuery, paramNames, values);

        return (list.size() > 0);
    }

    public boolean userIsOwnerOrCoexperimenter(int experimentId) {
        String hqlQuery = "select e.experimentId "
                + "from Experiment e "
                + "left join e.persons p "
                + "where e.experimentId = :experimentId "
                + "and ("
                + "e.personByOwnerId.personId = :personId "
                + "or p.personId = :personId "
                + ")";

        String[] paramNames = {"experimentId", "personId"};
        Object[] values = {experimentId, getLoggedPersonId()};
        List list = getHibernateTemplate().findByNamedParam(hqlQuery, paramNames, values);

        return (list.size() > 0);
    }

    /**
     * @param personId
     * @return <code>true</code> if the logged person and person specified by
     *         personId are members of the same group
     */
    public boolean userCanViewPersonDetails(int personId) {
        List<Integer> list = getGroupsWhereBothPersons(getLoggedPersonId(), personId);
        return (list.size() > 0);
    }

    /**
     * @return true, if the logged user is admin or the edited person
     */
    @Override
    public boolean userCanEditPerson(int personId) {
        return isAdmin() || personDao.getLoggedPerson().getPersonId() == personId;
    }

    public boolean userIsOwnerOfScenario(int scenarioId) {
        String hqlQuery = "select s.scenarioId "
                + "from Scenario s "
                + "where s.scenarioId = :scenarioId "
                + "and s.person.personId = :personId ";

        String[] paramNames = {"scenarioId", "personId"};
        Object[] values = {scenarioId, getLoggedPersonId()};
        List list = getHibernateTemplate().findByNamedParam(hqlQuery, paramNames, values);

        return (list.size() > 0);
    }

    private List<Integer> getGroupsWhereBothPersons(int personId1, int personId2) {
        // Quite complicated query - there are two joins into the same table, that
        // is used for fetching one relation of logged person and for fetching
        // another relation of person specified by parameter.
        String hqlQuery = "select rg.researchGroupId "
                + "from ResearchGroup rg "
                + "left join rg.researchGroupMemberships rgmLogged "
                + "left join rg.researchGroupMemberships rgmPerson "
                + "where rgmLogged.person.personId = :loggedUserId "
                + "and rgmPerson.person.personId = :personId ";

        String[] paramNames = {"loggedUserId", "personId"};
        Object[] values = {personId1, personId2};

        List<Integer> list = getHibernateTemplate().findByNamedParam(hqlQuery, paramNames, values);

        return list;
    }

    public boolean userIsOwnerOrCoexpOfCorrespExperiment(int dataFileId) {
        String hqlQuery = "select df.experiment.experimentId "
                + "from DataFile df "
                + "where df.dataFileId = :dataFileId";

        List list = getHibernateTemplate().findByNamedParam(hqlQuery, "dataFileId", dataFileId);
        if (list.size() <= 0) {
            return false;
        }

        int experimentId = (Integer) list.get(0);
        return userIsOwnerOrCoexperimenter(experimentId);
    }

    @Override
    public boolean userIsMemberInGroup(int groupId){
        String hqlQuery = "select rg.researchGroupId " +
                "from ResearchGroup rg " +
                "left join rg.researchGroupMemberships rgm " +
                "where rg.researchGroupId = :groupId " +
                "and rgm.person.personId = :personId ";
        String[] paramNames = {"groupId", "personId"};
        Object[] values = {groupId, getLoggedPersonId()};
        List list = getHibernateTemplate().findByNamedParam(hqlQuery, paramNames, values);
        return !list.isEmpty();
    }

    public boolean userIsExperimenterInGroup(int groupId) {
        String hqlQuery = "select rg.researchGroupId " +
                "from ResearchGroup rg " +
                "left join rg.researchGroupMemberships rgm " +
                "where rg.researchGroupId = :groupId " +
                "and rgm.person.personId = :personId " +
                "and rgm.authority in(:authorities)";

        List authorities = new ArrayList();
        authorities.add(Util.GROUP_EXPERIMENTER);
        authorities.add(Util.GROUP_ADMIN);
        String[] paramNames = {"groupId", "personId", "authorities"};
        Object[] values = {groupId, getLoggedPersonId(), authorities};
        List list = getHibernateTemplate().findByNamedParam(hqlQuery, paramNames, values);

        return (list.size() > 0);
    }

    public boolean userIsAdminInGroup(int groupId) {
        String hqlQuery = "select rg.researchGroupId " +
                "from ResearchGroup rg " +
                "left join rg.researchGroupMemberships rgm " +
                "where rg.researchGroupId = :groupId " +
                "and rgm.person.personId = :personId " +
                "and rgm.authority = :authority";

        String[] paramNames = {"groupId", "personId", "authority"};
        Object[] values = {groupId, getLoggedPersonId(), Util.GROUP_ADMIN};
        List list = getHibernateTemplate().findByNamedParam(hqlQuery, paramNames, values);

        return (list.size() > 0);
    }
    
    @Override
    public boolean isAuthorizedToRequestGroupRole() {
        Person loggedUser = personDao.getLoggedPerson();
        
        return (loggedUser.getAuthority().equals(Util.ROLE_ADMIN)
                || loggedUser.getAuthority().equals(Util.ROLE_USER)
                || !loggedUser.getResearchGroupMemberships().isEmpty());
    }
}
