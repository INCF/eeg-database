package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.controller.search.SearchRequest;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * This class extends powers (extend from) class SimpleGenericDao.
 * Class is determined only for Experiment.
 *
 * @author Pavel Bořík, A06208
 */
public class SimpleExperimentDao<T, PK extends Serializable>
        extends SimpleGenericDao<T, PK> implements ExperimentDao<T, PK> {

    public SimpleExperimentDao(Class<T> type) {
        super(type);
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
        return getExperimentsWhereOwner(person, 1, limit);
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
        String query = "from Experiment e left join fetch e.dataFiles left join fetch e.hardwares left join fetch e.experimentOptParamVals left join fetch e.scenario " +
                "where e.experimentId = :experimentId";
        return (Experiment) getSessionFactory().getCurrentSession().createQuery(query).setParameter("experimentId", experimentId).uniqueResult();
    }

    public int getCountForAllExperimentsForUser(Person person) {
        if (person.getAuthority().equals("ROLE_ADMIN")) {
            String query = "select count(distinct e) from Experiment e " +
                    "left join e.researchGroup.researchGroupMemberships m ";
            return ((Long) getSessionFactory().getCurrentSession().createQuery(query).uniqueResult()).intValue();
        } else {
            String query = "select count(distinct e) from Experiment e " +
                    "left join e.researchGroup.researchGroupMemberships m " +
                    "where " +
                    "e.privateExperiment = false " +
                    "or m.person.personId = :personId";
            return ((Long) getSessionFactory().getCurrentSession().createQuery(query).setParameter("personId", person.getPersonId()).uniqueResult()).intValue();
        }
    }

    @Override
    public List<Experiment> getAllExperimentsForUser(Person person, int start, int count) {
        if (person.getAuthority().equals("ROLE_ADMIN")) {
            String query = "select distinct e from Experiment e join fetch e.scenario s join fetch e.personBySubjectPersonId p " +
                    "left join e.researchGroup.researchGroupMemberships m ";
            return getSessionFactory().getCurrentSession().createQuery(query).setFirstResult(start).setMaxResults(count).list();
        } else {
            String query = "select distinct e from Experiment e join fetch e.scenario s join fetch e.personBySubjectPersonId p " +
                    "left join e.researchGroup.researchGroupMemberships m " +
                    "where " +
                    "e.privateExperiment = false " +
                    "or m.person.personId = :personId " +
                    "order by e.startTime desc";
            return getSessionFactory().getCurrentSession().createQuery(query).setParameter("personId", person.getPersonId()).setFirstResult(start).setMaxResults(count).list();
        }
    }

    public List<Experiment> getRecordsNewerThan(long oracleScn, int personId) {
        String HQLselect = "SELECT ex, s FROM Experiment ex LEFT JOIN FETCH ex.scenario s WHERE ex.experimentId IN (SELECT e.experimentId FROM Experiment e LEFT JOIN e.researchGroup.researchGroupMemberships membership WHERE e.privateExperiment = false OR membership.person.id = :personId) AND ex.scn > :oracleScn ORDER BY ex.startTime DESC";
        String[] stringParams = {"personId", "oracleScn"};
        Object[] objectParams = {personId, oracleScn};
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
                    hqlQuery += " (lower(hw.title) like lower('%" + request.getCondition() +
                            "%') or lower(hw.type) like lower('%" + request.getCondition() + "%'))";

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
                    hqlQuery += "e.personBySubjectPersonId.dateOfBirth" +
                            getCondition(request.getSource()) + "'" + getPersonYearOfBirth(request.getCondition()) + "'";
                } else if (request.getSource().endsWith("gender")) {
                    hqlQuery += "e.personBySubjectPersonId.gender = '" + request.getCondition().toUpperCase().charAt(0) + "'";
                } else {
                    hqlQuery += "lower(e." + request.getSource() + ")" + getCondition(request.getSource()) +
                            "lower('%" + request.getCondition() + "%')";
                }
                ignoreChoice = false;
            }
            hqlQuery += " and e.experimentId IN(SELECT e.experimentId FROM Experiment e LEFT JOIN e.researchGroup.researchGroupMemberships membership WHERE e.privateExperiment = false OR membership.person.id = " + personId + ")";
            Session ses = getSession();
            Query q = ses.createQuery(hqlQuery);
            int i = 0;
            for (Date date : datas) {
                q.setTimestamp("ts" + i, date);
                i++;
            }

            results = q.list();
        } catch (ParseException e) {
            throw new RuntimeException("Inserted date and time is not in valid format \n" +
                    "Valid format is DD/MM/YYYY HH:MM or DD/MM/YYYY.");
        } catch (Exception e) {
            return new ArrayList<Experiment>();
        }

        return results;
    }

    @Override
    public List<Experiment> getVisibleExperiments(int personId, int start, int limit) {

        Criteria criteria = getSession().createCriteria(Experiment.class);
        criteria.setMaxResults(limit);
        criteria.add(Restrictions.ge("experimentId", start));
        criteria.add(Restrictions.or(Restrictions.eq("personByOwnerId.personId", personId), Restrictions.eq("privateExperiment", false)));
       return criteria.list();


    }

    @Override
    public int getVisibleExperimentsCount(int personId) {
        Criteria criteria = getSession().createCriteria(Experiment.class);
        criteria.add(Restrictions.or(Restrictions.eq("personByOwnerId.personId", personId), Restrictions.eq("privateExperiment", false)));
        return criteria.list().size();
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
    public List<T> getAllRecordsFull() {
        return super.getAllRecordsFull();
    }
}
