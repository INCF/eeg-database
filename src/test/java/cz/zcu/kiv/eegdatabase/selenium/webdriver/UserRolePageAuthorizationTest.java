package cz.zcu.kiv.eegdatabase.selenium.webdriver;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
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
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.sql.Driver;

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
    protected SessionFactory sessionFactory;

    protected HibernateTemplate hibernateTemplate;

    protected WebDriver driver = new InternetExplorerDriver();

    private static final String DRIVER_BASE_ADDR = "http://localhost:8080/EEGDatabase";
    private static final String USERNAME = "junit-test-user";
    private static final String PASSWORD = ControllerUtils.getRandomPassword();

    Person person;
    EducationLevel educationLevel;

    @Test
    public void testItemVisibilityReader() throws Exception{
        driver.get(DRIVER_BASE_ADDR);
    	login(USERNAME, PASSWORD, driver);

        driver.get(DRIVER_BASE_ADDR + "/articles/list.html");
        assertNoSuchElement(driver,By.linkText("Articles Settings"));
        driver.get(DRIVER_BASE_ADDR + "/articles/settings.html");
        assertEquals("Access denied - EEGbase", driver.getTitle());

        driver.get(DRIVER_BASE_ADDR + "/administration/change-user-role.html");
        assertEquals("Access denied - EEGbase", driver.getTitle());
    }

    @Test
    public void testItemVisibilityUser() throws Exception{
        changeRole(Util.ROLE_USER);
        driver.get(DRIVER_BASE_ADDR);
    	login(USERNAME, PASSWORD, driver);//goes automatically to the menu

        driver.findElement(By.linkText("Articles")).click();
        driver.findElement(By.linkText("Articles Settings"));//click -> settings
        driver.get(DRIVER_BASE_ADDR + "/articles/settings.html");
        assertEquals("Articles Settings - EEGbase", driver.getTitle());

        driver.get(DRIVER_BASE_ADDR + "/administration/change-user-role.html");
        assertEquals("Access denied - EEGbase", driver.getTitle());
    }

    @Test
    public void testItemVisibilityAdmin() throws Exception {
        changeRole(Util.ROLE_ADMIN);
        driver.get(DRIVER_BASE_ADDR);

        login(USERNAME, PASSWORD, driver);//goes automatically to the menu
        driver.get(DRIVER_BASE_ADDR + "/articles/settings.html");
        assertEquals("Articles Settings - EEGbase", driver.getTitle());

        driver.get(DRIVER_BASE_ADDR + "/administration/change-user-role.html");
        assertEquals("Change user role - EEGbase", driver.getTitle());
    }

    @Before
    public void init(){
        Person previous = personDao.getPerson(USERNAME);
        if(previous != null){//when something goes wrong
            personDao.delete(previous);
        }
        educationLevel = new EducationLevel();
        educationLevel.setDefaultNumber(0);
        educationLevel.setTitle("junit-education-level");
        educationLevel.setEducationLevelId(0);
        hibernateTemplate = createHibernateTemplate(sessionFactory);
        hibernateTemplate.save(educationLevel);
        person = createPerson();
        person.setEducationLevel(educationLevel);
        hibernateTemplate.save(person);
        hibernateTemplate.flush();
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

    private void changeRole(String newRole){
        person.setAuthority(newRole);
        personDao.update(person);
        hibernateTemplate.flush();
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

    private void assertNoSuchElement(WebDriver driver, By by) {
        try{
           driver.findElement(By.linkText("Articles Settings"));
           fail("Element " + by.toString() + " should not be available!");
        } catch (NoSuchElementException e){
            //ok, element does not exist
        }
    }

    @After
    public void cleanUp(){
        try{
            driver.close();
        } finally {
            personDao.delete(person);
            hibernateTemplate.delete(educationLevel);
        }
    }
}
