package cz.zcu.kiv.eegdatabase.wui.core.scenarios.type;

import cz.zcu.kiv.eegdatabase.data.dao.ScenarioTypeDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ScenarioType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class ScenarioTypeServiceImpl implements ScenarioTypeService {

    protected Log log = LogFactory.getLog(getClass());

    ScenarioTypeDao dao;

    @Required
    public void setDao(ScenarioTypeDao dao) {
        this.dao = dao;
    }

    @Override
    @Transactional
    public Integer create(ScenarioType newInstance) {
        return dao.create(newInstance);
    }

    @Override
    @Transactional(readOnly = true)
    public ScenarioType read(Integer id) {
        return dao.read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScenarioType> readByParameter(String parameterName, Object parameterValue) {
        return dao.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional
    public void update(ScenarioType transientObject) {
        dao.update(transientObject);
    }

    @Override
    @Transactional
    public void delete(ScenarioType persistentObject) {
        dao.delete(persistentObject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScenarioType> getAllRecords() {
        return dao.getAllRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScenarioType> getRecordsAtSides(int first, int max) {
        return dao.getRecordsAtSides(first, max);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountRecords() {
        return dao.getCountRecords();
    }

    @Override
    public List<ScenarioType> getUnique(ScenarioType example) {
        return dao.findByExample(example);
    }
}
