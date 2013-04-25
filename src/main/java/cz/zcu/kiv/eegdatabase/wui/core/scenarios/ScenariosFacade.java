package cz.zcu.kiv.eegdatabase.wui.core.scenarios;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.logic.controller.search.SearchRequest;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacade;

import java.util.List;

public interface ScenariosFacade extends GenericFacade<Scenario, Integer> {

    List<Scenario> getScenariosWhereOwner(Person owner);

    List<Scenario> getRecordsNewerThan(long oracleScn);

    List<Scenario> getScenariosWhereOwner(Person person, int LIMIT);

    List<Scenario> getScenarioSearchResults(List<SearchRequest> request, int personId);

    void canSaveTitle(String title, int id);

    List<Scenario> getScenariosForList(Person person, int start, int count);

    int getScenarioCountForList(Person person);

    boolean existsScenario(String title);

    void flush();

    Scenario getScenarioByTitle(String input);
}
