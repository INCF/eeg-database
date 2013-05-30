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
    @Transactional
    public Integer create(Stimulus newInstance) {
        return stimulusDao.create(newInstance);
    }

    @Override
    @Transactional(readOnly = true)
    public Stimulus read(Integer id) {
        return stimulusDao.read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Stimulus> readByParameter(String parameterName, Object parameterValue) {
        return stimulusDao.readByParameter(parameterName, parameterValue);
    }


    @Override
    @Transactional
    public void update(Stimulus transientObject) {
         stimulusDao.update(transientObject);
    }

    @Override
    @Transactional
    public void delete(Stimulus persistentObject) {
         stimulusDao.delete(persistentObject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Stimulus> getAllRecords() {
        return stimulusDao.getAllRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Stimulus> getRecordsAtSides(int first, int max) {
        return stimulusDao.getRecordsAtSides(first, max);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountRecords() {
        return stimulusDao.getCountRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Stimulus> getUnique(Stimulus example) {
        return stimulusDao.findByExample(example);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canSaveDescription(String description) {
        return stimulusDao.canSaveDescription(description);
    }
}
