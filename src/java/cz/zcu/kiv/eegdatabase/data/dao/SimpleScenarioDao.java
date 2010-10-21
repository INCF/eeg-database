package cz.zcu.kiv.eegdatabase.data.dao;

import java.util.List;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.logic.controller.search.SearchRequest;
import java.util.ArrayList;
import org.springframework.web.util.NestedServletException;

/**
 * This class extends powers class SimpleGenericDao.
 * Class is determined only for Person.
 * @author Pavel Bořík, A06208
 */
public class SimpleScenarioDao
        extends SimpleGenericDao<Scenario, Integer> implements ScenarioDao {

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

  public List<Scenario> getScenarioSearchResults(List<SearchRequest> requests) throws NumberFormatException {

    boolean ignoreChoice = false;
    String hqlQuery = "from Scenario where ";
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
        Integer.parseInt(request.getCondition());
        hqlQuery += "scenarioLength" + getCondition(request.getSource()) + request.getCondition();
      } else if (request.getSource().equals("person")) {
        hqlQuery += getAuthor(request.getCondition());
      } else {
        hqlQuery += "lower(" + request.getSource() + ") like lower('%" + request.getCondition() + "%')";
      }
      ignoreChoice = false;
    }
    List<Scenario> results;
    try {
      results = getHibernateTemplate().find(hqlQuery);
    } catch (Exception e) {
      return new ArrayList<Scenario>();
    }

    return results;
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
      return "(lower(person.givenname) like lower('%" + words[0] + "%')" +
              " or lower(person.surname) like lower('%" + words[0] + "%'))";
    } else {
      return "(lower(person.givenname) like lower('%" + words[0] + "%')" +
              " and lower(person.surname) like lower('%" + words[1] + "%'))";
    }
  }
}
