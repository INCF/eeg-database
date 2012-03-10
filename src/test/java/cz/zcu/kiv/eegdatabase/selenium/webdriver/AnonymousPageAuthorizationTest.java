package cz.zcu.kiv.eegdatabase.selenium.webdriver;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import static org.junit.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: Jiri Novotny
 * Date: 10.3.12
 * Time: 15:34
 * To change this template use File | Settings | File Templates.
 */
public class AnonymousPageAuthorizationTest {

    protected WebDriver driver = new InternetExplorerDriver();

    private static final String DRIVER_BASE_ADDR = "http://localhost:8080/EEGDatabase";

    @Test
    public void testItemVisibilityAnonymous() throws Exception {
        driver.get(DRIVER_BASE_ADDR + "/scenarios/list.html");
        assertEquals("Log in - EEGbase", driver.getTitle());
    }

    @After
    public void cleanUp() {
        driver.close();
    }
}
