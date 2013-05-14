package cz.zcu.kiv.eegdatabase.wui.core.common;

import cz.zcu.kiv.eegdatabase.data.pojo.ProjectType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Prasek
 * Date: 16.4.13
 * Time: 15:31
 * To change this template use File | Settings | File Templates.
 */
public class ProjectTypeFacadeImpl implements ProjectTypeFacade{

    protected Log log = LogFactory.getLog(getClass());

    ProjectTypeService service;

    @Required
    public void setService(ProjectTypeService service) {
        this.service = service;
    }

    @Override
    public Integer create(ProjectType newInstance) {
        return service.create(newInstance);
    }

    @Override
    public ProjectType read(Integer id) {
        return service.read(id);
    }

    @Override
    public List<ProjectType> readByParameter(String parameterName, int parameterValue) {
        return service.readByParameter(parameterName, parameterValue);
    }

    @Override
    public List<ProjectType> readByParameter(String parameterName, String parameterValue) {
        return service.readByParameter(parameterName, parameterValue);
    }

    @Override
    public void update(ProjectType transientObject) {
        service.update(transientObject);
    }

    @Override
    public void delete(ProjectType persistentObject) {
        service.delete(persistentObject);
    }

    @Override
    public List<ProjectType> getAllRecords() {
        return service.getAllRecords();
    }

    @Override
    public List<ProjectType> getRecordsAtSides(int first, int max) {
        return service.getRecordsAtSides(first, max);
    }

    @Override
    public int getCountRecords() {
        return service.getCountRecords();
    }

    @Override
    public List<ProjectType> getUnique(ProjectType example) {
        return service.getUnique(example);
    }

    @Override
    public boolean canSaveTitle(String title) {
        return service.canSaveTitle(title);
    }
}
