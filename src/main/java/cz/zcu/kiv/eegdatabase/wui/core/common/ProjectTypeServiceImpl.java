package cz.zcu.kiv.eegdatabase.wui.core.common;

import cz.zcu.kiv.eegdatabase.data.dao.SimpleProjectTypeDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ProjectType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Prasek
 * Date: 16.4.13
 * Time: 15:25
 * To change this template use File | Settings | File Templates.
 */
public class ProjectTypeServiceImpl implements ProjectTypeService {

    protected Log log = LogFactory.getLog(getClass());

    SimpleProjectTypeDao projectTypeDao;

    public void setProjectTypeDao(SimpleProjectTypeDao projectTypeDao) {
        this.projectTypeDao = projectTypeDao;
    }

    @Override
    @Transactional
    public Integer create(ProjectType newInstance) {
        return projectTypeDao.create(newInstance);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectType read(Integer id) {
        return projectTypeDao.read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectType> readByParameter(String parameterName, int parameterValue) {
        return projectTypeDao.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectType> readByParameter(String parameterName, String parameterValue) {
        return projectTypeDao.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional
    public void update(ProjectType transientObject) {
        projectTypeDao.update(transientObject);
    }

    @Override
    @Transactional
    public void delete(ProjectType persistentObject) {
        projectTypeDao.delete(persistentObject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectType> getAllRecords() {
        return projectTypeDao.getAllRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectType> getRecordsAtSides(int first, int max) {
        return projectTypeDao.getRecordsAtSides(first, max);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountRecords() {
        return projectTypeDao.getCountRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectType> getUnique(ProjectType example) {
        return projectTypeDao.findByExample(example);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canSaveTitle(String title) {
        return projectTypeDao.canSaveTitle(title);
    }
}
