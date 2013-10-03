/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   ExperimentsServiceImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.experiments;

import cz.zcu.kiv.eegdatabase.data.dao.ExperimentDao;
import cz.zcu.kiv.eegdatabase.data.dao.ExperimentPackageConnectionDao;
import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.controller.search.SearchRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class ExperimentsServiceImpl implements ExperimentsService {
    
    protected Log log = LogFactory.getLog(getClass());

    ExperimentDao<Experiment, Integer> experimentDao;
    private ExperimentPackageConnectionDao experimentPackageConnectionDao;

    @Required
    public void setExperimentDao(ExperimentDao<Experiment, Integer> experimentDao) {
        this.experimentDao = experimentDao;
    }

    @Required
    public void setExperimentPackageConnectionDao(ExperimentPackageConnectionDao experimentPackageConnectionDao) {
	this.experimentPackageConnectionDao = experimentPackageConnectionDao;
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
    public List<Experiment> readByParameter(String parameterName, Object parameterValue) {
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

    @Override
    @Transactional(readOnly = true)
    public List<Experiment> getUnique(Experiment example) {
        return experimentDao.findByExample(example);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Experiment> getExperimentsByPackage(int packageId) {
		return this.experimentPackageConnectionDao.listExperimentsByPackage(packageId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Experiment> getExperimentsWithoutPackage(int researchGroupId, int packageId) {
		return this.experimentPackageConnectionDao.listExperimentsWithoutPackage(researchGroupId ,packageId);
    }

	@Override
	@Transactional(readOnly = true)
	public List<Experiment> getExperimentsWithoutPackage() {
		return experimentPackageConnectionDao.listExperimentsWithoutPackage();
	}

}
