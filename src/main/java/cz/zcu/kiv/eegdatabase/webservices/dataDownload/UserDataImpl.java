package cz.zcu.kiv.eegdatabase.webservices.dataDownload;


import cz.zcu.kiv.eegdatabase.data.dao.*;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.webservices.dataDownload.wrappers.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.jws.WebService;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Web service providing user's data remotely.
 *
 * @author Petr Miko
 */
@WebService(endpointInterface = "cz.zcu.kiv.eegdatabase.webservices.dataDownload.UserDataService")
@SuppressWarnings("unchecked")
public class UserDataImpl implements UserDataService {

    /* necessary Dao objects*/
    private PersonDao personDao;
    private ExperimentDao experimentDao;
    private WeatherDao weatherDao;
    private ScenarioDao scenarioDao;
    private ResearchGroupDao researchGroupDao;
    private Log log = LogFactory.getLog(getClass());
    private HardwareDao hardwareDao;

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

    public boolean isServiceAvailable() {

        log.debug("User " + personDao.getLoggedPerson().getUsername() +
                " verified connection with dataDownload web service.");
        return true;
    }

    public List<WeatherInfo> getWeather(long oracleScn) {
        List<Weather> weathers = weatherDao.getRecordsNewerThan(oracleScn);
        List<WeatherInfo> whs = new LinkedList<WeatherInfo>();

        for (Weather weather : weathers) {
            WeatherInfo info = new WeatherInfo();
            info.setDescription(weather.getDescription());
            info.setTitle(weather.getTitle());
            info.setWeatherId(weather.getWeatherId());
            info.setScn(weather.getScn());

            whs.add(info);
        }

        return whs;
    }

    public List<ExperimentInfo> getExperiments(long oracleScn) {

        List<ExperimentInfo> exps = new LinkedList<ExperimentInfo>();
        List<Experiment> experiments;

        experiments = new LinkedList<Experiment>(experimentDao.getRecordsNewerThan(oracleScn, personDao.getLoggedPerson().getPersonId()));

        for (Experiment experiment : experiments) {

            ExperimentInfo info = new ExperimentInfo();
            info.setExperimentId(experiment.getExperimentId());
            info.setOwnerId(experiment.getPersonByOwnerId().getPersonId());
            info.setSubjectPersonId(experiment.getPersonBySubjectPersonId().getPersonId());
            info.setScenarioId(experiment.getScenario().getScenarioId());
            info.setStartTimeInMillis(experiment.getStartTime().getTime());
            info.setEndTimeInMillis(experiment.getEndTime().getTime());
            info.setWeatherId(experiment.getWeather().getWeatherId());
            info.setWeatherNote(experiment.getWeathernote());
            info.setPrivateFlag((experiment.isPrivateExperiment() ? 1 : 0));
            info.setResearchGroupId(experiment.getResearchGroup().getResearchGroupId());
            info.setTemperature(experiment.getTemperature());
            info.setTitle(experiment.getScenario().getTitle());
            info.setScn(experiment.getScn());

            exps.add(info);

        }

        log.debug("User " + personDao.getLoggedPerson().getUsername() + " retrieved experiment list.");

        return exps;
    }

    public List<ScenarioInfo> getScenarios(long oracleScn) {
        List<Scenario> scenarios = scenarioDao.getRecordsNewerThan(oracleScn);
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
            info.setScn(scenario.getScn());

            scens.add(info);
        }
        return scens;
    }

    public List<PersonInfo> getPeople(long oracleScn) {

        List<PersonInfo> people = new LinkedList<PersonInfo>();
        List<Person> peopleDb = personDao.getRecordsNewerThan(oracleScn);

        for (Person subject : peopleDb) {
            PersonInfo person = new PersonInfo();

            person.setPersonId(subject.getPersonId());
            person.setGender(subject.getGender());
            person.setGivenName(subject.getGivenname());
            person.setSurname(subject.getSurname());
            person.setScn(subject.getScn());

            if (subject.getDefaultGroup() != null)
                person.setDefaultGroupId(subject.getDefaultGroup().getResearchGroupId());
            else
                person.setDefaultGroupId(-1);

            people.add(person);
        }

        log.debug("User " + personDao.getLoggedPerson().getUsername() + " retrieved list of people.");
        return people;
    }

    public List<ResearchGroupInfo> getResearchGroups(long oracleScn) {
        List<ResearchGroup> rGroups = researchGroupDao.getRecordsNewerThan(oracleScn);
        List<ResearchGroupInfo> rgps = new LinkedList<ResearchGroupInfo>();

        for (ResearchGroup rGroup : rGroups) {
            ResearchGroupInfo info = new ResearchGroupInfo();

            info.setDescription(rGroup.getDescription());
            info.setResearchGroupId(rGroup.getResearchGroupId());
            info.setOwnerId(rGroup.getPerson().getPersonId());
            info.setTitle(rGroup.getTitle());
            info.setScn(rGroup.getScn());

            rgps.add(info);
        }
        return rgps;
    }

    public List<DataFileInfo> getDataFiles(long oracleScn) throws DataDownloadException {
        List<Experiment> experiments = experimentDao.getAllRecords();
        List<DataFileInfo> fileInformation = new LinkedList<DataFileInfo>();
        DataFileInfo info;
        List<DataFile> files;

        try {

            for (Experiment experiment : experiments) {

                files = experimentDao.getDataFilesWhereExpId(experiment.getExperimentId());

                for (DataFile file : files) {
                    if (file.getScn() > oracleScn) {
                        info = new DataFileInfo();

                        info.setExperimentId(file.getExperiment().getExperimentId());
                        info.setFileId(file.getDataFileId());
                        info.setFileLength(file.getFileContent().length());
                        info.setFileName(file.getFilename());
                        info.setMimeType(file.getMimetype());
                        info.setSamplingRate(file.getSamplingRate());
                        info.setScn(file.getScn());

                        fileInformation.add(info);
                    }
                }
            }

            log.debug("User " + personDao.getLoggedPerson().getUsername() + " retrieved list of data files.");
        } catch (SQLException e) {
            DataDownloadException exception = new DataDownloadException(e);
            log.error("User " + personDao.getLoggedPerson().getUsername() + " did NOT retrieve list of data files!");
            log.error(e.getMessage(), e);
            throw exception;
        }

        return fileInformation;
    }

    public List<HardwareInfo> getHardware(long oracleScn) {
        List<HardwareInfo> hardware = new ArrayList<HardwareInfo>();
        List<Hardware> hardwareDb = hardwareDao.getRecordsNewerThan(oracleScn);

        for(Hardware piece : hardwareDb){
            HardwareInfo info = new HardwareInfo();

            info.setDescription(piece.getDescription());
            info.setHardwareId(piece.getHardwareId());
            info.setScn(piece.getScn());
            info.setTitle(piece.getTitle());
            info.setType(piece.getType());

            hardware.add(info);
        }

        return hardware;
    }

    public DataHandler downloadDataFile(int dataFileId) throws DataDownloadException {

        List<DataFile> files = experimentDao.getDataFilesWhereId(dataFileId);
        DataFile file = files.get(0);

        DataSource rawData;
        try {
            final InputStream in = file.getFileContent().getBinaryStream();
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
            log.debug("User " + personDao.getLoggedPerson().getUsername() + " retrieved file " + dataFileId);
        } catch (SQLException e) {
            DataDownloadException exception = new DataDownloadException(e);
            log.error("User " + personDao.getLoggedPerson().getUsername() + " did NOT retrieve file " + dataFileId);
            log.error(e.getMessage(), e);
            throw exception;
        }

        return new DataHandler(rawData);
    }
}