package cz.zcu.kiv.eegdatabase.wui.core.scenario;

import java.util.List;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.logic.controller.search.SearchRequest;
import cz.zcu.kiv.eegdatabase.wui.core.GenericService;

public interface ScenarioService extends GenericService<Scenario, Integer> {

    List<Scenario> getScenariosWhereOwner(Person owner);

    List<Scenario> getRecordsNewerThan(long oracleScn);

    List<Scenario> getScenariosWhereOwner(Person person, int LIMIT);

    List<Scenario> getScenarioSearchResults(List<SearchRequest> request, int personId);

    void canSaveTitle(String title, int id);

    List<Scenario> getScenariosForList(Person person, int start, int count);

    int getScenarioCountForList(Person person);
}
