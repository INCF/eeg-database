package cz.zcu.kiv.eegdatabase.wui.core.scenarios.type;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import cz.zcu.kiv.eegdatabase.data.pojo.ScenarioType;

public class ScenarioTypeFacadeImpl implements ScenarioTypeFacade {
    
    protected Log log = LogFactory.getLog(getClass());

    ScenarioTypeService service;

    @Required
    public void setService(ScenarioTypeService service) {
        this.service = service;
    }

    @Override
    public Integer create(ScenarioType newInstance) {
        return service.create(newInstance);
    }

    @Override
    public ScenarioType read(Integer id) {
        return service.read(id);
    }

    @Override
    public List<ScenarioType> readByParameter(String parameterName, int parameterValue) {
        return service.readByParameter(parameterName, parameterValue);
    }

    @Override
    public List<ScenarioType> readByParameter(String parameterName, String parameterValue) {
        return service.readByParameter(parameterName, parameterValue);
    }

    @Override
    public void update(ScenarioType transientObject) {
        service.update(transientObject);
    }

    @Override
    public void delete(ScenarioType persistentObject) {
        service.delete(persistentObject);
    }

    @Override
    public List<ScenarioType> getAllRecords() {
        return service.getAllRecords();
    }

    @Override
    public List<ScenarioType> getRecordsAtSides(int first, int max) {
        return service.getRecordsAtSides(first, max);
    }

    @Override
    public int getCountRecords() {
        return service.getCountRecords();
    }

}
