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
 *   UserDataImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.webservices.datadownload;


import cz.zcu.kiv.eegdatabase.data.dao.*;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.webservices.datadownload.wrappers.*;
import java.io.ByteArrayInputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.jws.WebService;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.*;
import org.apache.commons.io.IOUtils;

/**
 * Web service providing user's data remotely.
 *
 * @author Petr Miko
 */
@WebService(endpointInterface = "cz.zcu.kiv.eegdatabase.webservices.datadownload.UserDataService")
public class UserDataImpl implements UserDataService {

    /* necessary Dao objects*/
    private static final Log log = LogFactory.getLog(UserDataImpl.class);

    private PersonDao personDao;
    private ExperimentDao experimentDao;
    private WeatherDao weatherDao;
    private ScenarioDao scenarioDao;
    private ResearchGroupDao researchGroupDao;
    private HardwareDao hardwareDao;
    private DataFileDao dataFileDao;
    private DigitizationDao digitizationDao;
    private GenericListDao<Artifact> artifactDao;
    private GenericDao<ElectrodeConf, Integer> electrodeConfDao;
    private GenericDao<SubjectGroup, Integer> subjectGroupDao;

    public void setExperimentDao(ExperimentDao experimentDao) {
        this.experimentDao = experimentDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    public void setScenarioDao(ScenarioDao scenarioDao) {
        this.scenarioDao = scenarioDao;
    }

    public void setWeatherDao(WeatherDao weatherDao) {
        this.weatherDao = weatherDao;
    }

    public void setResearchGroupDao(ResearchGroupDao researchGroupDao) {
        this.researchGroupDao = researchGroupDao;
    }

    public void setHardwareDao(HardwareDao hardwareDao) {
        this.hardwareDao = hardwareDao;
    }

    public void setDataFileDao(DataFileDao dataFileDao) {
        this.dataFileDao = dataFileDao;
    }

    public void setDigitizationDao(DigitizationDao digitizationDao) {
        this.digitizationDao = digitizationDao;
    }

    public void setSubjectGroupDao(GenericDao<SubjectGroup, Integer> subjectGroupDao) {
        this.subjectGroupDao = subjectGroupDao;
    }


    public void setElectrodeConfDao(GenericDao<ElectrodeConf, Integer> electrodeConfDao) {
        this.electrodeConfDao = electrodeConfDao;
    }

    public void setArtifactDao(GenericListDao<Artifact> artifactDao) {
        this.artifactDao = artifactDao;
    }

    public boolean isServiceAvailable() {

        log.debug("User " + personDao.getLoggedPerson().getEmail() +
                " verified connection with datadownload web service.");
        return true;
    }

		@Override
    public List<WeatherInfo> getWeather() {
        List<Weather> weathers = weatherDao.getAllRecords();
        List<WeatherInfo> whs = new LinkedList<WeatherInfo>();

        for (Weather weather : weathers) {
            WeatherInfo info = new WeatherInfo();
            info.setDescription(weather.getDescription());
            info.setTitle(weather.getTitle());
            info.setWeatherId(weather.getWeatherId());
            whs.add(info);
        }

        return whs;
    }

		@Override
    public List<ExperimentInfo> getExperiments() {

        List<ExperimentInfo> exps = new LinkedList<ExperimentInfo>();
        List<Experiment> experiments;

        experiments = new LinkedList<Experiment>(experimentDao.getRecordsNewerThan(personDao.getLoggedPerson().getPersonId()));

        for (Experiment experiment : experiments) {
            ExperimentInfo info = new ExperimentInfo();
            info.setExperimentId(experiment.getExperimentId());
            info.setOwnerId(experiment.getPersonByOwnerId().getPersonId());
            info.setSubjectPersonId(experiment.getPersonBySubjectPersonId().getPersonId());
            info.setScenarioId(experiment.getScenario().getScenarioId());
            info.setStartTimeInMillis(experiment.getStartTime().getTime());
            info.setEndTimeInMillis(experiment.getEndTime().getTime());
            info.setWeatherId(experiment.getWeather().getWeatherId());
            info.setWeatherNote(experiment.getEnvironmentNote());
            info.setPrivateFlag((experiment.isPrivateExperiment() ? 1 : 0));
            info.setResearchGroupId(experiment.getResearchGroup().getResearchGroupId());
            info.setTemperature(experiment.getTemperature());
            info.setTitle(experiment.getScenario().getTitle());

            DigitizationInfo digitizationInfo = new DigitizationInfo();
            try {
                Digitization digitization = experiment.getDigitization();
                if (digitization != null) {
                    digitizationInfo.setFilter(digitization.getFilter());
                    digitizationInfo.setGain(digitization.getGain());
                    digitizationInfo.setSamplingRate(digitization.getSamplingRate());
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

            info.setDigitizationInfo(digitizationInfo);

            List<Integer> hwIds = new ArrayList<Integer>();
            for (Hardware hw : experiment.getHardwares()) {
                hwIds.add(hw.getHardwareId());
            }

            info.setHwIds(hwIds);

            exps.add(info);

        }

        log.debug("User " + personDao.getLoggedPerson().getEmail() + " retrieved experiment list.");

        return exps;
    }

		@Override
    public List<ScenarioInfo> getScenarios() {
        List<Scenario> scenarios = scenarioDao.getAllRecords();
        List<ScenarioInfo> scens = new LinkedList<ScenarioInfo>();

        for (Scenario scenario : scenarios) {
            ScenarioInfo info = new ScenarioInfo();

            info.setDescription(scenario.getDescription());
            info.setMimeType(scenario.getMimetype());
            info.setOwnerId(scenario.getPerson().getPersonId());
            info.setResearchGroupId(scenario.getResearchGroup().getResearchGroupId());
            info.setScenarioId(scenario.getScenarioId());
            info.setScenarioLength(scenario.getScenarioLength());
            info.setScenarioName(scenario.getScenarioName());
            info.setTitle(scenario.getTitle());

            scens.add(info);
        }
        return scens;
    }

    public List<PersonInfo> getPeople() {

        List<PersonInfo> people = new LinkedList<PersonInfo>();
        List<Person> peopleDb = personDao.getAllRecords();

        for (Person subject : peopleDb) {
            PersonInfo person = new PersonInfo();

            person.setPersonId(subject.getPersonId());
            person.setGender(subject.getGender());
            person.setGivenName(subject.getGivenname());
            person.setSurname(subject.getSurname());

            if (subject.getDefaultGroup() != null)
                person.setDefaultGroupId(subject.getDefaultGroup().getResearchGroupId());
            else
                person.setDefaultGroupId(-1);

            people.add(person);
        }

        log.debug("User " + personDao.getLoggedPerson().getEmail() + " retrieved list of people.");
        return people;
    }

		@Override
    public List<ResearchGroupInfo> getResearchGroups() {
        List<ResearchGroup> rGroups = researchGroupDao.getAllRecords();
        List<ResearchGroupInfo> rgps = new LinkedList<ResearchGroupInfo>();

        for (ResearchGroup rGroup : rGroups) {
            ResearchGroupInfo info = new ResearchGroupInfo();

            info.setDescription(rGroup.getDescription());
            info.setResearchGroupId(rGroup.getResearchGroupId());
            info.setOwnerId(rGroup.getPerson().getPersonId());
            info.setTitle(rGroup.getTitle());

            rgps.add(info);
        }
        return rgps;
    }

		@Override
    public List<DataFileInfo> getDataFiles() throws UserDataServiceException {
        List<Experiment> experiments = experimentDao.getAllRecords();
        List<DataFileInfo> fileInformation = new LinkedList<DataFileInfo>();
        DataFileInfo info;
        List<DataFile> files;

            for (Experiment experiment : experiments) {

                files = experimentDao.getDataFilesWhereExpId(experiment.getExperimentId());

                for (DataFile file : files) {
                        info = new DataFileInfo();

                        info.setExperimentId(file.getExperiment().getExperimentId());
                        info.setFileId(file.getDataFileId());
                        info.setFileLength(file.getFileContent().length);
                        info.setFileName(file.getFilename());
                        info.setMimeType(file.getMimetype());

                        fileInformation.add(info);
                    }
            }

            log.debug("User " + personDao.getLoggedPerson().getEmail() + " retrieved list of data files.");

        return fileInformation;
    }

		@Override
    public List<HardwareInfo> getHardware() {
        List<HardwareInfo> hardware = new ArrayList<HardwareInfo>();
        List<Hardware> hardwareDb = hardwareDao.getAllRecords();

        for (Hardware piece : hardwareDb) {
            HardwareInfo info = new HardwareInfo();
            List<Integer> experimentIds = new ArrayList<Integer>();

            info.setDescription(piece.getDescription());
            info.setHardwareId(piece.getHardwareId());
            info.setTitle(piece.getTitle());
            info.setType(piece.getType());

            for (Experiment exp : piece.getExperiments()) {
                experimentIds.add(exp.getExperimentId());
            }
            info.setExperimentIds(experimentIds);

            hardware.add(info);
        }

        return hardware;
    }

    public DataHandler downloadDataFile(int dataFileId) throws UserDataServiceException {

        List<DataFile> files = experimentDao.getDataFilesWhereId(dataFileId);
        DataFile file = files.get(0);

        DataSource rawData;
            final InputStream in = new ByteArrayInputStream(file.getFileContent());
            rawData = new DataSource() {
                public String getContentType() {
                    return "application/octet-stream";
                }

                public InputStream getInputStream() throws IOException {
                    return in;
                }

                public String getName() {
                    return "application/octet-stream";
                }

                public OutputStream getOutputStream() throws IOException {
                    return null;
                }
            };
            log.debug("User " + personDao.getLoggedPerson().getEmail() + " retrieved file " + dataFileId);

        return new DataHandler(rawData);
    }

    @Override
    public int addOrUpdateDataFile(DataFileInfo dataFile, DataHandler inputData) throws UserDataServiceException {
        DataFile file;
        if (dataFile.isAdded()) {
            file = new DataFile();
        } else {
            file = (DataFile) experimentDao.getDataFilesWhereId(dataFile.getFileId()).get(0);
        }
        file.setExperiment((Experiment) experimentDao.read(dataFile.getExperimentId()));
        file.setDescription(dataFile.getDescription());
        file.setFilename(dataFile.getFileName());
        file.setMimetype(dataFile.getMimeType());

        try {
            if (inputData != null) {
                file.setFileContent( IOUtils.toByteArray(inputData.getInputStream()));
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new UserDataServiceException(e);
        }

        if (dataFile.isAdded()) {
            int fileId = dataFileDao.create(file);
            log.debug("User " + personDao.getLoggedPerson().getEmail() + " created new data file (primary key " + fileId + ").");
            return fileId;
        } else {
            dataFileDao.update(file);
            log.debug("User " + personDao.getLoggedPerson().getEmail() + " edited existing data file (primary key " + file.getDataFileId() + ").");
            return file.getDataFileId();
        }
    }

    @Override
    public int addOrUpdateExperiment(ExperimentInfo experiment) {
        Experiment exp;
        if (!experiment.isAdded()) {
            exp = (Experiment) experimentDao.read(experiment.getExperimentId());
        } else {
            exp = new Experiment();
            exp.setPersonByOwnerId(personDao.read(experiment.getOwnerId()));
            DigitizationInfo digitizationInfo = experiment.getDigitizationInfo();

            if (digitizationInfo != null) {
                if (digitizationInfo.getFilter() == null)
                    digitizationInfo.setFilter("NotKnown");
                Digitization digitization = digitizationDao.getDigitizationByParams(digitizationInfo.getSamplingRate(),
                        digitizationInfo.getGain(),
                        digitizationInfo.getFilter());

                if (digitization == null) {
                    digitization = new Digitization();
                    digitization.setSamplingRate(digitizationInfo.getSamplingRate());
                    digitization.setFilter(digitizationInfo.getFilter());
                    digitization.setGain(digitizationInfo.getGain());
                    digitizationDao.create(digitization);
                }
                exp.setDigitization(digitization);
            }
        }

        SubjectGroup subjectGroup = subjectGroupDao.read(experiment.getSubjectGroupId());
//        if nothing was read, load default group (ID 1)
        if (subjectGroup == null)
            subjectGroup = subjectGroupDao.read(1);

        exp.setSubjectGroup(subjectGroup);

        //TODO this will have to be handled in future
        exp.setArtifact(artifactDao.read(1));
        exp.setElectrodeConf(electrodeConfDao.read(1));

        exp.setStartTime(new Timestamp(experiment.getStartTimeInMillis()));
        exp.setEndTime(new Timestamp(experiment.getEndTimeInMillis()));
        exp.setPersonBySubjectPersonId(personDao.read(experiment.getSubjectPersonId()));
        exp.setScenario(scenarioDao.read(experiment.getScenarioId()));
        exp.setResearchGroup(researchGroupDao.read(experiment.getResearchGroupId()));
        exp.setWeather(weatherDao.read(experiment.getWeatherId()));
        exp.setEnvironmentNote(experiment.getWeatherNote());
        exp.setTemperature(experiment.getTemperature());

        if (experiment.getHwIds() != null) {
            Set<Hardware> hardwareTypes = new HashSet<Hardware>();
            for (Integer hwId : experiment.getHwIds()) {
                hardwareTypes.add(hardwareDao.read(hwId));
            }
            exp.setHardwares(hardwareTypes);
        }

        if (!experiment.isAdded()) {
            experimentDao.update(exp);
            log.debug("User " + personDao.getLoggedPerson().getEmail() + " edited existing experiment (primary key " + experiment.getExperimentId() + ").");
            return exp.getExperimentId();
        } else {
            int expId = (Integer) experimentDao.create(exp);
            log.debug("User " + personDao.getLoggedPerson().getEmail() + " saved new experiment (primary key " + experiment.getExperimentId() + ").");
            return expId;
        }

    }

    @Override
    public int addOrUpdateHardware(HardwareInfo hardware) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public int addOrUpdatePerson(PersonInfo person) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public int addOrUpdateResearchGroup(ResearchGroupInfo group) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public int addOrUpdateScenario(ScenarioInfo scenario) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public int addOfUpdateWeather(WeatherInfo weather) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
