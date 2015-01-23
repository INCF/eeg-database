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

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import cz.zcu.kiv.eegdatabase.data.dao.DigitizationDao;
import cz.zcu.kiv.eegdatabase.data.dao.ExperimentDao;
import cz.zcu.kiv.eegdatabase.data.dao.ExperimentPackageConnectionDao;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.dao.HardwareDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.SimpleArtifactDao;
import cz.zcu.kiv.eegdatabase.data.dao.SimpleDiseaseDao;
import cz.zcu.kiv.eegdatabase.data.dao.SimplePharmaceuticalDao;
import cz.zcu.kiv.eegdatabase.data.dao.SimpleProjectTypeDao;
import cz.zcu.kiv.eegdatabase.data.dao.SimpleSoftwareDao;
import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Disease;
import cz.zcu.kiv.eegdatabase.data.pojo.ElectrodeConf;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Pharmaceutical;
import cz.zcu.kiv.eegdatabase.data.pojo.ProjectType;
import cz.zcu.kiv.eegdatabase.data.pojo.Software;
import cz.zcu.kiv.eegdatabase.data.pojo.SubjectGroup;
import cz.zcu.kiv.eegdatabase.logic.controller.search.SearchRequest;

public class ExperimentsServiceImpl implements ExperimentsService {
    
    protected Log log = LogFactory.getLog(getClass());

    private ExperimentDao experimentDao;
    private HardwareDao hardwareDao;
    private SimpleSoftwareDao softwareDao;
    private PersonDao personDao;
    private SimpleDiseaseDao diseaseDao;
    private SimplePharmaceuticalDao pharmDao;
    private SimpleProjectTypeDao projectTypesDao;
    private ExperimentPackageConnectionDao experimentPackageConnectionDao;
    
    private SimpleArtifactDao artifactDao;
    private DigitizationDao digitizationDao;
    private GenericDao<SubjectGroup, Integer> subjectGroupDao;
    private GenericDao<ElectrodeConf, Integer> electrodeConfDao;

    @Required
    public void setExperimentDao(ExperimentDao experimentDao) {
        this.experimentDao = experimentDao;
    }

    @Required
    public void setExperimentPackageConnectionDao(ExperimentPackageConnectionDao experimentPackageConnectionDao) {
        this.experimentPackageConnectionDao = experimentPackageConnectionDao;
    }
    
    @Required
    public void setHardwareDao(HardwareDao hardwareDao) {
        this.hardwareDao = hardwareDao;
    }
    
    @Required
    public void setSoftwareDao(SimpleSoftwareDao softwareDao) {
        this.softwareDao = softwareDao;
    }
    
    @Required
    public void setProjectTypesDao(SimpleProjectTypeDao projectTypesDao) {
        this.projectTypesDao = projectTypesDao;
    }
    
    @Required
    public void setPharmDao(SimplePharmaceuticalDao pharmDao) {
        this.pharmDao = pharmDao;
    }
    
    @Required
    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }
    
    @Required
    public void setDiseaseDao(SimpleDiseaseDao diseaseDao) {
        this.diseaseDao = diseaseDao;
    }
    
    @Required
    public void setSubjectGroupDao(GenericDao<SubjectGroup, Integer> subjectGroupDao) {
        this.subjectGroupDao = subjectGroupDao;
    }
    
    @Required
    public void setElectrodeConfDao(GenericDao<ElectrodeConf, Integer> electrodeConfDao) {
        this.electrodeConfDao = electrodeConfDao;
    }
    
    @Required
    public void setDigitizationDao(DigitizationDao digitizationDao) {
        this.digitizationDao = digitizationDao;
    }
    
    @Required
    public void setArtifactDao(SimpleArtifactDao artifactDao) {
        this.artifactDao = artifactDao;
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
        
        Experiment experiment = experimentDao.getExperimentForDetail(experimentId);
        
        return experiment;
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
    public List<Experiment> getExperimentSearchResults(List<SearchRequest> requests, int personId) {
        return experimentDao.getExperimentSearchResults(requests, personId);
    }

    @Override
    @Transactional
    public Integer create(Experiment experiment) {
        
        for(Hardware tmp : experiment.getHardwares()) 
            hardwareDao.read(tmp.getHardwareId()).getExperiments().add(experiment);
        
        for(Disease tmp : experiment.getDiseases())
            diseaseDao.read(tmp.getDiseaseId()).getExperiments().add(experiment);
        
        for(Person tmp : experiment.getPersons())
            personDao.read(tmp.getPersonId()).getExperiments().add(experiment);
        
        for(Pharmaceutical tmp : experiment.getPharmaceuticals())
            pharmDao.read(tmp.getPharmaceuticalId()).getExperiments().add(experiment);
        
        for(ProjectType tmp : experiment.getProjectTypes())
            projectTypesDao.read(tmp.getProjectTypeId()).getExperiments().add(experiment);
        
        for(Software tmp : experiment.getSoftwares())
            softwareDao.read(tmp.getSoftwareId()).getExperiments().add(experiment);
        if (experiment.getArtifact() == null) {
            experiment.setArtifact(artifactDao.read(1));
        }
        if (experiment.getSubjectGroup() == null) {
            experiment.setSubjectGroup(subjectGroupDao.read(1));
        }
        if (experiment.getDigitization() == null) {
            experiment.setDigitization(digitizationDao.read(1));
        }
        if (experiment.getElectrodeConf() == null) {
            experiment.setElectrodeConf(electrodeConfDao.read(1));
        }
        
        experimentDao.create(experiment);

        return experiment.getExperimentId();
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
    public void update(Experiment experiment) {
        
        Experiment updated = read(experiment.getExperimentId());
        
        // remove old objects from both side of relation
        // first remove from side of the objects
        for(Hardware tmp : updated.getHardwares())
            hardwareDao.read(tmp.getHardwareId()).getExperiments().remove(updated);
        
        for(Disease tmp : updated.getDiseases())
            diseaseDao.read(tmp.getDiseaseId()).getExperiments().remove(updated);
        
        for(Person tmp : updated.getPersons())
            personDao.read(tmp.getPersonId()).getExperiments().remove(updated);
        
        for(Pharmaceutical tmp : updated.getPharmaceuticals())
            pharmDao.read(tmp.getPharmaceuticalId()).getExperiments().remove(updated);
        
        for(ProjectType tmp : updated.getProjectTypes())
            projectTypesDao.read(tmp.getProjectTypeId()).getExperiments().remove(updated);
        
        for(Software tmp : updated.getSoftwares())
            softwareDao.read(tmp.getSoftwareId()).getExperiments().remove(updated);
        // then remove from experiment sides
        updated.getHardwares().clear();
        updated.getSoftwares().clear();
        updated.getDiseases().clear();
        updated.getPersons().clear();
        updated.getPharmaceuticals().clear();
        updated.getProjectTypes().clear();
        
        // add new objects for experiments and create relation from both side
        
        for(Hardware tmp : experiment.getHardwares())
            hardwareDao.read(tmp.getHardwareId()).getExperiments().add(updated);
        
        for(Disease tmp : experiment.getDiseases())
            diseaseDao.read(tmp.getDiseaseId()).getExperiments().add(updated);
        
        for(Person tmp : experiment.getPersons())
            personDao.read(tmp.getPersonId()).getExperiments().add(updated);
        
        for(Pharmaceutical tmp : experiment.getPharmaceuticals())
            pharmDao.read(tmp.getPharmaceuticalId()).getExperiments().add(updated);
        
        for(ProjectType tmp : experiment.getProjectTypes())
            projectTypesDao.read(tmp.getProjectTypeId()).getExperiments().add(updated);
        
        for(Software tmp : experiment.getSoftwares())
            softwareDao.read(tmp.getSoftwareId()).getExperiments().add(updated);
        
        updated.setHardwares(experiment.getHardwares());
        updated.setSoftwares(experiment.getSoftwares());
        updated.setDiseases(experiment.getDiseases());
        updated.setPersons(experiment.getPersons());
        updated.setPharmaceuticals(experiment.getPharmaceuticals());
        updated.setProjectTypes(experiment.getProjectTypes());
        
        updated.setScenario(experiment.getScenario());
        updated.setPersonBySubjectPersonId(experiment.getPersonBySubjectPersonId());
        updated.setEndTime(experiment.getEndTime());
        updated.setStartTime(experiment.getStartTime());
        updated.setResearchGroup(experiment.getResearchGroup());
        updated.setPrivateExperiment(experiment.isPrivateExperiment());
        updated.setTemperature(experiment.getTemperature());
        updated.setWeather(experiment.getWeather());
        updated.setEnvironmentNote(experiment.getEnvironmentNote());
        
        experimentDao.update(updated);
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

    @Override
    @Transactional
    public void changePrice(Experiment experiment) {
        experimentDao.update(experiment);
        
    }

}
