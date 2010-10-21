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
    boolean hardware = false;
    int countHw = 0;
    int countNonHw = 0;
    String hardwareQuery = "";
    String andOr = "";
    String hqlQuery = "from Experiment where ";
    for (SearchRequest request : requests) {
      if (request.getSource().equals("usedHardware")) {
        hardware = true;
        if (countHw > 0) {
          hardwareQuery += request.getChoice();
        }
        hardwareQuery +=
                " (lower(hw.title) like lower('%" + request.getCondition() +
                "%'} or lower(hw.type) like lower('%" + request.getCondition() + "%'))";
        andOr = request.getChoice();
        countHw++;
        continue;
      }
      if (countNonHw > 0) {
        hqlQuery += request.getChoice();
      }
      countNonHw++;
      if (andOr.equals("")) {
        andOr = request.getChoice();
      }
      if (request.getSource().endsWith("Time")) {
        hqlQuery += request.getSource() + getCondition(request.getSource()) + "'" + request.getCondition() + "'";
      } else if (request.getSource().startsWith("age")) {
        try {
          hqlQuery += "personBySubjectPersonId.dateOfBirth" +
                  getCondition(request.getSource()) + "'" + getPersonYearOfBirth(request.getCondition()) + "'";
        } catch (Exception e) {
          continue;
        }

      } else if (request.getSource().endsWith("gender")) {
        hqlQuery += "personBySubjectPersonId.gender = '" + request.getCondition().toUpperCase().charAt(0) + "'";
      } else {
        hqlQuery += "lower(" + request.getSource() + ")" + getCondition(request.getSource()) +
                "lower('%" + request.getCondition() + "%')";
      }
    }
    List<Experiment> hardwares = new ArrayList<Experiment>();
    if (hardware) {
      hardwareQuery = "from Experiment e" +
              " left join fetch e.hardwares hw where" + hardwareQuery;

      System.out.println(hardwareQuery);
      hardwares = getHibernateTemplate().find(hardwareQuery);
    }
    List<Experiment> results = new ArrayList<Experiment>();
    try {
      System.out.println(hqlQuery);
      // System.out.println(andOr);
      results = getHibernateTemplate().find(hqlQuery);
    } catch (Exception e) {
    } finally {
      results = connectList(andOr, results, hardwares);
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
    // Create a calendar object with the date of birth 
    Calendar dateOfBirth = new GregorianCalendar(yearOfBirth, Calendar.JANUARY, Integer.parseInt(age));


    return "01-01-" + yearOfBirth;
  }

  private List<Experiment> connectList(String condition, List<Experiment> results, List<Experiment> hardware) {
    if (hardware.isEmpty()) {
      return results;
    }
    if (results.isEmpty()) {
      return hardware;
    }
    if (condition.equals(" or ")) {
      results.addAll(hardware);
      return results;
    }
    List<Experiment> newResults = new ArrayList<Experiment>();
    for (int i = 0; i < results.size(); i++) {
      for (int j = 0; j < hardware.size(); j++) {
        if (results.get(i).getExperimentId() == hardware.get(j).getExperimentId()) {
          newResults.add(results.get(i));
        }
      }
    }
    return newResults;
  }
}
