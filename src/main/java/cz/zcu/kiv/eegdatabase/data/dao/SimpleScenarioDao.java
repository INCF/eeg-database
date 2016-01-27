/**
 * *****************************************************************************
 * This file is part of the EEG-database project
 *
 * ==========================================
 *
 * Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 *  ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 *  ***********************************************************************************************************************
 *
 * SimpleScenarioDao.java, 2013/10/02 00:01 Jakub Rinkes
 * ****************************************************************************
 */
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.logic.search.SearchRequest;

import java.sql.Blob;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * This class extends powers class SimpleGenericDao. Class is determined only
 * for Person.
 *
 * @author Pavel Bořík, A06208
 */
public class SimpleScenarioDao extends SimpleGenericDao<Scenario, Integer> implements ScenarioDao {

	public SimpleScenarioDao() {
		super(Scenario.class);
	}

	public List<Scenario> getScenariosWhereOwner(Person owner) {
		String hqlQuery = "from Scenario s where s.person.personId = :ownerId";
		List<Scenario> list = getHibernateTemplate().findByNamedParam(hqlQuery, "ownerId", owner.getPersonId());
		return list;
	}

	public List<Scenario> getScenariosWhereOwner(Person person, int limit) {
		getHibernateTemplate().setMaxResults(limit);
		List<Scenario> list = getScenariosWhereOwner(person);
		getHibernateTemplate().setMaxResults(0);
		return list;
	}

	public List<Scenario> getScenarioSearchResults(List<SearchRequest> requests, int personId) throws NumberFormatException {

		boolean ignoreChoice = false;
		String hqlQuery = "from Scenario where (";
		for (SearchRequest request : requests) {
			if (request.getCondition().equals("")) {
				if (request.getChoice().equals("")) {
					ignoreChoice = true;
				}
				continue;
			}
			if (!ignoreChoice) {
				hqlQuery += request.getChoice();

			}
			if (request.getSource().endsWith("ScenarioLength")) {
				if (Integer.parseInt(request.getCondition()) < 0) {
					throw new RuntimeException("Invalid length value. It has to be non-negative number");
				}
				hqlQuery += "scenarioLength" + getCondition(request.getSource()) + request.getCondition();
			} else if (request.getSource().equals("person")) {
				hqlQuery += getAuthor(request.getCondition());
			} else {
				hqlQuery += "lower(" + request.getSource() + ") like lower('%" + request.getCondition() + "%')";
			}
			ignoreChoice = false;
		}
		List<Scenario> results;

		hqlQuery += ")";
		try {
			results = getHibernateTemplate().find(hqlQuery);
		} catch (Exception e) {
			return new ArrayList<Scenario>();
		}
		return results;
	}

	public boolean canSaveTitle(String title, int id) {
		String hqlQuery = "from Scenario s where s.title = :title and s.scenarioId != :id";
		String[] names = {"title", "id"};
		Object[] values = {title, id};
		List<Scenario> list = getHibernateTemplate().findByNamedParam(hqlQuery, names, values);
		return (list.size() == 0);
	}

	@Override
	public List<Scenario> getScenariosForList(Person person, int start, int count) {
		if (person.getAuthority().equals("ROLE_ADMIN")) {
			String query = "from Scenario s "
					+ "order by s.scenarioName asc";
			return getSessionFactory().getCurrentSession().createQuery(query).setFirstResult(start).setMaxResults(count).list();
		} else {
			String query = "from Scenario s "
					+ "where "
					+ "s.privateScenario = false "
					+ "or s.researchGroup.researchGroupId in "
					+ "(select m.researchGroup.researchGroupId from ResearchGroupMembership m where m.person.personId = :personId) "
					+ "order by s.scenarioName asc";
			return getSessionFactory().getCurrentSession().createQuery(query).setParameter("personId", person.getPersonId()).setFirstResult(start).setMaxResults(count).list();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Scenario> getAvailableScenarios(Person person) {
		if (person.getAuthority().equals("ROLE_ADMIN")) {
			String query = "from Scenario s "
					+ "order by s.scenarioName asc";
			return getSessionFactory().getCurrentSession().createQuery(query).list();
		} else {
			String query = "from Scenario s "
					+ "where "
					+ "s.privateScenario = false "
					+ "or s.researchGroup.researchGroupId in "
					+ "(select m.researchGroup.researchGroupId from ResearchGroupMembership m where m.person.personId = :personId) "
					+ "order by s.scenarioName asc";
			return getSessionFactory().getCurrentSession().createQuery(query).setParameter("personId", person.getPersonId()).list();
		}
	}

	@Override
	public int getScenarioCountForList(Person person) {
		if (person.getAuthority().equals("ROLE_ADMIN")) {
			String query = "select count(s) from Scenario s ";
			return ((Long) getSessionFactory().getCurrentSession().createQuery(query).uniqueResult()).intValue();
		} else {
			String query = " select count(s) from Scenario s "
					+ "where s.privateScenario = false or s.researchGroup.researchGroupId in "
					+ "(select m.researchGroup.researchGroupId from ResearchGroupMembership m where m.person.personId = :personId)";
			return ((Long) getSessionFactory().getCurrentSession().createQuery(query).setParameter("personId", person.getPersonId()).uniqueResult()).intValue();
		}
	}

	@Override
	public void flush() {
		getHibernateTemplate().flush();
	}

	private String getCondition(String choice) {
		if (choice.equals("minScenarioLength")) {
			return ">=";
		}
		if (choice.equals("maxScenarioLength")) {
			return "<=";
		}
		return " like ";
	}

	private String getAuthor(String name) {
		String[] words = name.split(" ");
		if (words.length == 1) {
			return "(lower(person.givenname) like lower('%" + words[0] + "%')"
					+ " or lower(person.surname) like lower('%" + words[0] + "%'))";
		} else {
			return "(lower(person.givenname) like lower('%" + words[0] + "%')"
					+ " and lower(person.surname) like lower('%" + words[1] + "%')) or "
					+ "(lower(person.givenname) like lower('%" + words[1] + "%')"
					+ " and lower(person.surname) like lower('%" + words[0] + "%'))";
		}
	}

	@Transactional(readOnly = true)
	@Override
	public List<Scenario> getAllRecordsFull() {
		return super.getAllRecordsFull();
	}

    @Transactional(readOnly = true)
    @Override
    public Blob getScenarioFile(int scenarioId) {
        String query = "from Scenario s where s.scenarioId=:scenarioId";
        Scenario s = (Scenario) getSessionFactory().getCurrentSession().createQuery(query).setParameter("scenarioId", scenarioId).uniqueResult();
        return s.getScenarioFile();
    }
}
