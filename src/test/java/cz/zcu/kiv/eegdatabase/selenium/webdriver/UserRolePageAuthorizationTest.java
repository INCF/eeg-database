package cz.zcu.kiv.eegdatabase.selenium.webdriver;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.dao.EducationLevelDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.sql.Driver;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jiri Novotny
 * Date: 9.3.12
 * Time: 21:34
 * To change this template use File | Settings | File Templates.
 */
public class UserRolePageAuthorizationTest extends AbstractDataAccessTest {

    @Autowired
    protected PersonDao personDao;

    @Autowired
    protected EducationLevelDao educationLevelDao;

    protected WebDriver driver = new InternetExplorerDriver();

    private static final String DRIVER_BASE_ADDR = "http://localhost:8080/EEGDatabase";
    private static final String USERNAME = "junit-test-user";
    private static final String PASSWORD = ControllerUtils.getRandomPassword();
    private static final String EDUCATION_LEVEL = "junit-education-level";

    Person person;
    EducationLevel educationLevel;

    @Test
    public void testItemVisibilityReader() throws Exception{
        driver.get(DRIVER_BASE_ADDR);
    	login(USERNAME, PASSWORD, driver);

        driver.get(DRIVER_BASE_ADDR + "/articles/list.html");
        assertNoSuchElement(By.linkText("Articles Settings"));
        assertAccessDenied(DRIVER_BASE_ADDR + "/articles/settings.html");

        driver.get(DRIVER_BASE_ADDR + "/groups/list.html");
        assertNoSuchElement(By.linkText("Book room for group"));
        assertAccessDenied(DRIVER_BASE_ADDR + "/groups/book-room.html");

        driver.get(DRIVER_BASE_ADDR + "/people/list.html");
        assertNoSuchElement(By.linkText("Add person"));
        assertAccessDenied(DRIVER_BASE_ADDR + "/people/add-person.html");

        driver.get(DRIVER_BASE_ADDR + "/articles/list.html");//TODO go home instead
        assertNoSuchElement(By.linkText("History"));
        assertAccessDenied(DRIVER_BASE_ADDR + "/history/daily-history.html");

        assertAccessDenied(DRIVER_BASE_ADDR + "/administration/change-user-role.html");
    }

    private void visitUserVisibleLinks() {
        driver.get(DRIVER_BASE_ADDR);
        login(USERNAME, PASSWORD, driver);//goes automatically to the menu

        driver.findElement(By.linkText("Articles")).click();
        driver.findElement(By.linkText("Articles Settings")).click();
        assertEquals("Articles Settings - EEGbase", driver.getTitle());

        driver.get(DRIVER_BASE_ADDR + "/groups/list.html");
        driver.findElement(By.linkText("Book room for group")).click();//click -> book
        assertEquals("Book UU403 for experiment - EEGbase", driver.getTitle());

        driver.get(DRIVER_BASE_ADDR + "/people/list.html");
        driver.findElement(By.linkText("Add person")).click();
        assertEquals("Add/edit person - EEGbase", driver.getTitle());

        driver.get(DRIVER_BASE_ADDR + "/home.html");
        driver.findElement(By.linkText("History")).click();//TODO Not group admin, so I should not see the link
    }

    @Test
    public void testItemVisibilityUser() throws Exception{
        changeRole(Util.ROLE_USER);
        visitUserVisibleLinks();

        assertAccessDenied(DRIVER_BASE_ADDR + "/history/daily-history.html");
        assertAccessDenied(DRIVER_BASE_ADDR + "/administration/change-user-role.html");
    }

    @Test
    public void testItemVisibilityAdmin() throws Exception {
        changeRole(Util.ROLE_ADMIN);
        visitUserVisibleLinks();

        driver.get(DRIVER_BASE_ADDR + "/history/daily-history.html");
        assertEquals("Daily download history - EEGbase", driver.getTitle());

        driver.get(DRIVER_BASE_ADDR + "/administration/change-user-role.html");
        assertEquals("Change user role - EEGbase", driver.getTitle());
    }

    @Before
    public void init(){
        deletePreviousIfFound();
        educationLevel = new EducationLevel();
        educationLevel.setDefaultNumber(0);
        educationLevel.setTitle(EDUCATION_LEVEL);
        educationLevel.setEducationLevelId(0);
        educationLevelDao.create(educationLevel);
        person = createPerson();
        person.setEducationLevel(educationLevel);
        personDao.create(person);
        //hibernateTemplate.flush();//TODO check
        assertNotNull(personDao.read(person.getPersonId()));
    }

    public Person createPerson(){
        Person p = new Person();
        p.setUsername(USERNAME);
        p.setAuthority(Util.ROLE_READER);
        p.setPassword(ControllerUtils.getMD5String(PASSWORD));
        p.setEmail("junit@test.reader");
        p.setSurname("junit-test-surname");
        p.setGivenname("junit-test-name");
        p.setConfirmed(true);
        p.setGender('M');
        p.setLaterality('X');
        return p;
    }

    private void deletePreviousIfFound() {
        Person previous = personDao.getPerson(USERNAME);
        if(previous != null){//when something goes wrong
            personDao.delete(previous);
        }
        List<EducationLevel> prevLevels = educationLevelDao.getEducationLevels(EDUCATION_LEVEL);
        for(EducationLevel level : prevLevels){
            educationLevelDao.delete(level);
        }
    }

    private void changeRole(String newRole){
        person.setAuthority(newRole);
        personDao.update(person);
        //hibernateTemplate.flush();
    }

    static void login(String user, String pwd, WebDriver driver){
    	driver.findElement(By.name("j_username")).sendKeys(user);
        final WebElement passwordField = driver.findElement(By
                .name("j_password"));
        passwordField.sendKeys(pwd);
        passwordField.submit();
    }

    public HibernateTemplate createHibernateTemplate(SessionFactory sessionFactory){
        assertNotNull("Session factory must exist",sessionFactory);
        HibernateDaoSupport daoSupport = new HibernateDaoSupport() {};//empty impl
        daoSupport.setSessionFactory(sessionFactory);
        return daoSupport.getHibernateTemplate();
    }

    private void assertNoSuchElement(By by) {
        try{
           driver.findElement(By.linkText("Articles Settings"));
           fail("Element " + by.toString() + " should not be available!");
        } catch (NoSuchElementException e){
            //ok, element does not exist
        }
    }

    private void assertAccessDenied(String path){
        driver.get(path);
        assertEquals("Access denied - EEGbase", driver.getTitle());
    }

    @After
    public void cleanUp(){
        try{
            driver.close();
        } finally {
            personDao.delete(person);
            educationLevelDao.delete(educationLevel);
        }
    }
}
