package cz.zcu.kiv.eegdatabase.wui.core.experiments;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import cz.zcu.kiv.eegdatabase.data.dao.ExperimentDao;
import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.controller.search.SearchRequest;

public class ExperimentsServiceImpl implements ExperimentsService {
    
    protected Log log = LogFactory.getLog(getClass());

    ExperimentDao<Experiment, Integer> experimentDao;

    @Required
    public void setExperimentDao(ExperimentDao<Experiment, Integer> experimentDao) {
        this.experimentDao = experimentDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataFile> getDataFilesWhereExpId(int experimentId) {
        return experimentDao.getDataFilesWhereExpId(experimentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataFile> getDataFilesWhereId(int dataFileId) {
        return experimentDao.getDataFilesWhereId(dataFileId);
    }

    @Override
    @Transactional(readOnly = true)
    public Experiment getExperimentForDetail(int experimentId) {
        return experimentDao.getExperimentForDetail(experimentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Experiment> getExperimentsWhereOwner(Person person, int limit) {
        return experimentDao.getExperimentsWhereOwner(person, limit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Experiment> getExperimentsWhereOwner(Person person, int start, int limit) {
        return experimentDao.getExperimentsWhereOwner(person, start, limit);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountForExperimentsWhereOwner(Person loggedUser) {
        return experimentDao.getCountForExperimentsWhereOwner(loggedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Experiment> getExperimentsWhereSubject(Person person, int limit) {
        return experimentDao.getExperimentsWhereSubject(person, limit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Experiment> getExperimentsWhereSubject(Person person, int start, int limit) {
        return experimentDao.getExperimentsWhereSubject(person, start, limit);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountForExperimentsWhereSubject(Person person) {
        return experimentDao.getCountForExperimentsWhereSubject(person);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountForAllExperimentsForUser(Person person) {
        return experimentDao.getCountForAllExperimentsForUser(person);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Experiment> getAllExperimentsForUser(Person person, int start, int count) {
        return experimentDao.getAllExperimentsForUser(person, start, count);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Experiment> getRecordsNewerThan(long oracleScn, int personId) {
        return experimentDao.getRecordsNewerThan(oracleScn, personId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Experiment> getExperimentSearchResults(List<SearchRequest> requests, int personId) {
        return experimentDao.getExperimentSearchResults(requests, personId);
    }

    @Override
    @Transactional
    public Integer create(Experiment newInstance) {
        return experimentDao.create(newInstance);
    }

    @Override
    @Transactional(readOnly = true)
    public Experiment read(Integer id) {
        return experimentDao.read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Experiment> readByParameter(String parameterName, int parameterValue) {
        return experimentDao.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Experiment> readByParameter(String parameterName, String parameterValue) {
        return experimentDao.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional
    public void update(Experiment transientObject) {
        experimentDao.update(transientObject);
    }

    @Override
    @Transactional
    public void delete(Experiment persistentObject) {
        experimentDao.delete(persistentObject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Experiment> getAllRecords() {
        return experimentDao.getAllRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Experiment> getRecordsAtSides(int first, int max) {
        return experimentDao.getRecordsAtSides(first, max);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountRecords() {
        return experimentDao.getCountRecords();
    }

}
