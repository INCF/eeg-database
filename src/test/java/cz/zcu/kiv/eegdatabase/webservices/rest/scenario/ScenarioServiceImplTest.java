package cz.zcu.kiv.eegdatabase.webservices.rest.scenario;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ScenarioDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.webservices.rest.scenario.wrappers.ScenarioData;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Tests for scenario service
 *
 * @author Petr Miko
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-context.xml"})
public class ScenarioServiceImplTest {

    @Autowired
    private PersonDao personDao;
    @Autowired
    private ScenarioService service;
    @Autowired
    private ScenarioDao scenarioDao;

    @Before
    public void setUp() throws Exception {
        //credentials in plain, not really safe, but don't know another way to get logged in person
        Person user = personDao.getPerson("petrmiko@students.zcu.cz");
        List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(user.getAuthority()));
        Authentication auth = new UsernamePasswordAuthenticationToken(user, "heslo", authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    @Transactional(readOnly = true)
    public void testGetAllScenarios() throws Exception {
        Collection<Scenario> scenariosOriginal = scenarioDao.getAvailableScenarios(personDao.getLoggedPerson());
        Collection<ScenarioData> scenariosObtained = service.getAllScenarios();

        Assert.assertNotNull(scenariosObtained);
        Assert.assertEquals(scenariosOriginal.size(), scenariosObtained.size());
    }

    @Test
    @Transactional
    public void testGetMyScenarios() throws Exception {
        Collection<Scenario> scenariosOriginal = personDao.getLoggedPerson().getScenarios();
        Collection<ScenarioData> scenariosObtained = service.getMyScenarios();

        Assert.assertNotNull(scenariosObtained);
        Assert.assertEquals(scenariosOriginal.size(), scenariosObtained.size());
    }

    /**
     * Creates new record and tests that it is present in all obtainable lists.
     *
     * @throws Exception error while testing
     */
    @Test
    @Transactional
    public void testCreate() throws Exception {
        Person user = personDao.getLoggedPerson();
        ScenarioData scenarioData = new ScenarioData();
        scenarioData.setFileLength(500);
        scenarioData.setResearchGroupId(user.getResearchGroups().iterator().next().getResearchGroupId());
        scenarioData.setFileName("junit");
        scenarioData.setDescription("junit");
        scenarioData.setMimeType("application/octet-stream");
        scenarioData.setScenarioName("junit");
        File tmp = File.createTempFile("junit", "test");

        Scenario scenario = null;
        try {
            int pk = service.create(scenarioData, new MockMultipartFile("junit", "junit.txt", "application/octet-stream", new FileInputStream(tmp)));
            scenario = scenarioDao.read(pk);

            Assert.assertNotNull(scenario);

            Collection<ScenarioData> scenariosObtained = service.getMyScenarios();

            boolean contains = false;

            for (ScenarioData s : scenariosObtained)
                if (s.getScenarioId() == scenario.getScenarioId()) {
                    contains = true;
                    break;
                }

            Assert.assertTrue(contains);

            scenariosObtained = service.getAllScenarios();

            contains = false;

            for (ScenarioData s : scenariosObtained)
                if (s.getScenarioId() == scenario.getScenarioId()) {
                    contains = true;
                    break;
                }

            Assert.assertTrue(contains);

        } finally {
            tmp.delete();
            if (scenario != null)
                scenarioDao.delete(scenario);
        }

    }
}
