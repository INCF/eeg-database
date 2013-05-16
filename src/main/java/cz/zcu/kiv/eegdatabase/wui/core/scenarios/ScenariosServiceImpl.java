package cz.zcu.kiv.eegdatabase.wui.core.scenarios;

import cz.zcu.kiv.eegdatabase.data.dao.ScenarioDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.logic.controller.search.SearchRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class ScenariosServiceImpl implements ScenariosService {

    protected Log log = LogFactory.getLog(getClass());

    ScenarioDao scenarioDAO;

    @Required
    public void setScenarioDAO(ScenarioDao scenarioDAO) {
        this.scenarioDAO = scenarioDAO;
    }

    @Override
    @Transactional
    public Integer create(Scenario newInstance) {
        return scenarioDAO.create(newInstance);
    }

    @Override
    @Transactional(readOnly = true)
    public Scenario read(Integer id) {
        return scenarioDAO.read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Scenario> readByParameter(String parameterName, int parameterValue) {
        return scenarioDAO.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Scenario> readByParameter(String parameterName, String parameterValue) {
        return scenarioDAO.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional
    public void update(Scenario transientObject) {
        scenarioDAO.update(transientObject);
    }

    @Override
    @Transactional
    public void delete(Scenario persistentObject) {
        scenarioDAO.delete(persistentObject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Scenario> getAllRecords() {
        return scenarioDAO.getAllRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Scenario> getRecordsAtSides(int first, int max) {
        return scenarioDAO.getRecordsAtSides(first, max);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountRecords() {
        return scenarioDAO.getCountRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Scenario> getUnique(Scenario example) {
        return scenarioDAO.findByExample(example);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Scenario> getScenariosWhereOwner(Person owner) {
        return scenarioDAO.getScenariosWhereOwner(owner);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Scenario> getRecordsNewerThan(long oracleScn) {
        return scenarioDAO.getRecordsNewerThan(oracleScn);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Scenario> getScenariosWhereOwner(Person person, int LIMIT) {
        return scenarioDAO.getScenariosWhereOwner(person, LIMIT);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Scenario> getScenarioSearchResults(List<SearchRequest> request, int personId) {
        return scenarioDAO.getScenarioSearchResults(request, personId);
    }

    @Override
    @Transactional(readOnly = true)
    public void canSaveTitle(String title, int id) {
        scenarioDAO.canSaveTitle(title, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Scenario> getScenariosForList(Person person, int start, int count) {
        return scenarioDAO.getScenariosForList(person, start, count);
    }

    @Override
    @Transactional(readOnly = true)
    public int getScenarioCountForList(Person person) {
        return scenarioDAO.getScenarioCountForList(person);
    }

    @Override
    public void flush() {
        scenarioDAO.flush();
    }

    @Override
    @Transactional(readOnly = true)
    public Scenario getScenarioByTitle(String title) {
        List<Scenario> scenarios = scenarioDAO.readByParameter("title", title);
        if(scenarios.size() > 0){
            return scenarios.get(0);
        } else {
            return null;
        }
    }

}
