package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.logic.controller.search.SearchRequest;

import java.util.List;

/**
 * DAO for fetching and saving objects with people.
 *
 * @author Jindrich Pergler
 */
public interface ScenarioDao extends GenericDao<Scenario, Integer> {

    public List<Scenario> getScenariosWhereOwner(Person owner);

    public List<Scenario> getRecordsNewerThan(long oracleScn);

    public List getScenariosWhereOwner(Person person, int LIMIT);

    public List<Scenario> getScenarioSearchResults(List<SearchRequest> request, int personId);

    boolean canSaveTitle(String title, int id);

    public List<Scenario> getScenariosForList(Person person, int start, int count);

    public int getScenarioCountForList(Person person);
}
