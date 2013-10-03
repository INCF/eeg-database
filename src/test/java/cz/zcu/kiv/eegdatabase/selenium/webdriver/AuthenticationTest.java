/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   AuthenticationTest.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
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
