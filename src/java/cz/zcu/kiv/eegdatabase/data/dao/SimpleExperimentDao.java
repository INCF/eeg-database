package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import java.io.Serializable;
import java.util.List;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.logic.controller.search.SearchRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * This class extends powers (extend from) class SimpleGenericDao.
 * Class is determined only for Experiment.
 * @author Pavel Bořík, A06208
 */
public class SimpleExperimentDao<T, PK extends Serializable>
        extends SimpleGenericDao<T, PK> implements ExperimentDao<T, PK> {

  public SimpleExperimentDao(Class<T> type) {
    super(type);
  }

  public List<Experiment> getExperimentsWhereOwner(int personId) {
    String HQLselect = "from Experiment experiment " + "where experiment.personByOwnerId.personId = :personId";
    return getHibernateTemplate().
            findByNamedParam(HQLselect, "personId", personId);
  }

  public List getExperimentsWhereOwner(Person person, int limit) {
    getHibernateTemplate().setMaxResults(limit);
    List<Experiment> list = getExperimentsWhereOwner(person.getPersonId());
    getHibernateTemplate().setMaxResults(0);
    return list;
  }

  public List<Experiment> getExperimentsWhereSubject(int personId) {
    String HQLselect = "from Experiment experiment " + "where experiment.personBySubjectPersonId.personId = :personId";
    // find records with item personId = personId
    return getHibernateTemplate().
            findByNamedParam(HQLselect, "personId", personId);
  }

  public List getExperimentsWhereSubject(Person person, int limit) {
    getHibernateTemplate().setMaxResults(limit);
    List<Experiment> list = getExperimentsWhereSubject(person.getPersonId());
    getHibernateTemplate().setMaxResults(0);
    return list;
  }

  public List<Experiment> getExperimentsWhereMember(int groupId) {
    String HQLselect = "from Experiment experiment " + "where experiment.researchGroup.researchGroupId = :researchGroupId";
    return getHibernateTemplate().findByNamedParam(HQLselect, "researchGroupId", groupId);
  }

  public List<Experiment> getExperimentSearchResults(List<SearchRequest> requests) {

    boolean ignoreChoice = false;
    String hqlQuery = "from Experiment e left join fetch e.hardwares hw where ";
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
        hqlQuery += "e." + request.getSource() + getCondition(request.getSource()) + "'" + request.getCondition() + "'";
      } else if (request.getSource().startsWith("age")) {
        hqlQuery += "e.personBySubjectPersonId.dateOfBirth" +
                getCondition(request.getSource()) + "'" + getPersonYearOfBirth(request.getCondition()) + "'";
      } else if (request.getSource().endsWith("gender")) {
        hqlQuery += "e.personBySubjectPersonId.gender = '" + request.getCondition().toUpperCase().charAt(0) + "'";
      } else {
        hqlQuery += "lower(e." + request.getSource() + ")" + getCondition(request.getSource()) +
                "lower('%" + request.getCondition() + "%')";
      }
    }

    // System.out.println(hqlQuery);
    List<Experiment> results;
    try {
      results = getHibernateTemplate().find(hqlQuery);
    } catch (Exception e) {
      return new ArrayList<Experiment>();
    }

    return results;
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

  private String getPersonYearOfBirth(String age) {
    // Create a calendar object with the date of birth
    Calendar today = Calendar.getInstance(); // Get age based on year
    int yearOfBirth = today.get(Calendar.YEAR) - Integer.parseInt(age);

    return today.get(Calendar.DATE) + "-" + (today.get(Calendar.MONTH) + 1) + "-" + yearOfBirth;
  }
}
