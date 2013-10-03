package cz.zcu.kiv.eegdatabase.wui.core.common;

import cz.zcu.kiv.eegdatabase.data.dao.SimpleSoftwareDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Software;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Prasek
 * Date: 16.4.13
 * Time: 13:28
 * To change this template use File | Settings | File Templates.
 */
public class SoftwareServiceImpl implements SoftwareService{

    protected Log log = LogFactory.getLog(getClass());

    SimpleSoftwareDao softwareDao;

    @Required
    public void setSoftwareDao(SimpleSoftwareDao softwareDao){
        this.softwareDao = softwareDao;
    }

    @Override
    @Transactional
    public void createDefaultRecord(Software software) {
        softwareDao.createDefaultRecord(software);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isDefault(int id) {
        return softwareDao.isDefault(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canSaveDefaultTitle(String title, int swId) {
        return softwareDao.canSaveDefaultTitle(title, swId);
    }

    @Override
    @Transactional
    public Integer create(Software newInstance) {
        return softwareDao.create(newInstance);
    }

    @Override
    @Transactional(readOnly = true)
    public Software read(Integer id) {
        return softwareDao.read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Software> readByParameter(String parameterName, Object parameterValue) {
        return softwareDao.readByParameter(parameterName, parameterValue);
    }


    @Override
    @Transactional
    public void update(Software transientObject) {
        softwareDao.update(transientObject);
    }

    @Override
    @Transactional
    public void delete(Software persistentObject) {
        softwareDao.delete(persistentObject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Software> getAllRecords() {
        return softwareDao.getAllRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Software> getRecordsAtSides(int first, int max) {
        return softwareDao.getRecordsAtSides(first,max);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountRecords() {
        return softwareDao.getCountRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Software> getUnique(Software example) {
        return softwareDao.findByExample(example);
    }
}
