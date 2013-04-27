package cz.zcu.kiv.eegdatabase.webservices.rest.experiment;

import cz.zcu.kiv.eegdatabase.data.dao.*;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.webservices.rest.experiment.wrappers.*;
import cz.zcu.kiv.eegdatabase.webservices.rest.groups.wrappers.ResearchGroupData;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Test class for experiment REST service.
 *
 * @author Petr Miko
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-context.xml"})
public class ExperimentServiceImplTest {

    private final static Log logger = LogFactory.getLog(ExperimentServiceImplTest.class);

    @Autowired
    private ExperimentService service;
    @Autowired
    private PersonDao personDao;
    @Autowired
    private SimpleElectrodeLocationDao electrodeLocationDao;
    @Autowired
    private DigitizationDao digitizationDao;
    @Autowired
    private SimpleDiseaseDao diseaseDao;
    @Autowired
    private SimpleArtifactDao artifactDao;
    @Autowired
    private SimpleElectrodeFixDao electrodeFixDao;
    @Autowired
    private ExperimentDao experimentDao;

    @Before
    public void setUp() throws Exception {
        logger.debug("Logging in test user");
        //credentials in plain, not really safe, but don't know another way to get logged in person
        Person user = personDao.getPerson("petrmiko@students.zcu.cz");
        List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(user.getAuthority()));
        Authentication auth = new UsernamePasswordAuthenticationToken(user, "heslo", authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    public void testGetPublicExperiments() throws Exception {
        logger.debug("Obtaining number of public experiments");
        int publicExpCount = service.getPublicExperimentsCount();
        logger.debug("Fetching " + publicExpCount + " public experiments");
        List<ExperimentData> exp = service.getPublicExperiments(0, publicExpCount);
        Assert.assertNotNull(exp);
        Assert.assertEquals(publicExpCount, exp.size());
        logger.debug("Fetching of public experiments OK");
    }

    @Test
    @Transactional
    public void testCreateElectrodeLocation() throws Exception {
        ElectrodeLocation elObtained = null;
        ElectrodeLocationData el = new ElectrodeLocationData();
        el.setTitle("Test location");
        el.setDescription("testing...");
        el.setAbbr("test");

        //default type
        ElectrodeTypeData type = new ElectrodeTypeData();
        type.setId(1);
        //default fix
        ElectrodeFixData fix = new ElectrodeFixData();
        fix.setId(1);

        el.setElectrodeType(type);
        el.setElectrodeFix(fix);


        try {
            int pk = service.createElectrodeLocation(el);
            elObtained = electrodeLocationDao.read(pk);

            Assert.assertNotNull(elObtained);
            Assert.assertEquals(el.getTitle(), elObtained.getTitle());
            Assert.assertEquals(el.getAbbr(), elObtained.getShortcut());
            Assert.assertEquals(el.getDescription(), elObtained.getDescription());
        } finally {
            //removing created data
            if (elObtained != null)
                electrodeLocationDao.delete(elObtained);
        }

    }

    @Test
    @Transactional
    public void testCreateDigitization() throws Exception {
        Digitization digiObtained = null;
        DigitizationData digi = new DigitizationData();
        digi.setSamplingRate(228);
        digi.setGain(12.3f);
        digi.setFilter("Testing...");

        try {
            int pk = service.createDigitization(digi);
            digiObtained = digitizationDao.read(pk);

            Assert.assertNotNull(digiObtained);
            Assert.assertEquals(digi.getFilter(), digiObtained.getFilter());
            Assert.assertEquals(digi.getSamplingRate(), digiObtained.getSamplingRate());
            Assert.assertEquals(digi.getGain(), digiObtained.getGain());
        } finally {
            if (digiObtained != null)
                digitizationDao.delete(digiObtained);
        }
    }

    @Test
    @Transactional
    public void testCreateDisease() throws Exception {
        Disease diseaseObtained = null;
        DiseaseData disease = new DiseaseData();
        disease.setName("junit disease");
        disease.setDescription("junit test");

        try {
            int pk = service.createDisease(disease);
            diseaseObtained = diseaseDao.read(pk);

            Assert.assertNotNull(diseaseObtained);
            Assert.assertEquals(disease.getName(), diseaseObtained.getTitle());
            Assert.assertEquals(disease.getDescription(), diseaseObtained.getDescription());

        } finally {
            if (diseaseObtained != null)
                diseaseDao.delete(diseaseObtained);
        }

    }

    @Test
    @Transactional
    public void testCreateArtifact() throws Exception {
        Artifact artifactObtained = null;
        ArtifactData artifact = new ArtifactData();
        artifact.setCompensation("junit compensation");
        artifact.setRejectCondition("junit");

        try {
            int pk = service.createArtifact(artifact);
            artifactObtained = artifactDao.read(pk);

            Assert.assertNotNull(artifactObtained);
            Assert.assertEquals(artifact.getCompensation(), artifactObtained.getCompensation());
            Assert.assertEquals(artifact.getRejectCondition(), artifactObtained.getRejectCondition());
        } finally {
            if (artifactObtained != null)
                artifactDao.delete(artifactObtained);
        }

    }

    @Test
    @Transactional
    public void testCreateElectrodeFix() throws Exception {
        ElectrodeFix fixObtained = null;
        ElectrodeFixData fix = new ElectrodeFixData();
        fix.setTitle("junit fix");
        fix.setDescription("junit description");

        try {
            int pk = service.createElectrodeFix(fix);
            fixObtained = electrodeFixDao.read(pk);

            Assert.assertNotNull(fixObtained);
            Assert.assertEquals(fix.getTitle(), fixObtained.getTitle());
            Assert.assertEquals(fix.getDescription(), fixObtained.getDescription());
        } finally {
            if (fixObtained != null)
                electrodeFixDao.delete(fixObtained);
        }
    }

    @Test
    @Transactional
    public void testCreateExperiment() throws Exception {
        Experiment expObtained = null;
        ExperimentData exp = new ExperimentData();

        exp.setTemperature(12);
        exp.setEnvironmentNote("junit note");

        //owner
        OwnerData owner = new OwnerData();
        owner.setId(personDao.getLoggedPerson().getPersonId());
        exp.setOwner(owner);

        //subject
        SubjectData subject = new SubjectData();
        subject.setPersonId(personDao.getLoggedPerson().getPersonId());
        exp.setSubject(subject);

        //default group
        ResearchGroupData group = new ResearchGroupData();
        group.setGroupId(1);
        exp.setResearchGroup(group);

        //first existing scenario
        ScenarioSimpleData scenario = new ScenarioSimpleData();
        scenario.setScenarioId(4);
        exp.setScenario(scenario);

        //default artifact
        ArtifactData artifact = new ArtifactData();
        artifact.setArtifactId(1);
        exp.setArtifact(artifact);

        //default digitization
        DigitizationData digi = new DigitizationData();
        digi.setDigitizationId(1);
        exp.setDigitization(digi);

        //times
        exp.setStartTime(Calendar.getInstance().getTime());
        exp.setEndTime(Calendar.getInstance().getTime());

        //default weather
        WeatherData weather = new WeatherData();
        weather.setWeatherId(1);
        exp.setWeather(weather);

        ElectrodeConfData conf = new ElectrodeConfData();
        conf.setImpedance(15);

        //default electrode system
        ElectrodeSystemData system = new ElectrodeSystemData();
        system.setId(1);
        conf.setElectrodeSystem(system);

        //default electrode location
        ElectrodeLocationData location = new ElectrodeLocationData();
        location.setId(1);
        List<ElectrodeLocationData> locations = new ArrayList<ElectrodeLocationData>();
        locations.add(location);
        conf.setElectrodeLocations(new ElectrodeLocationDataList(locations));
        exp.setElectrodeConf(conf);

        //now is experiment container ready - creation process

        try {
            int pk = service.createExperiment(exp);
            expObtained = (Experiment) experimentDao.read(pk);
            Assert.assertNotNull(expObtained);
            Assert.assertEquals(exp.getOwner().getId(), expObtained.getPersonByOwnerId().getPersonId());
            Assert.assertEquals(exp.getSubject().getPersonId(), expObtained.getPersonBySubjectPersonId().getPersonId());
            Assert.assertEquals(exp.getResearchGroup().getGroupId(), expObtained.getResearchGroup().getResearchGroupId());
            Assert.assertEquals(exp.getArtifact().getArtifactId(), expObtained.getArtifact().getArtifactId());
            Assert.assertEquals(exp.getDigitization().getDigitizationId(), expObtained.getDigitization().getDigitizationId());
            Assert.assertEquals(exp.getEnvironmentNote(), expObtained.getEnvironmentNote());
            Assert.assertEquals(exp.getTemperature(), expObtained.getTemperature());
            Assert.assertEquals(exp.getElectrodeConf().getImpedance(), expObtained.getElectrodeConf().getImpedance());
        } finally {
            if (expObtained != null) {
                experimentDao.delete(expObtained);
            }
        }

    }
}
