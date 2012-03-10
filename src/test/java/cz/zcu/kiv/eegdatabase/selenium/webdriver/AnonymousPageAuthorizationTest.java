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

    protected WebDriver driver = AbstractDatabaseAwareWebdriverTest.createWebDriver();

    @Test
    public void testItemVisibilityAnonymous() throws Exception {
        driver.get(AbstractDatabaseAwareWebdriverTest.DRIVER_BASE_ADDR + "/scenarios/list.html");
        assertEquals(AbstractDatabaseAwareWebdriverTest.LOGIN_PAGE_TITLE, driver.getTitle());
    }

    @After
    public void cleanUp() {
        driver.close();
    }
}
