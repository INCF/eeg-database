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
 * SimpleExperimentDao.java, 2013/10/02 00:01 Jakub Rinkes
 * ****************************************************************************
 */
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.nosql.entities.ExperimentElastic;
import cz.zcu.kiv.eegdatabase.data.nosql.entities.GenericParameter;
import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.controller.search.SearchRequest;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang.NotImplementedException;
import org.elasticsearch.index.query.AndFilterBuilder;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import static org.elasticsearch.index.query.FilterBuilders.termFilter;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import org.elasticsearch.index.query.NestedFilterBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.OrFilterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

/**
 * This class extends powers (extend from) class SimpleGenericDao. Class is
 * determined only for Experiment.
 *
 * @author Pavel Bořík, A06208
 */
public class SimpleExperimentDao extends SimpleGenericDao<Experiment, Integer> implements ExperimentDao {

	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;

	public SimpleExperimentDao() {
		super(Experiment.class);
	}

	@Override
	public ElasticsearchTemplate getElasticsearchTemplate() {
		return elasticsearchTemplate;
	}

	public void setElasticsearchTemplate(ElasticsearchTemplate elasticsearchTemplate) {
		this.elasticsearchTemplate = elasticsearchTemplate;
	}

	public List<DataFile> getDataFilesWhereExpId(int experimentId) {
		String HQLselect = "from DataFile file where file.experiment.experimentId = :experimentId";
		return getHibernateTemplate().findByNamedParam(HQLselect, "experimentId", experimentId);
	}

	public List<DataFile> getDataFilesWhereId(int dataFileId) {
		String HQLselect = "from DataFile file where file.dataFileId = :dataFileId";
		return getHibernateTemplate().findByNamedParam(HQLselect, "dataFileId", dataFileId);
	}

	@Override
	public int getCountForExperimentsWhereOwner(Person person) {
		String query = "select count(e) from Experiment e where e.personByOwnerId.personId = :personId";
		return ((Long) getSessionFactory().getCurrentSession().createQuery(query).setParameter("personId", person.getPersonId()).uniqueResult()).intValue();
	}

	@Override
	public List<Experiment> getExperimentsWhereOwner(Person person, int limit) {
		return getExperimentsWhereOwner(person, 0, limit);
	}

	@Override
	public List<Experiment> getExperimentsWhereOwner(Person person, int start, int limit) {
		String query = "from Experiment e left join fetch e.scenario where e.personByOwnerId.personId = :personId order by e.startTime desc";
		return getSessionFactory().getCurrentSession().createQuery(query).setParameter("personId", person.getPersonId()).setFirstResult(start).setMaxResults(limit).list();
	}

	@Override
	public int getCountForExperimentsWhereSubject(Person person) {
		String query = "select count(e) from Experiment e where e.personBySubjectPersonId.personId = :personId";
		return ((Long) getSessionFactory().getCurrentSession().createQuery(query).setParameter("personId", person.getPersonId()).uniqueResult()).intValue();
	}

	@Override
	public List<Experiment> getExperimentsWhereSubject(Person person, int limit) {
		return getExperimentsWhereSubject(person, 0, limit);
	}

	@Override
	public List<Experiment> getExperimentsWhereSubject(Person person, int start, int limit) {
		String query = "from Experiment e left join fetch e.scenario where e.personBySubjectPersonId.personId = :personId order by e.startTime desc";
		return getSessionFactory().getCurrentSession().createQuery(query).setParameter("personId", person.getPersonId()).setFirstResult(start).setMaxResults(limit).list();
	}

	public Experiment getExperimentForDetail(int experimentId) {
		String query = "from Experiment e left join fetch e.dataFiles left join fetch e.hardwares left join fetch e.experimentOptParamVals left join fetch e.scenario "
				+ "left join fetch e.weather left join fetch e.projectTypes left join fetch e.diseases left join fetch e.pharmaceuticals pharmaceuticals "
				+ "left join fetch e.softwares left join fetch e.persons left join fetch e.experimentPackageConnections where e.experimentId = :experimentId";
		return (Experiment) getSessionFactory().getCurrentSession().createQuery(query).setParameter("experimentId", experimentId).uniqueResult();
	}

	public int getCountForAllExperimentsForUser(Person person) {
		if (person.getAuthority().equals("ROLE_ADMIN")) {
			String query = "select count(distinct e) from Experiment e "
					+ "left join e.researchGroup.researchGroupMemberships m ";
			return ((Long) getSessionFactory().getCurrentSession().createQuery(query).uniqueResult()).intValue();
		} else {
//            String query = "select count(distinct e) from Experiment e " +
//                    "left join e.researchGroup.researchGroupMemberships m " +
//                    "where " +
//                    "e.privateExperiment = false " +
//                    "or m.person.personId = :personId";
			String query = "select count(distinct epc.experiment) from ExperimentPackageConnection epc, ExperimentPackageLicense epl, "
					+ " PersonalLicense pl where "
					+ "epc.experimentPackage.experimentPackageId = epl.experimentPackage.experimentPackageId and "
					+ "epl.license.licenseId = pl.license.licenseId and "
					+ "pl.person.personId = :personId";
			return ((Long) getSessionFactory().getCurrentSession().createQuery(query).setParameter("personId", person.getPersonId()).uniqueResult()).intValue();
		}
	}

	@Override
	@Transactional
	public List<Experiment> getAllExperimentsForUser(Person person, int start, int count) {
		if (person.getAuthority().equals("ROLE_ADMIN")) {
			String query = "select distinct e from Experiment e join fetch e.scenario s join fetch e.personBySubjectPersonId p "
					+ "left join e.researchGroup.researchGroupMemberships m ";
			return getSessionFactory().getCurrentSession().createQuery(query).setFirstResult(start).setMaxResults(count).list();
		} else {
//            String query = "select distinct e from Experiment e join fetch e.scenario s join fetch e.personBySubjectPersonId p " +
//                    "left join e.researchGroup.researchGroupMemberships m " +
//                    "where " +
//                    "e.privateExperiment = false " +
//                    "or m.person.personId = :personId " +
//                    "order by e.startTime desc";
			String query = "select distinct e from Experiment e, ExperimentPackageConnection epc, ExperimentPackageLicense epl, PersonalLicense pl "
					+ " join fetch e.scenario s join fetch e.personBySubjectPersonId p "
					+ " where "
					+ "e.experimentId = epc.experiment.experimentId and "
					+ "epc.experimentPackage.experimentPackageId = epl.experimentPackage.experimentPackageId and "
					+ "epl.license.licenseId = pl.license.licenseId and "
					+ "pl.person.personId = :personId "
					+ "order by e.startTime desc";
			return getSessionFactory().getCurrentSession().createQuery(query).setParameter("personId", person.getPersonId()).setFirstResult(start).setMaxResults(count).list();
		}
	}

	public List<Experiment> getRecordsNewerThan(int personId) {
		String HQLselect = "SELECT ex, s FROM Experiment ex LEFT JOIN FETCH ex.scenario s "
				+ "WHERE ex.experimentId IN "
				+ "(SELECT epc.experiment.experimentId from ExperimentPackageConnection epc, ExperimentPackageLicense epl, "
				+ " PersonalLicense pl where "
				+ "epc.experimentPackage.experimentPackageId = epl.experimentPackage.experimentPackageId and "
				+ "epl.license.licenseId = pl.license.licenseId and "
				+ "pl.person.personId = :personId) "
				+ " ORDER BY ex.startTime DESC";
		String[] stringParams = {"personId"};
		Object[] objectParams = {personId};
		return getHibernateTemplate().findByNamedParam(HQLselect, stringParams, objectParams);
	}

	public List<Experiment> getExperimentSearchResults(List<SearchRequest> requests, int personId) throws NumberFormatException {
		List<Experiment> results;
		boolean ignoreChoice = false;
		int index = 0;
		List<Date> datas = new ArrayList<Date>();
		String hqlQuery = "from Experiment e left join fetch e.hardwares hw where ";
		try {
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
				if (request.getSource().equals("usedHardware")) {
					hqlQuery += " (lower(hw.title) like lower('%" + request.getCondition()
							+ "%') or lower(hw.type) like lower('%" + request.getCondition() + "%'))";

				} else if (request.getSource().endsWith("Time")) {
					String[] times = request.getCondition().split(" ");
					if (times.length == 1) {
						datas.add(ControllerUtils.getDateFormat().parse(request.getCondition()));
					}
					if (times.length > 1) {
						datas.add(ControllerUtils.getDateFormatWithTime().parse(request.getCondition()));
					}
					hqlQuery += "e." + request.getSource() + getCondition(request.getSource()) + " :ts" + index;
					index++;

				} else if (request.getSource().startsWith("age")) {
					hqlQuery += "e.personBySubjectPersonId.dateOfBirth"
							+ getCondition(request.getSource()) + "'" + getPersonYearOfBirth(request.getCondition()) + "'";
				} else if (request.getSource().endsWith("gender")) {
					hqlQuery += "e.personBySubjectPersonId.gender = '" + request.getCondition().toUpperCase().charAt(0) + "'";
				} else {
					hqlQuery += "lower(e." + request.getSource() + ")" + getCondition(request.getSource())
							+ "lower('%" + request.getCondition() + "%')";
				}
				ignoreChoice = false;
			}
//            hqlQuery += " and e.experimentId IN(SELECT e.experimentId FROM Experiment e LEFT JOIN e.researchGroup.researchGroupMemberships membership WHERE e.privateExperiment = false OR membership.person.id = " + personId + ")";
			hqlQuery += " and e.experimentId IN(SELECT epc.experiment.experimentId from ExperimentPackageConnection epc, ExperimentPackageLicense epl, "
					+ " PersonalLicense pl where "
					+ "epc.experimentPackage.experimentPackageId = epl.experimentPackage.experimentPackageId and "
					+ "epl.license.licenseId = pl.license.licenseId and "
					+ "pl.person.personId = " + personId + ")";
			Session ses = getSession();
			Query q = ses.createQuery(hqlQuery);
			int i = 0;
			for (Date date : datas) {
				q.setTimestamp("ts" + i, date);
				i++;
			}

			results = q.list();
		} catch (ParseException e) {
			throw new RuntimeException("Inserted date and time is not in valid format \n"
					+ "Valid format is DD/MM/YYYY HH:MM or DD/MM/YYYY.");
		} catch (Exception e) {
			return new ArrayList<Experiment>();
		}

		return results;
	}

	@Override
	public List<Experiment> getVisibleExperiments(int personId, int start, int limit) {

//        Criteria criteria = getSession().createCriteria(Experiment.class);
//        criteria.setMaxResults(limit);
//        criteria.add(Restrictions.ge("experimentId", start));
//        criteria.add(Restrictions.or(Restrictions.eq("personByOwnerId.personId", personId), Restrictions.eq("privateExperiment", false)));
//	return criteria.list();

		String query = "select distinct e from Experiment e, ExperimentPackageConnection epc, ExperimentPackageLicense epl, PersonalLicense pl "
				+ " join fetch e.scenario s join fetch e.personBySubjectPersonId p "
				+ " where "
				+ "e.experimentId = epc.experiment.experimentId and "
				+ "epc.experimentPackage.experimentPackageId = epl.experimentPackage.experimentPackageId and "
				+ "epl.license.licenseId = pl.license.licenseId and "
				+ "pl.person.personId = :personId "
				+ "order by e.startTime desc";
		return getSessionFactory().getCurrentSession().createQuery(query).setParameter("personId", personId).setFirstResult(start).setMaxResults(limit).list();



	}

	@Override
	public int getVisibleExperimentsCount(int personId) {
//        Criteria criteria = getSession().createCriteria(Experiment.class);
//        criteria.add(Restrictions.or(Restrictions.eq("personByOwnerId.personId", personId), Restrictions.eq("privateExperiment", false)));
//        return criteria.list().size();
		String query = "select count(distinct epc.experiment) from ExperimentPackageConnection epc, ExperimentPackageLicense epl, "
				+ " PersonalLicense pl where "
				+ "epc.experimentPackage.experimentPackageId = epl.experimentPackage.experimentPackageId and "
				+ "epl.license.licenseId = pl.license.licenseId and "
				+ "pl.person.personId = :personId";
		return ((Long) getSessionFactory().getCurrentSession().createQuery(query).setParameter("personId", personId).uniqueResult()).intValue();
	}

	private String getCondition(String choice) {
		if (choice.equals("startTime") || (choice.equals("ageMax"))) {
			return ">=";
		}
		if (choice.equals("endTime") || (choice.equals("ageMin"))) {
			return "<=";
		}
		return " like ";
	}

	private String getPersonYearOfBirth(String age) throws NumberFormatException {
		// Create a calendar object with the date of birth
		Calendar today = Calendar.getInstance(); // Get age based on year
		int year = Integer.parseInt(age);
		if (year < 0) {
			throw new RuntimeException("Invalid age value. It has to be non-negative number");
		}
		int yearOfBirth = today.get(Calendar.YEAR) - year;

		return today.get(Calendar.DATE) + "-" + (today.get(Calendar.MONTH) + 1) + "-" + yearOfBirth;
	}

	@Transactional(readOnly = true)
	@Override
	public List<Experiment> getAllRecordsFull() {
		return super.getAllRecordsFull();
	}

	@Transactional(readOnly = true)
	private List<Experiment> transformEsResultToHibernate(List<ExperimentElastic> experiments) {
		List<Integer> ids = new ArrayList<Integer>();
		for (ExperimentElastic e : experiments) {
			ids.add(Integer.parseInt(e.getExperimentId()));
		}
		return this.getExperimentsById(ids);
	}

	private List<Experiment> getExperimentsById(List<Integer> ids) {
		if (ids.isEmpty()) {
			return new ArrayList<Experiment>();
		}
		String query = "from Experiment e where e.experimentId IN ( :ids )";
		return getSessionFactory().getCurrentSession().createQuery(query).setParameterList("ids", ids).list();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Experiment> searchByParameter(String paramName, String paramValue) {
		GenericParameter[] p = {new GenericParameter(paramName, paramValue)};
		return this.searchByParameters(p);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Experiment> searchByParameter(String paramName, double paramValue) {
		GenericParameter[] p = {new GenericParameter(paramName, paramValue)};
		return this.searchByParameters(p);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Experiment> searchByParameterRange(String paramName, int min, int max) {
		NestedFilterBuilder b = new NestedFilterBuilder("params", rangeQuery("params.valueInteger").from(min).to(max));
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withFilter(b).build();
		searchQuery.setPageable(new PageRequest(0, 1000));
		List<ExperimentElastic> list = this.elasticsearchTemplate.queryForList(searchQuery, ExperimentElastic.class);
		return this.transformEsResultToHibernate(list);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Experiment> searchByParameters(GenericParameter[] params) {
		GenericParameter[] not = {};
		return this.searchByParameters(params, not);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Experiment> searchByParameters(GenericParameter[] contains, GenericParameter[] notContains) {

		AndFilterBuilder and = new AndFilterBuilder();
		for (GenericParameter p : contains) {
			BoolQueryBuilder b = new BoolQueryBuilder();
			Object value = p.getValueString() == null ? p.getValueInteger() : p.getValueString();
			String fieldName = p.getValueString() == null ? "params.valueInteger" : "params.valueString";
			b.must(termQuery("params.name", p.getName())).must(matchQuery(fieldName, value));
			and.add(new NestedFilterBuilder("params", b));
		}

		for (GenericParameter p : notContains) {
			BoolQueryBuilder b = new BoolQueryBuilder();
			Object value = p.getValueString() == null ? p.getValueInteger() : p.getValueString();
			String fieldName = p.getValueString() == null ? "params.valueInteger" : "params.valueString";
			b.must(termQuery("params.name", p.getName())).must(matchQuery(fieldName, value));
			BoolFilterBuilder not = new BoolFilterBuilder();
			not.mustNot(new NestedFilterBuilder("params", b));
			and.add(not);
		}

		SearchQuery searchQuery = new NativeSearchQueryBuilder().withFilter(and).build();
		searchQuery.setPageable(new PageRequest(0, 1000));
		List<ExperimentElastic> list = this.elasticsearchTemplate.queryForList(searchQuery, ExperimentElastic.class);
		return this.transformEsResultToHibernate(list);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Experiment> search(String value) {

		OrFilterBuilder or = new OrFilterBuilder();
		or.add(new NestedFilterBuilder("params", new MatchQueryBuilder("params.valueString", value)));
		or.add(new NestedFilterBuilder("params.attributes", new MatchQueryBuilder("params.attributes.value", value)));
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withFilter(or).build();
		searchQuery.setPageable(new PageRequest(0, 1000));

		List<ExperimentElastic> list = this.elasticsearchTemplate.queryForList(searchQuery, ExperimentElastic.class);
		return this.transformEsResultToHibernate(list);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Experiment> searchByAttribute(String attrName, String attrValue) {
		throw new NotImplementedException();
	}
}
