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
 *   ExperimentsFacadeImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.experiments;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.controller.search.SearchRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class ExperimentsFacadeImpl implements ExperimentsFacade {

    protected Log log = LogFactory.getLog(getClass());

    ExperimentsService service;

    @Required
    public void setService(ExperimentsService service) {
        this.service = service;
    }

    @Override
    public List<DataFile> getDataFilesWhereExpId(int experimentId) {
        return service.getDataFilesWhereExpId(experimentId);
    }

    @Override
    public List<DataFile> getDataFilesWhereId(int dataFileId) {
        return service.getDataFilesWhereId(dataFileId);
    }

    @Override
    public Experiment getExperimentForDetail(int experimentId) {
        
        return service.getExperimentForDetail(experimentId);
    }

    @Override
    public List<Experiment> getExperimentsWhereOwner(Person person, int limit) {
        return service.getExperimentsWhereOwner(person, limit);
    }

    @Override
    public List<Experiment> getExperimentsWhereOwner(Person person, int start, int limit) {
        return service.getExperimentsWhereOwner(person, start, limit);
    }

    @Override
    public int getCountForExperimentsWhereOwner(Person loggedUser) {
        return service.getCountForExperimentsWhereOwner(loggedUser);
    }

    @Override
    public List<Experiment> getExperimentsWhereSubject(Person person, int limit) {
        return service.getExperimentsWhereSubject(person, limit);
    }

    @Override
    public List<Experiment> getExperimentsWhereSubject(Person person, int start, int limit) {
        return service.getExperimentsWhereSubject(person, start, limit);
    }

    @Override
    public int getCountForExperimentsWhereSubject(Person person) {
        return service.getCountForExperimentsWhereSubject(person);
    }

    @Override
    public int getCountForAllExperimentsForUser(Person person) {
        return service.getCountForAllExperimentsForUser(person);
    }

    @Override
    public List<Experiment> getAllExperimentsForUser(Person person, int start, int count) {
        return service.getAllExperimentsForUser(person, start, count);
    }


    @Override
    public List<Experiment> getExperimentSearchResults(List<SearchRequest> requests, int personId) {
        return service.getExperimentSearchResults(requests, personId);
    }

    @Override
    public Integer create(Experiment newInstance) {
        return service.create(newInstance);
    }

    @Override
    public Experiment read(Integer id) {
        return service.read(id);
    }

    @Override
    public List<Experiment> readByParameter(String parameterName, Object parameterValue) {
        return service.readByParameter(parameterName, parameterValue);
    }

    @Override
    public void update(Experiment transientObject) {
        service.update(transientObject);
    }

    @Override
    public void delete(Experiment persistentObject) {
        service.delete(persistentObject);
    }

    @Override
    public List<Experiment> getAllRecords() {
        return service.getAllRecords();
    }

    @Override
    public List<Experiment> getRecordsAtSides(int first, int max) {
        return service.getRecordsAtSides(first, max);
    }

    @Override
    public int getCountRecords() {
        return service.getCountRecords();
    }

    @Override
    public List<Experiment> getUnique(Experiment example) {
        return service.getUnique(example);
    }

    @Override
    public List<Experiment> getExperimentsByPackage(int packageId) {
	return service.getExperimentsByPackage(packageId);
    }

    @Override
    public List<Experiment> getExperimentsWithoutPackage(ExperimentPackage pckg) {
	return service.getExperimentsWithoutPackage(pckg.getResearchGroup().getResearchGroupId(), pckg.getExperimentPackageId());
    }

	@Override
	public List<Experiment> getExperimentsWithoutPackage() {
		return service.getExperimentsWithoutPackage();
	}

    @Override
    public void changePrice(Experiment experiment) {
        service.changePrice(experiment);
    }
}
