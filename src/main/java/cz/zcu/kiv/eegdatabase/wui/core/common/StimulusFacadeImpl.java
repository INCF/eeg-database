package cz.zcu.kiv.eegdatabase.wui.core.common;

import cz.zcu.kiv.eegdatabase.data.pojo.Software;
import cz.zcu.kiv.eegdatabase.data.pojo.Stimulus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Matheo
 * Date: 16.4.13
 * Time: 19:37
 * To change this template use File | Settings | File Templates.
 */
public class StimulusFacadeImpl implements StimulusFacade {

    protected Log log = LogFactory.getLog(getClass());

    StimulusService service;

    @Required
    public void setService(StimulusService service) {
        this.service = service;
    }

    @Override
    public Integer create(Stimulus newInstance) {
        return service.create(newInstance);
    }

    @Override
    public Stimulus read(Integer id) {
        return service.read(id);
    }

    @Override
    public List<Stimulus> readByParameter(String parameterName, int parameterValue) {
        return service.readByParameter(parameterName, parameterValue);
    }

    @Override
    public List<Stimulus> readByParameter(String parameterName, String parameterValue) {
        return service.readByParameter(parameterName, parameterValue);
    }

    @Override
    public void update(Stimulus transientObject) {
        service.update(transientObject);
    }

    @Override
    public void delete(Stimulus persistentObject) {
        service.delete(persistentObject);
    }

    @Override
    public List<Stimulus> getAllRecords() {
        return service.getAllRecords();
    }

    @Override
    public List<Stimulus> getRecordsAtSides(int first, int max) {
        return service.getRecordsAtSides(first, max);
    }

    @Override
    public int getCountRecords() {
        return service.getCountRecords();
    }

    @Override
    public boolean canSaveDescription(String description) {
        return service.canSaveDescription(description);
    }
}
