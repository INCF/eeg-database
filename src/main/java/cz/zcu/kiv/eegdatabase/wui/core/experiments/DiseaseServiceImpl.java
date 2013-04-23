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
    @Transactional(readOnly = true)
    public boolean existsDisease(String name) {
        List<Disease> existingDisease = diseaseDao.readByParameter("title", name);
        return existingDisease != null && existingDisease.size() > 0;
    }
}
