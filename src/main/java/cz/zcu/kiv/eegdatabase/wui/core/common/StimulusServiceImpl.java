package cz.zcu.kiv.eegdatabase.wui.core.common;

import cz.zcu.kiv.eegdatabase.data.dao.SimpleStimulusDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Stimulus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Matheo
 * Date: 16.4.13
 * Time: 19:48
 * To change this template use File | Settings | File Templates.
 */
public class StimulusServiceImpl implements StimulusService {

    protected Log log = LogFactory.getLog(getClass());

    SimpleStimulusDao stimulusDao;

    @Required
    public void setStimulusDao(SimpleStimulusDao stimulusDao) {
        this.stimulusDao = stimulusDao;
    }


    @Override
    public Integer create(Stimulus newInstance) {
        return stimulusDao.create(newInstance);
    }

    @Override
    public Stimulus read(Integer id) {
        return stimulusDao.read(id);
    }

    @Override
    public List<Stimulus> readByParameter(String parameterName, int parameterValue) {
        return stimulusDao.readByParameter(parameterName, parameterValue);
    }

    @Override
    public List<Stimulus> readByParameter(String parameterName, String parameterValue) {
        return stimulusDao.readByParameter(parameterName, parameterValue);
    }

    @Override
    public void update(Stimulus transientObject) {
         stimulusDao.update(transientObject);
    }

    @Override
    public void delete(Stimulus persistentObject) {
         stimulusDao.delete(persistentObject);
    }

    @Override
    public List<Stimulus> getAllRecords() {
        return stimulusDao.getAllRecords();
    }

    @Override
    public List<Stimulus> getRecordsAtSides(int first, int max) {
        return stimulusDao.getRecordsAtSides(first, max);
    }

    @Override
    public int getCountRecords() {
        return stimulusDao.getCountRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canSaveDescription(String description) {
        return stimulusDao.canSaveDescription(description);
    }
}
