package cz.zcu.kiv.eegdatabase.wui.core.common;

import cz.zcu.kiv.eegdatabase.data.pojo.Software;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Prasek
 * Date: 16.4.13
 * Time: 13:41
 * To change this template use File | Settings | File Templates.
 */
public class SoftwareFacadeImpl implements SoftwareFacade{

    protected Log log = LogFactory.getLog(getClass());

    SoftwareService service;

    @Required
    public void setService(SoftwareService service) {
        this.service = service;
    }

    @Override
    public void createDefaultRecord(Software software) {
        service.createDefaultRecord(software);
    }

    @Override
    public boolean isDefault(int id) {
        return service.isDefault(id);
    }

    @Override
    public boolean canSaveDefaultTitle(String title, int swId) {
        return service.canSaveDefaultTitle(title, swId);
    }

    @Override
    public Integer create(Software newInstance) {
        return service.create(newInstance);
    }

    @Override
    public Software read(Integer id) {
        return service.read(id);
    }

    @Override
    public List<Software> readByParameter(String parameterName, int parameterValue) {
        return service.readByParameter(parameterName, parameterValue);
    }

    @Override
    public List<Software> readByParameter(String parameterName, String parameterValue) {
        return service.readByParameter(parameterName, parameterValue);
    }

    @Override
    public void update(Software transientObject) {
        service.update(transientObject);
    }

    @Override
    public void delete(Software persistentObject) {
        service.delete(persistentObject);
    }

    @Override
    public List<Software> getAllRecords() {
        return service.getAllRecords();
    }

    @Override
    public List<Software> getRecordsAtSides(int first, int max) {
        return service.getRecordsAtSides(first, max);
    }

    @Override
    public int getCountRecords() {
        return service.getCountRecords();
    }
}
