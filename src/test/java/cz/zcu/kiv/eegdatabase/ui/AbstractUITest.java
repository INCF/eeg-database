/*******************************************************************************
 * This file is part of the EEG-database project
 *
 * ==========================================
 *
 * Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 * ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * ***********************************************************************************************************************
 *
 * AbstractUITest.java, 2014/09/10 00:01 Jan Stebetak
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.ui;

import net.sourceforge.jwebunit.junit.WebTester;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

/**
 * Created by stebjan on 10.9.14.
 */

@ContextConfiguration(locations = {"classpath:/web-test-context.xml"})
public abstract class AbstractUITest extends AbstractTestNGSpringContextTests {
    protected WebTester tester;

    protected int waitForAjax = 5000;

    protected String url = "http://localhost:8082";


    protected void createGroupIfNotExists() throws InterruptedException {
        tester.clickLinkWithText("Groups");
        try {
            tester.assertTextPresent("new group");
        } catch (AssertionError ex) {
            tester.clickLinkWithText("Create group");
            tester.setTextField("title", "new group");
            tester.setTextField("description", "description");
            String oldPage = tester.getTestingEngine().getPageText();
            tester.clickButtonWithText("Save");
            waitForAjaxWithTimeout(oldPage);
        }

    }

    /**
     * Method wait for ajax response by comparison of old and new page contents. The default timeout is 60 s.
     * @param oldPage - old page content before an ajax operation was called.
     * @throws InterruptedException
     */
    protected void waitForAjaxWithTimeout(String oldPage) throws InterruptedException {
        waitForAjaxWithTimeout(oldPage, 60);
    }

    /**
     *
     * Method wait for ajax response by comparison of old and new page contents.
     * @param oldPage - old page content before an ajax operation was called.
     * @param timeoutSec - timeout in seconds
     * @throws InterruptedException
     */
    protected void waitForAjaxWithTimeout(String oldPage, int timeoutSec) throws InterruptedException {
        for (int i = 0; i < timeoutSec * 2; i++) {
            try {
            if (!(oldPage.equals(tester.getTestingEngine().getPageText()))) {
                System.out.println("Time spent [s]: " + i * 0.5);
                break;
            }
            }catch (NullPointerException npe) {
                System.out.println("NPE");
                Thread.sleep(500);
                continue;
            }
            Thread.sleep(500);
        }
    }

    protected void waitForAjax(String oldPage) throws InterruptedException {
        while(oldPage.equals(tester.getTestingEngine().getPageText())) {
            Thread.sleep(500);
        }

    }

}

