package cz.zcu.kiv.eegdatabase.selenium.webdriver;

import cz.zcu.kiv.eegdatabase.logic.Util;
import org.junit.Test;
import org.openqa.selenium.By;

import static org.junit.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jiri Novotny
 * Date: 9.3.12
 * Time: 21:34
 * To change this template use File | Settings | File Templates.
 */
public class UserRolePageAuthorizationTest extends AbstractDatabaseAwareWebdriverTest {

    @Test
    public void testItemVisibilityReader() throws Exception{
        createTestUser(Util.ROLE_READER);
        driver.get(DRIVER_BASE_ADDR);
    	login(TEST_USER_NAME, TEST_USER_PASSWORD, driver);

        driver.findElement(By.linkText("Articles")).click();//link accessible from home.html when logged in
        //driver.get(DRIVER_BASE_ADDR + "/articles/list.html");
        assertNoSuchElement(By.linkText("Articles Settings"));
        assertAccessDenied(DRIVER_BASE_ADDR + "/articles/settings.html");

        driver.get(DRIVER_BASE_ADDR + "/groups/list.html");
        assertNoSuchElement(By.linkText("Book room for group"));
        assertNoSuchElement(By.linkText("Edit group role"));
        assertAccessDenied(DRIVER_BASE_ADDR + "/groups/book-room.html");

        driver.get(DRIVER_BASE_ADDR + "/home.html");
        assertNoSuchElement(By.linkText("People"));
        assertAccessDenied(DRIVER_BASE_ADDR + "/people/add-person.html");

        driver.get(DRIVER_BASE_ADDR + "/home.html");
        assertNoSuchElement(By.linkText("History"));
        assertAccessDenied(DRIVER_BASE_ADDR + "/history/daily-history.html");

        assertAccessDenied(DRIVER_BASE_ADDR + "/administration/change-user-role.html");
    }

    private void visitUserVisibleLinks() throws Exception {
        driver.get(DRIVER_BASE_ADDR);
        login(TEST_USER_NAME, TEST_USER_PASSWORD, driver);

        driver.findElement(By.linkText("Articles")).click();//link accessible from home.html when logged in
        driver.findElement(By.linkText("Articles Settings")).click();
        assertEquals("Articles Settings - EEGbase", driver.getTitle());

        driver.get(DRIVER_BASE_ADDR + "/groups/list.html");
        driver.findElement(By.linkText("Edit group role"));//verify link exists
        driver.findElement(By.linkText("Book room for group")).click();//click -> book
        assertEquals("Book UU403 for experiment - EEGbase", driver.getTitle());

        driver.get(DRIVER_BASE_ADDR + "/home.html");
        driver.findElement(By.linkText("People")).click();
        driver.findElement(By.linkText("Add person")).click();
        assertEquals("Add/edit person - EEGbase", driver.getTitle());

        driver.get(DRIVER_BASE_ADDR + "/home.html");
        driver.findElement(By.linkText("History")).click();//TODO Not group admin, so I should not see the link
    }

    @Test
    public void testItemVisibilityUser() throws Exception{
        createTestUser(Util.ROLE_USER);
        visitUserVisibleLinks();

        assertAccessDenied(DRIVER_BASE_ADDR + "/history/daily-history.html");
        assertAccessDenied(DRIVER_BASE_ADDR + "/administration/change-user-role.html");
    }

    @Test
    public void testItemVisibilityAdmin() throws Exception {
        createTestUser(Util.ROLE_ADMIN);
        visitUserVisibleLinks();

        driver.get(DRIVER_BASE_ADDR + "/history/daily-history.html");
        assertEquals("Daily download history - EEGbase", driver.getTitle());

        driver.get(DRIVER_BASE_ADDR + "/administration/change-user-role.html");
        assertEquals("Change user role - EEGbase", driver.getTitle());
    }
}
