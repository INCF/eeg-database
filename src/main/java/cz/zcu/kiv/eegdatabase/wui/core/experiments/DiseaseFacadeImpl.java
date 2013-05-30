package cz.zcu.kiv.eegdatabase.wui.core.experiments;

import cz.zcu.kiv.eegdatabase.data.pojo.Disease;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 16.4.13
 * Time: 10:28
 */
public class DiseaseFacadeImpl implements DiseaseFacade {
    DiseaseService diseaseService;

    @Required
    public void setDiseaseService(DiseaseService diseaseService) {
        this.diseaseService = diseaseService;
    }

    @Override
    public Integer create(Disease newInstance) {
        return diseaseService.create(newInstance);
    }

    @Override
    public Disease read(Integer id) {
        return diseaseService.read(id);
    }

    @Override
    public List<Disease> readByParameter(String parameterName, Object parameterValue) {
        return diseaseService.readByParameter(parameterName, parameterValue);  //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public void update(Disease transientObject) {
        diseaseService.update(transientObject);//To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void delete(Disease persistentObject) {
        diseaseService.delete(persistentObject);//To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Disease> getAllRecords() {
        return diseaseService.getAllRecords();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Disease> getRecordsAtSides(int first, int max) {
        return diseaseService.getRecordsAtSides(first, max);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getCountRecords() {
        return diseaseService.getCountRecords();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Disease> getUnique(Disease example) {
        return diseaseService.getUnique(example);
    }

    @Override
    public boolean existsDisease(String name) {
        return diseaseService.existsDisease(name);
    }
}
