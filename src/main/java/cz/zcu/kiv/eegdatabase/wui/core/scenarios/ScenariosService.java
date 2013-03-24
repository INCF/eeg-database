package cz.zcu.kiv.eegdatabase.wui.core.scenarios;

import java.util.List;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.data.pojo.ScenarioSchemas;
import cz.zcu.kiv.eegdatabase.logic.controller.search.SearchRequest;
import cz.zcu.kiv.eegdatabase.wui.core.GenericService;

public interface ScenariosService extends GenericService<Scenario, Integer> {

    List<Scenario> getScenariosWhereOwner(Person owner);

    List<Scenario> getRecordsNewerThan(long oracleScn);

    List<Scenario> getScenariosWhereOwner(Person person, int LIMIT);

    List<Scenario> getScenarioSearchResults(List<SearchRequest> request, int personId);

    boolean canSaveTitle(String title, int id);

    List<Scenario> getScenariosForList(Person person, int start, int count);

    int getScenarioCountForList(Person person);
    
    void flush();

    Scenario getScenarioByTitle(String title);
    
    Integer create(ScenarioSchemas newInstance);

    Integer createScenarioSchema(ScenarioSchemas newInstance);

    ScenarioSchemas readScenarioSchema(Integer id);

    List<ScenarioSchemas> readScenarioSchemaByParameter(String parameterName, int parameterValue);

    List<ScenarioSchemas> readScenarioSchemaByParameter(String parameterName, String parameterValue);

    void updateScenarioSchema(ScenarioSchemas transientObject);

    void deleteScenarioSchema(ScenarioSchemas persistentObject);

    List<ScenarioSchemas> getAllScenarioSchemasRecords();

    List<ScenarioSchemas> getScenarioSchemasRecordsAtSides(int first, int max);

    int getCountScenarioSchemasRecords();
    
    int getNextSchemaId();
    
    List<ScenarioSchemas> getListOfScenarioSchemas();
}
