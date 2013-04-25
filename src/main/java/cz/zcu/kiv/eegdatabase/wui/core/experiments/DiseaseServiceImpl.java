package cz.zcu.kiv.eegdatabase.wui.core.experiments;

import cz.zcu.kiv.eegdatabase.data.dao.SimpleDiseaseDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Disease;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 16.4.13
 * Time: 10:27
 */
public class DiseaseServiceImpl implements DiseaseService {
    SimpleDiseaseDao diseaseDao;

    @Required
    public void setDiseaseDao(SimpleDiseaseDao diseaseDao) {
        this.diseaseDao = diseaseDao;
    }

    @Override
    @Transactional
    public Integer create(Disease newInstance) {
        return diseaseDao.create(newInstance);
    }

    @Override
    public Disease read(Integer id) {
        return diseaseDao.read(id);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Disease> readByParameter(String parameterName, int parameterValue) {
        return diseaseDao.readByParameter(parameterName, parameterValue);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Disease> readByParameter(String parameterName, String parameterValue) {
        return diseaseDao.readByParameter(parameterName,parameterValue);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void update(Disease transientObject) {
        diseaseDao.update(transientObject);//To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void delete(Disease persistentObject) {
        diseaseDao.delete(persistentObject);//To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Disease> getAllRecords() {
        return diseaseDao.getAllRecords();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Disease> getRecordsAtSides(int first, int max) {
        return diseaseDao.getRecordsAtSides(first, max);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getCountRecords() {
        return diseaseDao.getCountRecords();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsDisease(String name) {
        List<Disease> existingDisease = diseaseDao.readByParameter("title", name);
        return existingDisease != null && existingDisease.size() > 0;
    }
}
