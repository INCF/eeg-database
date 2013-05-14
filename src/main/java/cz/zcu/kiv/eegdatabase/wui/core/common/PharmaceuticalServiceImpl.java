package cz.zcu.kiv.eegdatabase.wui.core.common;

import cz.zcu.kiv.eegdatabase.data.dao.SimplePharmaceuticalDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Pharmaceutical;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Matheo
 * Date: 16.4.13
 * Time: 20:07
 * To change this template use File | Settings | File Templates.
 */
public class PharmaceuticalServiceImpl implements PharmaceuticalService {

    protected Log log = LogFactory.getLog(getClass());

    SimplePharmaceuticalDao pharmaceuticalDao;

    @Required
    public void setPharmaceuticalDao(SimplePharmaceuticalDao pharmaceuticalDao) {
        this.pharmaceuticalDao = pharmaceuticalDao;
    }

    @Override
    public Integer create(Pharmaceutical newInstance) {
        return pharmaceuticalDao.create(newInstance);
    }

    @Override
    public Pharmaceutical read(Integer id) {
        return pharmaceuticalDao.read(id);
    }

    @Override
    public List<Pharmaceutical> readByParameter(String parameterName, int parameterValue) {
        return pharmaceuticalDao.readByParameter(parameterName, parameterValue);
    }

    @Override
    public List<Pharmaceutical> readByParameter(String parameterName, String parameterValue) {
        return pharmaceuticalDao.readByParameter(parameterName, parameterValue);
    }

    @Override
    public void update(Pharmaceutical transientObject) {
        pharmaceuticalDao.update(transientObject);
    }

    @Override
    public void delete(Pharmaceutical persistentObject) {
        pharmaceuticalDao.delete(persistentObject);
    }

    @Override
    public List<Pharmaceutical> getAllRecords() {
        return pharmaceuticalDao.getAllRecords();
    }

    @Override
    public List<Pharmaceutical> getRecordsAtSides(int first, int max) {
        return pharmaceuticalDao.getRecordsAtSides(first, max);
    }

    @Override
    public int getCountRecords() {
        return pharmaceuticalDao.getCountRecords();
    }

    @Override
    public List<Pharmaceutical> getUnique(Pharmaceutical example) {
        return pharmaceuticalDao.findByExample(example);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canSaveTitle(String title){
        return pharmaceuticalDao.canSaveTitle(title);
    }
}
