package cz.zcu.kiv.eegdatabase.wui.core.common;

import cz.zcu.kiv.eegdatabase.data.pojo.Pharmaceutical;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Matheo
 * Date: 16.4.13
 * Time: 20:07
 * To change this template use File | Settings | File Templates.
 */
public class PharmaceuticalFacadeImpl implements PharmaceuticalFacade {

    protected Log log = LogFactory.getLog(getClass());

    PharmaceuticalService service;

    @Required
    public void setService(PharmaceuticalService service) {
        this.service = service;
    }

    @Override
    public Integer create(Pharmaceutical newInstance) {
        return service.create(newInstance);
    }

    @Override
    public Pharmaceutical read(Integer id) {
        return service.read(id);
    }

    @Override
    public List<Pharmaceutical> readByParameter(String parameterName, Object parameterValue) {
        return service.readByParameter(parameterName, parameterValue);
    }


    @Override
    public void update(Pharmaceutical transientObject) {
        service.update(transientObject);
    }

    @Override
    public void delete(Pharmaceutical persistentObject) {
        service.delete(persistentObject);
    }

    @Override
    public List<Pharmaceutical> getAllRecords() {
        return service.getAllRecords();
    }

    @Override
    public List<Pharmaceutical> getRecordsAtSides(int first, int max) {
        return service.getRecordsAtSides(first, max);
    }

    @Override
    public int getCountRecords() {
        return service.getCountRecords();
    }

    @Override
    public List<Pharmaceutical> getUnique(Pharmaceutical example) {
        return service.getUnique(example);
    }

    @Override
    public boolean canSaveTitle(String title){
        return service.canSaveTitle(title);
    }
}
