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
 *   ClientServiceImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.webservices.client;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.activation.DataHandler;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlMimeType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.zcu.kiv.eegdatabase.data.dao.DataFileDao;
import cz.zcu.kiv.eegdatabase.data.dao.DigitizationDao;
import cz.zcu.kiv.eegdatabase.data.dao.EducationLevelDao;
import cz.zcu.kiv.eegdatabase.data.dao.ExperimentDao;
import cz.zcu.kiv.eegdatabase.data.dao.ExperimentOptParamDefDao;
import cz.zcu.kiv.eegdatabase.data.dao.FileMetadataParamDefDao;
import cz.zcu.kiv.eegdatabase.data.dao.GenericDao;
import cz.zcu.kiv.eegdatabase.data.dao.HardwareDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonOptParamDefDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.dao.ScenarioDao;
import cz.zcu.kiv.eegdatabase.data.dao.SimpleGenericDao;
import cz.zcu.kiv.eegdatabase.data.dao.WeatherDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Artifact;
import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Digitization;
import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;
import cz.zcu.kiv.eegdatabase.data.pojo.ElectrodeConf;
import cz.zcu.kiv.eegdatabase.data.pojo.ElectrodeSystem;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamVal;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentOptParamValId;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamVal;
import cz.zcu.kiv.eegdatabase.data.pojo.FileMetadataParamValId;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamDef;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamVal;
import cz.zcu.kiv.eegdatabase.data.pojo.PersonOptParamValId;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroupMembership;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroupMembershipId;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.data.pojo.SubjectGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.DataFileInfo;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.EducationLevelInfo;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.ExperimentInfo;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.ExperimentOptParamDefInfo;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.ExperimentOptParamValInfo;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.FileMetadataParamDefInfo;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.FileMetadataParamValInfo;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.HardwareInfo;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.PersonInfo;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.PersonOptParamDefInfo;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.PersonOptParamValInfo;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.ResearchGroupInfo;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.ResearchGroupMembershipInfo;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.ScenarioInfo;
import cz.zcu.kiv.eegdatabase.webservices.client.wrappers.WeatherInfo;
import org.apache.commons.io.IOUtils;
import org.hibernate.Hibernate;

/**
 * @author František Liška
 */
@WebService(endpointInterface = "cz.zcu.kiv.eegdatabase.webservices.client.ClientService")
public class ClientServiceImpl implements ClientService {
	private PersonDao personDao;
	private ResearchGroupDao researchGroupDao;
	private HardwareDao hardwareDao;
	private WeatherDao weatherDao;
	private EducationLevelDao educationLevelDao;
	private PersonOptParamDefDao personOptParamDefDao;
	private GenericDao<Artifact, Integer> artifactDao;
	private GenericDao<ElectrodeConf, Integer> electrodeConfDao;
	private GenericDao<ElectrodeSystem, Integer> electrodeSystemDao;
	private GenericDao<SubjectGroup, Integer> subjectGroupDao;
	private DigitizationDao digitizationDao;
	private DataFileDao dataFileDao;
	private ExperimentDao experimentDao;
	private GenericDao<PersonOptParamVal, PersonOptParamValId> personOptParamValDao;
	private GenericDao<ExperimentOptParamVal, ExperimentOptParamValId> experimentOptParamValDao;
	private GenericDao<FileMetadataParamVal, FileMetadataParamValId> fileMetadataParamValDao;
	private GenericDao<ResearchGroupMembership, ResearchGroupMembershipId> researchGroupMembershipDao;

	private ExperimentOptParamDefDao experimentOptParamDefDao;
	private FileMetadataParamDefDao fileMetadataParamDefDao;
	private ScenarioDao scenarioDao;
	private static final Log log = LogFactory.getLog(ClientServiceImpl.class);

	@Override
	public boolean isServiceAvailable() {
		log.debug("User " + personDao.getLoggedPerson().getEmail() + " verified connection with client web service.");
		return true;
	}

	@Override
	public int addHardware(HardwareInfo info) {
		Hardware h = new Hardware();
		h.setDefaultNumber(info.getDefaultNumber());
		h.setDescription(info.getDescription());
		h.setTitle(info.getTitle());
		h.setType(info.getType());
		int newId = hardwareDao.create(h);
		for (int groupId : info.getResearchGroupIdList()) {
			ResearchGroup r = researchGroupDao.read(groupId);
			hardwareDao.createGroupRel(h, r);
		}
		return newId;
	}

	@Override
	public int addWeather(WeatherInfo info) {
		Weather w = new Weather();
		w.setDefaultNumber(info.getDefaultNumber());
		w.setDescription(info.getDescription());
		w.setTitle(info.getTitle());
		int newId = weatherDao.create(w);
		for (int groupId : info.getResearchGroupIdList()) {
			ResearchGroup r = researchGroupDao.read(groupId);
			weatherDao.createGroupRel(w, r);
		}
		return newId;
	}

	@Override
	public int addResearchGroup(ResearchGroupInfo info) {
		ResearchGroup r = new ResearchGroup();
		r.setTitle(info.getTitle());
		r.setDescription(info.getDescription());
		Person p = personDao.getLoggedPerson();
		r.setPerson(p);
		int newId = researchGroupDao.create(r);
		p.getResearchGroups().add(r);
		personDao.update(p);
		return newId;
	}

	@Override
	public void addResearchGroupMembership(ResearchGroupMembershipInfo info) {
		ResearchGroupMembershipId id = new ResearchGroupMembershipId();
		ResearchGroupMembership membership = new ResearchGroupMembership();
		Person p = personDao.read(info.getId().getPersonId());
		ResearchGroup r = researchGroupDao.read(info.getId().getResearchGroupId());
		id.setPersonId(p.getPersonId());
		id.setResearchGroupId(r.getResearchGroupId());
		membership.setId(id);
		membership.setAuthority(info.getAuthority());
		membership.setPerson(p);
		membership.setResearchGroup(r);
		researchGroupMembershipDao.create(membership);
		p.getResearchGroupMemberships().add(membership);
		r.getResearchGroupMemberships().add(membership);
		personDao.update(p);
		researchGroupDao.update(r);
	}

	@Override
	public int addPerson(PersonInfo info) {
		int newId;
		if (personDao.usernameExists(info.getUsername())) {
			newId = personDao.getPerson(info.getUsername()).getPersonId();
		} else {
			Person p = new Person();
			p.setUsername(info.getUsername());
			p.setGender(info.getGender());
			p.setNote(info.getNote());
			p.setSurname(info.getSurname());
			p.setGivenname(info.getGivenname());
			p.setLaterality(info.getLaterality());
			p.setPhoneNumber(info.getPhoneNumber());
			p.setDateOfBirth(new Timestamp(info.getDateOfBirthInMillis()));
			EducationLevel e = educationLevelDao.read(info.getEducationLevelId());
			p.setEducationLevel(e);
			newId = personDao.create(p);
			e.getPersons().add(p);
			educationLevelDao.update(e);
		}
		return newId;
	}

	@Override
	public int addScenario(ScenarioInfo info, @XmlMimeType("application/octet-stream") DataHandler inputData) throws ClientServiceException {
		Scenario s = new Scenario();
		s.setDescription(info.getDescription());
		Person p = personDao.read(info.getOwnerPersonId());
		s.setPerson(p);
		ResearchGroup r = researchGroupDao.read(info.getResearchGroupId());
		s.setResearchGroup(r);
		s.setPrivateScenario(info.isPrivateScenario());
		s.setScenarioLength(info.getScenarioLength());
		s.setTitle(info.getTitle());
		try {
			if (inputData != null) {
				s.setMimetype(info.getMimetype());
				s.setScenarioName(info.getScenarioName());
				Blob blob = dataFileDao.createBlob(inputData.getInputStream(), (int) info.getFileLength());
			}
		} catch (IOException ex) {
			log.error(ex.getMessage(), ex);
			throw new ClientServiceException(ex);
		}
		int scenarioId = scenarioDao.create(s);
		log.debug("User " + personDao.getLoggedPerson().getEmail() + " created new Scenario (primary key " + scenarioId + ").");
		p.getScenarios().add(s);
		personDao.update(p);
		r.getScenarios().add(s);
		researchGroupDao.update(r);
		return scenarioId;
	}

	@Override
	public int addDataFile(DataFileInfo info, DataHandler inputData) throws ClientServiceException {
		DataFile file = new DataFile();
		Experiment e = experimentDao.read(info.getExperimentId());
		file.setExperiment(e);
		file.setDescription(info.getDescription());
		file.setFilename(info.getFilename());
		file.setMimetype(info.getMimetype());

		try {
			if (inputData != null) {

                file.setFileContent(Hibernate.getLobCreator(personDao.getSessionFactory().getCurrentSession()).
                        createBlob(inputData.getInputStream(), info.getFileLength()));
			}
		} catch (IOException ex) {
			log.error(ex.getMessage(), ex);
			throw new ClientServiceException(ex);
		}

		int fileId = dataFileDao.create(file);
		e.getDataFiles().add(file);
		experimentDao.update(e);
		log.debug("User " + personDao.getLoggedPerson().getEmail() + " created new data file (primary key " + fileId + ").");
		return fileId;
	}

	@Override
	public int addPersonOptParamDef(PersonOptParamDefInfo info) {
		PersonOptParamDef p = new PersonOptParamDef();
		p.setDefaultNumber(info.getDefaultNumber());
		p.setParamName(info.getParamName());
		p.setParamDataType(info.getParamDataType());
		int newId = personOptParamDefDao.create(p);
		for (int groupId : info.getResearchGroupIdList()) {
			ResearchGroup r = researchGroupDao.read(groupId);
			personOptParamDefDao.createGroupRel(p, r);
		}
		return newId;
	}

	@Override
	public void addPersonOptParamVal(PersonOptParamValInfo info) {
		PersonOptParamValId id = new PersonOptParamValId(info.getId().getPersonId(), info.getId().getPersonAdditionalParamId());
		PersonOptParamVal val = new PersonOptParamVal();
		val.setId(id);
		val.setPerson(personDao.read(id.getPersonId()));
		val.setPersonOptParamDef(personOptParamDefDao.read(id.getPersonAdditionalParamId()));
		val.setParamValue(info.getParamValue());
		personOptParamValDao.create(val);
	}

	@Override
	public int addExperiment(ExperimentInfo info) {
		Experiment experiment = new Experiment();
		try {
			log.debug("Client Service - adding new measuration");
			log.debug("Creating new Measuration object");
			log.debug("Setting the owner to the logged user.");
			Person owner = personDao.read(info.getOwnerPersonId());
			experiment.setPersonByOwnerId(owner);

			log.debug("Setting the group, which is the new experiment being added into.");
			ResearchGroup researchGroup = researchGroupDao.read(info.getResearchGroupId());
			researchGroup.setResearchGroupId(info.getResearchGroupId());
			experiment.setResearchGroup(researchGroup);

			Weather weather = weatherDao.read(info.getWeatherId());
			weather.setWeatherId(info.getWeatherId());
			experiment.setWeather(weather);

			log.debug("Setting Scenario object - ID " + info.getScenarioId());
			Scenario scenario = scenarioDao.read(info.getScenarioId());
			scenario.setScenarioId(info.getScenarioId());
			experiment.setScenario(scenario);

			log.debug("Setting Person object (measured person) - ID " + info.getSubjectPersonId());
			Person subjectPerson = personDao.read(info.getSubjectPersonId());
			subjectPerson.setPersonId(info.getSubjectPersonId());
			experiment.setPersonBySubjectPersonId(subjectPerson);

			experiment.setStartTime(new Timestamp(info.getStartTimeInMillis()));
			log.debug("Setting start date - " + info.getStartTimeInMillis());

			experiment.setEndTime(new Timestamp(info.getEndTimeInMillis()));
			log.debug("Setting end date - " + info.getEndTimeInMillis());

			log.debug("Setting the temperature - " + info.getTemperature());
			experiment.setTemperature(info.getTemperature());

			log.debug("Setting the weather note - " + info.getWeatherNote());
			experiment.setEnvironmentNote(info.getWeatherNote());

			log.debug("Started setting the Hardware objects");
			List<Integer> hwList = info.getHwIds();
			Set<Hardware> hardwareSet = new HashSet<Hardware>();
			for (int hardwareId : hwList) {
				Hardware tempHardware = hardwareDao.read(hardwareId);
				hardwareSet.add(tempHardware);
				tempHardware.getExperiments().add(experiment);
				log.debug("Added Hardware object - ID " + hardwareId);
			}
			log.debug("Setting Hardware list to Measuration object");
			experiment.setHardwares(hardwareSet);

			Digitization digitization = new Digitization();
			digitization.setFilter("Unknown");
			digitization.setGain(1);
			digitization.setSamplingRate(1);
			digitizationDao.create(digitization);
			// TODO digitization setters
			experiment.setDigitization(digitization);

			SubjectGroup subjectGroup = new SubjectGroup();
			subjectGroup.setTitle("Unknown");
			subjectGroup.setDescription("Unknown");
			// TODO subject group setters
			subjectGroupDao.create(subjectGroup);
			experiment.setSubjectGroup(subjectGroup);

			Artifact artifact = new Artifact();
			artifact.setCompensation("Unknown");
			artifact.setRejectCondition("Unknown");
			artifactDao.create(artifact);
			// TODO artifact setters
			experiment.setArtifact(artifact);

			ElectrodeSystem system = new ElectrodeSystem();
			system.setDescription("Unknown");
			system.setTitle("Unknown");
			// TODO electrode system setters
			electrodeSystemDao.create(system);

			ElectrodeConf conf = new ElectrodeConf();
			conf.setImpedance(1);
			conf.setElectrodeSystem(system);
			electrodeConfDao.create(conf);
			// TODO conf setters
			experiment.setElectrodeConf(conf);

			log.debug("Setting private/public access");
			experiment.setPrivateExperiment(info.isPrivateExperiment());

			log.debug("Saving the Measuration object to database using DAO - create()");
			return experimentDao.create(experiment);
		} catch (NullPointerException n) {
			n.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return -1;
	}

	@Override
	public int addExperimentOptParamDef(ExperimentOptParamDefInfo info) {
		ExperimentOptParamDef e = new ExperimentOptParamDef();
		e.setDefaultNumber(info.getDefaultNumber());
		e.setParamName(info.getParamName());
		e.setParamDataType(info.getParamDataType());
		int newId = experimentOptParamDefDao.create(e);
		for (int groupId : info.getResearchGroupIdList()) {
			ResearchGroup r = researchGroupDao.read(groupId);
			experimentOptParamDefDao.createGroupRel(e, r);
		}
		return newId;
	}

	@Override
	public void addExperimentOptParamVal(ExperimentOptParamValInfo info) {
		ExperimentOptParamValId id = new ExperimentOptParamValId(info.getId().getExperimentId(), info.getId().getExperimentOptParamDefId());
		ExperimentOptParamVal val = new ExperimentOptParamVal();
		val.setId(id);
		val.setExperiment(experimentDao.read(id.getExperimentId()));
		val.setExperimentOptParamDef(experimentOptParamDefDao.read(id.getExperimentOptParamDefId()));
		val.setParamValue(info.getParamValue());
		experimentOptParamValDao.create(val);
	}

	@Override
	public int addFileMetadataParamDef(FileMetadataParamDefInfo info) {
		FileMetadataParamDef f = new FileMetadataParamDef();
		f.setDefaultNumber(info.getDefaultNumber());
		f.setParamName(info.getParamName());
		f.setParamDataType(info.getParamDataType());
		int newId = fileMetadataParamDefDao.create(f);
		for (int groupId : info.getResearchGroupIdList()) {
			ResearchGroup r = researchGroupDao.read(groupId);
			fileMetadataParamDefDao.createGroupRel(f, r);
		}
		return newId;
	}

	@Override
	public void addFileMetadataParamVal(FileMetadataParamValInfo info) {
		FileMetadataParamValId id = new FileMetadataParamValId(info.getId().getFileMetadataParamDefId(), info.getId().getDataFileId());
		FileMetadataParamVal val = new FileMetadataParamVal();
		val.setId(id);
		val.setDataFile(dataFileDao.read(id.getDataFileId()));
		val.setFileMetadataParamDef(fileMetadataParamDefDao.read(id.getFileMetadataParamDefId()));
		val.setMetadataValue(info.getMetadataValue());
		fileMetadataParamValDao.create(val);
	}

	public List<PersonInfo> getPersonList() {
		List<PersonInfo> people = new LinkedList<PersonInfo>();
		List<Person> peopleDb = personDao.getAllRecords();
		for (Person p : peopleDb) {
			PersonInfo i = new PersonInfo();
			i.setPersonId(p.getPersonId());
			i.setGivenname(p.getGivenname());
			i.setSurname(p.getSurname());
			if (p.getDateOfBirth() != null) {
				i.setDateOfBirthInMillis(p.getDateOfBirth().getTime());
			}
			i.setGender(p.getGender());
			i.setPhoneNumber(p.getPhoneNumber());
			if (p.getRegistrationDate() != null) {
				i.setRegistrationDateInMillis(p.getRegistrationDate().getTime());
			}
			i.setNote(p.getNote());
			i.setUsername(p.getUsername());
			i.setEducationLevelId(p.getEducationLevel().getEducationLevelId());
			i.setLaterality(p.getLaterality());
			people.add(i);
		}
		log.debug("User " + personDao.getLoggedPerson().getEmail() + " retrieved list of people.");
		return people;
	}

	@Override
	public List<ScenarioInfo> getScenarioList() {
		List<Scenario> scenarios = scenarioDao.getAllRecords();
		List<ScenarioInfo> scens = new LinkedList<ScenarioInfo>();

		for (Scenario scenario : scenarios) {
			ScenarioInfo info = new ScenarioInfo();

			info.setDescription(scenario.getDescription());
			info.setMimetype(scenario.getMimetype());
			info.setOwnerPersonId(scenario.getPerson().getPersonId());
			info.setResearchGroupId(scenario.getResearchGroup().getResearchGroupId());
			info.setScenarioId(scenario.getScenarioId());
			info.setScenarioLength(scenario.getScenarioLength());
			info.setScenarioName(scenario.getScenarioName());
			info.setPrivateScenario(scenario.isPrivateScenario());
			info.setTitle(scenario.getTitle());

			scens.add(info);
		}
		return scens;
	}

	@Override
	public List<EducationLevelInfo> getEducationLevelList() {
		List<EducationLevelInfo> infoList = new LinkedList<EducationLevelInfo>();
		List<EducationLevel> levelsDb = educationLevelDao.getAllRecords();
		for (EducationLevel o : levelsDb) {
			EducationLevelInfo i = new EducationLevelInfo();
			i.setEducationLevelId(o.getEducationLevelId());
			i.setTitle(o.getTitle());
			i.setDefaultNumber(o.getDefaultNumber());
			infoList.add(i);
		}
		log.debug("User " + personDao.getLoggedPerson().getEmail() + " retrieved list of education levels.");
		return infoList;
	}

	@Override
	public List<PersonOptParamValInfo> getPersonOptParamValList() {
		List<PersonOptParamValInfo> infos = new LinkedList<PersonOptParamValInfo>();
		List<PersonOptParamVal> valuesDb = personOptParamValDao.getAllRecords();
		for (PersonOptParamVal o : valuesDb) {
			PersonOptParamValInfo i = new PersonOptParamValInfo();
			i.setId(o.getId());
			i.setParamValue(o.getParamValue());
			infos.add(i);
		}
		log.debug("User " + personDao.getLoggedPerson().getEmail() + " retrieved list of person optional parameters.");
		return infos;
	}

	@Override
	public ResearchGroupInfo getDefaultLists() {
		ResearchGroupInfo i = new ResearchGroupInfo();
		ResearchGroup r = new ResearchGroup();
		i.setResearchGroupId(-1);
		i.setTitle("default");
		i.setDescription("");
		// adding hardware
		List<Hardware> hardwareDb = hardwareDao.getDefaultRecords();
		for (Hardware h : hardwareDb) {
			HardwareInfo hi = new HardwareInfo();
			hi.setHardwareId(h.getHardwareId());
			hi.setTitle(h.getTitle());
			hi.setDescription(h.getDescription());
			hi.setType(h.getType());
			hi.setDefaultNumber(h.getDefaultNumber());
			i.getHardwares().add(hi);
		}

		// adding weather
		List<Weather> weatherDb = weatherDao.getDefaultRecords();
		for (Weather w : weatherDb) {
			WeatherInfo wi = new WeatherInfo();
			wi.setWeatherId(w.getWeatherId());
			wi.setTitle(w.getTitle());
			wi.setDescription(w.getDescription());
			wi.setDefaultNumber(w.getDefaultNumber());
			i.getWeathers().add(wi);
		}

		// adding optional parameter for people
		List<PersonOptParamDef> personParamDb = personOptParamDefDao.getDefaultRecords();
		for (PersonOptParamDef pp : personParamDb) {
			PersonOptParamDefInfo pi = new PersonOptParamDefInfo();
			pi.setParamName(pp.getParamName());
			pi.setPersonOptParamDefId(pp.getPersonOptParamDefId());
			pi.setDefaultNumber(pp.getDefaultNumber());
			pi.setParamDataType(pp.getParamDataType());
			i.getPersonOptParamDefInfos().add(pi);
		}

		// adding optional parameter for experiments
		List<ExperimentOptParamDef> expParamDb = experimentOptParamDefDao.getDefaultRecords();
		for (ExperimentOptParamDef ed : expParamDb) {
			ExperimentOptParamDefInfo ei = new ExperimentOptParamDefInfo();
			ei.setParamName(ed.getParamName());
			ei.setExperimentOptParamDefId(ed.getExperimentOptParamDefId());
			ei.setDefaultNumber(ed.getDefaultNumber());
			ei.setParamDataType(ed.getParamDataType());
			i.getExperimentOptParamDefInfos().add(ei);
		}

		// adding file metadata
		List<FileMetadataParamDef> fileMetadataDb = fileMetadataParamDefDao.getDefaultRecords();
		for (FileMetadataParamDef fd : fileMetadataDb) {
			FileMetadataParamDefInfo fi = new FileMetadataParamDefInfo();
			fi.setParamName(fd.getParamName());
			fi.setFileMetadataParamDefId(fd.getFileMetadataParamDefId());
			fi.setDefaultNumber(fd.getDefaultNumber());
			fi.setParamDataType(fd.getParamDataType());
			i.getFileMetadataParamDefInfos().add(fi);
		}
		return i;
	}

	@Override
	public List<ResearchGroupInfo> getResearchGroupList() {
		List<ResearchGroupInfo> groups = new LinkedList<ResearchGroupInfo>();
		List<ResearchGroup> groupsDb = researchGroupDao.getResearchGroupsWhereMember(personDao.getLoggedPerson());
		for (ResearchGroup r : groupsDb) {
			ResearchGroupInfo i = new ResearchGroupInfo();
			i.setResearchGroupId(r.getResearchGroupId());
			i.setTitle(r.getTitle());
			i.setDescription(r.getDescription());
			if (r.getPerson() != null) {
				i.setPersonOwner(r.getPerson().getPersonId());
			}
			// adding research group memberships
			List<ResearchGroupMembership> memberships = researchGroupMembershipDao.readByParameter("researchGroup.researchGroupId",
					r.getResearchGroupId());
			for (ResearchGroupMembership rm : memberships) {
				ResearchGroupMembershipInfo ri = new ResearchGroupMembershipInfo();
				ResearchGroupMembershipId id = new ResearchGroupMembershipId();
				id.setPersonId(rm.getId().getPersonId());
				id.setResearchGroupId(rm.getId().getResearchGroupId());
				ri.setAuthority(rm.getAuthority());
				ri.setId(id);
				i.getResearchGroupMembershipInfos().add(ri);
			}

			// adding hardware
			List<Hardware> hardwareDb = hardwareDao.getRecordsByGroup(r.getResearchGroupId());
			for (Hardware h : hardwareDb) {
				HardwareInfo hi = new HardwareInfo();
				hi.setHardwareId(h.getHardwareId());
				hi.setTitle(h.getTitle());
				hi.setDescription(h.getDescription());
				hi.setType(h.getType());
				hi.setDefaultNumber(0);
				i.getHardwares().add(hi);
			}

			// adding weather
			List<Weather> weatherDb = weatherDao.getRecordsByGroup(r.getResearchGroupId());
			for (Weather w : weatherDb) {
				WeatherInfo wi = new WeatherInfo();
				wi.setWeatherId(w.getWeatherId());
				wi.setTitle(w.getTitle());
				wi.setDescription(w.getDescription());
				wi.setDefaultNumber(0);
				i.getWeathers().add(wi);
			}

			// adding optional parameter for people
			List<PersonOptParamDef> personParamDb = personOptParamDefDao.getRecordsByGroup(r.getResearchGroupId());
			for (PersonOptParamDef pp : personParamDb) {
				PersonOptParamDefInfo pi = new PersonOptParamDefInfo();
				pi.setParamName(pp.getParamName());
				pi.setPersonOptParamDefId(pp.getPersonOptParamDefId());
				pi.setDefaultNumber(0);
				pi.setParamDataType(pi.getParamDataType());
				i.getPersonOptParamDefInfos().add(pi);
			}

			// adding optional parameter for experiments
			List<ExperimentOptParamDef> expParamDb = experimentOptParamDefDao.getRecordsByGroup(r.getResearchGroupId());
			for (ExperimentOptParamDef ed : expParamDb) {
				ExperimentOptParamDefInfo ei = new ExperimentOptParamDefInfo();
				ei.setParamName(ed.getParamName());
				ei.setExperimentOptParamDefId(ed.getExperimentOptParamDefId());
				ei.setDefaultNumber(0);
				ei.setParamDataType(ed.getParamDataType());
				i.getExperimentOptParamDefInfos().add(ei);
			}

			// adding file metadata
			List<FileMetadataParamDef> fileMetadataDb = fileMetadataParamDefDao.getRecordsByGroup(r.getResearchGroupId());
			for (FileMetadataParamDef fd : fileMetadataDb) {
				FileMetadataParamDefInfo fi = new FileMetadataParamDefInfo();
				fi.setParamName(fd.getParamName());
				fi.setFileMetadataParamDefId(fd.getFileMetadataParamDefId());
				fi.setDefaultNumber(0);
				fi.setParamDataType(fd.getParamDataType());
				i.getFileMetadataParamDefInfos().add(fi);
			}
			groups.add(i);
		}
		log.debug("User " + personDao.getLoggedPerson().getEmail() + " retrieved list of filled research groups.");
		return groups;
	}

	public PersonDao getPersonDao() {
		return personDao;
	}

	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}

	public ResearchGroupDao getResearchGroupDao() {
		return researchGroupDao;
	}

	public void setResearchGroupDao(ResearchGroupDao researchGroupDao) {
		this.researchGroupDao = researchGroupDao;
	}

	public void setHardwareDao(HardwareDao hardwareDao) {
		this.hardwareDao = hardwareDao;
	}

	public void setWeatherDao(WeatherDao weatherDao) {
		this.weatherDao = weatherDao;
	}

	public void setEducationLevelDao(EducationLevelDao educationLevelDao) {
		this.educationLevelDao = educationLevelDao;
	}

	public void setScenarioDao(ScenarioDao scenarioDao) {
		this.scenarioDao = scenarioDao;
	}

	public void setPersonOptParamDefDao(PersonOptParamDefDao personOptParamDefDao) {
		this.personOptParamDefDao = personOptParamDefDao;
	}

	public void setExperimentOptParamDefDao(ExperimentOptParamDefDao experimentOptParamDefDao) {
		this.experimentOptParamDefDao = experimentOptParamDefDao;
	}

	public void setFileMetadataParamDefDao(FileMetadataParamDefDao fileMetadataParamDefDao) {
		this.fileMetadataParamDefDao = fileMetadataParamDefDao;
	}

	public void setDataFileDao(DataFileDao dataFileDao) {
		this.dataFileDao = dataFileDao;
	}

	public void setExperimentDao(ExperimentDao experimentDao) {
		this.experimentDao = experimentDao;
	}

	public void setElectrodeConfDao(GenericDao<ElectrodeConf, Integer> electrodeConfDao) {
		this.electrodeConfDao = electrodeConfDao;
	}

	public void setDigitizationDao(DigitizationDao digitizationDao) {
		this.digitizationDao = digitizationDao;
	}

	public void setPersonOptParamValDao(GenericDao<PersonOptParamVal, PersonOptParamValId> personOptParamValDao) {
		this.personOptParamValDao = personOptParamValDao;
	}

	public void setFileMetadataParamValDao(GenericDao<FileMetadataParamVal, FileMetadataParamValId> fileMetadataParamValDao) {
		this.fileMetadataParamValDao = fileMetadataParamValDao;
	}

	public void setExperimentOptParamValDao(GenericDao<ExperimentOptParamVal, ExperimentOptParamValId> experimentOptParamValDao) {
		this.experimentOptParamValDao = experimentOptParamValDao;
	}

	public void setResearchGroupMembershipDao(GenericDao<ResearchGroupMembership, ResearchGroupMembershipId> researchGroupMembershipDao) {
		this.researchGroupMembershipDao = researchGroupMembershipDao;
	}

	public void setArtifactDao(GenericDao<Artifact, Integer> artifactDao) {
		this.artifactDao = artifactDao;
	}

	public void setElectrodeSystemDao(GenericDao<ElectrodeSystem, Integer> electrodeSystemDao) {
		this.electrodeSystemDao = electrodeSystemDao;
	}

	public void setSubjectGroupDao(GenericDao<SubjectGroup, Integer> subjectGroupDao) {
		this.subjectGroupDao = subjectGroupDao;
	}
}
