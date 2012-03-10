package cz.zcu.kiv.eegdatabase.selenium.webdriver;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.dao.EducationLevelDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import org.junit.After;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Base class for selenium tests using database manipulation to
 * prepare the users and environment
 * Also contains public static convenience methods & fields
 * for tests not directly subclassing this file
 * User: jnovotny
 * Date: 10.3.12
 * Time: 17:44
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractDatabaseAwareWebdriverTest extends AbstractDataAccessTest {

    public static final String DRIVER_BASE_ADDR = "http://localhost:8080/EEGDatabase";
    public static final String LOGIN_PAGE_TITLE = "Home page - EEGbase";//homepage used as login page

    protected static final String TEST_USER_NAME = "junit-test-user";
    protected static final String TEST_USER_PASSWORD = ControllerUtils.getRandomPassword();
    private static final String TEST_EDUCATION_LEVEL = "junit-education-level";

    @Autowired
    protected PersonDao personDao;

    @Autowired
    protected EducationLevelDao educationLevelDao;

    private Person person;
    private EducationLevel educationLevel;

    protected WebDriver driver = createWebDriver();

    public static WebDriver createWebDriver(){
        return new InternetExplorerDriver();//There are compatibility issues with newer Firefox drivers
    }

    public static void login(String user, String pwd, WebDriver driver){
    	driver.findElement(By.name("j_username")).sendKeys(user);
        final WebElement passwordField = driver.findElement(By
                .name("j_password"));
        passwordField.sendKeys(pwd);
        passwordField.submit();
    }

     protected void assertNoSuchElement(By by) {
        try{
           driver.findElement(by);
           fail("Element " + by.toString() + " should not be available!");
        } catch (NoSuchElementException e){
            //ok, element does not exist
        }
    }

    protected void assertAccessDenied(String path){
        driver.get(path);
        assertEquals("Access denied - EEGbase", driver.getTitle());
    }

    protected Person createTestUser(String role){
        deleteTestUserIfExists();
        educationLevel = new EducationLevel();
        educationLevel.setDefaultNumber(0);
        educationLevel.setTitle(TEST_EDUCATION_LEVEL);
        educationLevel.setEducationLevelId(0);
        educationLevelDao.create(educationLevel);
        person = createPersonEntity(role);
        person.setEducationLevel(educationLevel);
        personDao.create(person);
        assertNotNull(personDao.read(person.getPersonId()));
        return person;
    }

    private Person createPersonEntity(String role){
        Person p = new Person();
        p.setUsername(TEST_USER_NAME);
        p.setAuthority(role);
        p.setPassword(ControllerUtils.getMD5String(TEST_USER_PASSWORD));
        p.setEmail("junit@test.reader");
        p.setSurname("junit-test-surname");
        p.setGivenname("junit-test-name");
        p.setConfirmed(true);
        p.setGender('M');
        p.setLaterality('X');
        return p;
    }

    @After
    public void cleanUp(){
        try{
            driver.close();
        } finally {
            if(person != null){
                personDao.delete(person);
            }
            if(educationLevel != null){
                educationLevelDao.delete(educationLevel);
            }
        }
    }

    private void deleteTestUserIfExists() {
        Person previous = personDao.getPerson(TEST_USER_NAME);
        if(previous != null){//when something goes wrong
            personDao.delete(previous);
        }
        List<EducationLevel> prevLevels = educationLevelDao.getEducationLevels(TEST_EDUCATION_LEVEL);
        for(EducationLevel level : prevLevels){
            educationLevelDao.delete(level);
        }
    }
}
