package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.logic.controller.search.SearchRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * This class extends powers class SimpleGenericDao.
 * Class is determined only for Person.
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

    public List<Scenario> getRecordsNewerThan(long oracleScn) {
        String hqlQuery = "from Scenario s where s.scn > :oracleScn";
        return getHibernateTemplate().findByNamedParam(hqlQuery, "oracleScn", oracleScn);
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

        hqlQuery += ") and (private=0 or person.personId = " + personId + ")";
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
                    " and lower(person.surname) like lower('%" + words[1] + "%')) or " +
                    "(lower(person.givenname) like lower('%" + words[1] + "%')" +
                    " and lower(person.surname) like lower('%" + words[0] + "%'))";
        }
    }
}
