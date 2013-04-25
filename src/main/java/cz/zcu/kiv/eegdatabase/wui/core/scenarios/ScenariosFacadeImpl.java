package cz.zcu.kiv.eegdatabase.wui.core.scenarios;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.logic.controller.search.SearchRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class ScenariosFacadeImpl implements ScenariosFacade {

    protected Log log = LogFactory.getLog(getClass());

    ScenariosService service;

    @Required
    public void setService(ScenariosService service) {
        this.service = service;
    }

    @Override
    public Integer create(Scenario newInstance) {
        return service.create(newInstance);
    }

    @Override
    public Scenario read(Integer id) {
        return service.read(id);
    }

    @Override
    public List<Scenario> readByParameter(String parameterName, int parameterValue) {
        return service.readByParameter(parameterName, parameterValue);
    }

    @Override
    public List<Scenario> readByParameter(String parameterName, String parameterValue) {
        return service.readByParameter(parameterName, parameterValue);
    }

    @Override
    public void update(Scenario transientObject) {
        service.update(transientObject);
    }

    @Override
    public void delete(Scenario persistentObject) {
        service.delete(persistentObject);
    }

    @Override
    public List<Scenario> getAllRecords() {
        return service.getAllRecords();
    }

    @Override
    public List<Scenario> getRecordsAtSides(int first, int max) {
        return service.getRecordsAtSides(first, max);
    }

    @Override
    public int getCountRecords() {
        return service.getCountRecords();
    }

    @Override
    public List<Scenario> getScenariosWhereOwner(Person owner) {
        return service.getScenariosWhereOwner(owner);
    }

    @Override
    public List<Scenario> getRecordsNewerThan(long oracleScn) {
        return service.getRecordsNewerThan(oracleScn);
    }

    @Override
    public List<Scenario> getScenariosWhereOwner(Person person, int LIMIT) {
        return service.getScenariosWhereOwner(person, LIMIT);
    }

    @Override
    public List<Scenario> getScenarioSearchResults(List<SearchRequest> request, int personId) {
        return service.getScenarioSearchResults(request, personId);
    }

    @Override
    public void canSaveTitle(String title, int id) {
        service.canSaveTitle(title, id);
    }

    @Override
    public List<Scenario> getScenariosForList(Person person, int start, int count) {
        return service.getScenariosForList(person, start, count);
    }

    @Override
    public int getScenarioCountForList(Person person) {
        return service.getScenarioCountForList(person);
    }

    @Override
    public boolean existsScenario(String title) {
        List<Scenario> scenarios = service.readByParameter("title",title);
        return scenarios != null && scenarios.size() > 0;
    }

    @Override
    public void flush() {
        service.flush();
    }
}
