package cz.zcu.kiv.eegdatabase.webservices.rest.groups;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.webservices.rest.groups.wrappers.ResearchGroupData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Tests for Research group service.
 *
 * @author Petr Miko
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-context.xml"})
public class ResearchGroupServiceImplTest {

    @Autowired
    @Qualifier("personDao")
    private PersonDao personDao;
    @Autowired
    private ResearchGroupService service;
    @Autowired
    @Qualifier("researchGroupDao")
    private ResearchGroupDao groupDao;

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
    public void testGetAllGroups() throws Exception {
        Collection<ResearchGroup> groupsOriginal = groupDao.getAllRecords();
        List<ResearchGroupData> groupsObtained = service.getAllGroups();

        Assert.assertNotNull(groupsObtained);
        Assert.assertEquals(groupsOriginal.size(), groupsObtained.size());
    }

    @Test
    @Transactional
    public void testGetMyGroups() throws Exception {
        Collection<ResearchGroup> groupsOriginal = personDao.getLoggedPerson().getResearchGroups();
        List<ResearchGroupData> groupsObtained = service.getMyGroups();

        Assert.assertNotNull(groupsObtained);
        Assert.assertEquals(groupsOriginal.size(), groupsObtained.size());
    }
}
