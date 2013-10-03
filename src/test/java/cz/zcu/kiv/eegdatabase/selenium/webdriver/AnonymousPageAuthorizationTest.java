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
 *   AnonymousPageAuthorizationTest.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
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
