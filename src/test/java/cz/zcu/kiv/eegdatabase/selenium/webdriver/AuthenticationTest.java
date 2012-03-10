package cz.zcu.kiv.eegdatabase.selenium.webdriver;

import cz.zcu.kiv.eegdatabase.logic.Util;
import org.junit.Test;
import org.openqa.selenium.By;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: Jiri Novotny
 * Date: 10.3.12
 * Time: 17:55
 * To change this template use File | Settings | File Templates.
 */
public class AuthenticationTest extends AbstractDatabaseAwareWebdriverTest {

    @Test
    public void testRedirectingToOriginalPageAfterLogin() throws Exception {
        createTestUser(Util.ROLE_READER);
        driver.get(DRIVER_BASE_ADDR + "/scenarios/list.html");
        assertEquals(LOGIN_PAGE_TITLE, driver.getTitle());
        login("wrong-username", "wrong-password", driver);//make a mistake on the first try
        String errorMessage = driver.findElement(By.className("errorMessage")).getText();
        assertTrue("Auth error should be reported to the user",errorMessage.toLowerCase().contains("bad credentials"));
        assertEquals(LOGIN_PAGE_TITLE, driver.getTitle());//still on login page
        login(TEST_USER_NAME, TEST_USER_PASSWORD, driver);
        assertEquals("List of scenarios - EEGbase",driver.getTitle());
    }
}
