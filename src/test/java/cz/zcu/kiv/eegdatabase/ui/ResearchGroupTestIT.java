package cz.zcu.kiv.eegdatabase.ui;

import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.Util;
import net.sourceforge.jwebunit.junit.WebTester;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by Roman Mouček on 13.11.2014.
 */
public class ResearchGroupTestIT extends AbstractUITest {

    @Autowired
    private PersonDao personDao;

    private WebTester tester;


    @BeforeMethod(groups = "web")
    public void setUp() throws IOException {
        if (!personDao.usernameExists("jan.stebetak@seznam.cz")) {
            Person person = TestUtils.createPersonForTesting("jan.stebetak@seznam.cz", Util.ROLE_USER);
            person.setConfirmed(true);
            personDao.create(person);
        }
        tester = new WebTester();

        //   tester.setBaseUrl("http://eeg2.kiv.zcu.cz:8080");
        tester.setBaseUrl(url);
        tester.beginAt("/home-page");
        tester.setTextField("userName", "jan.stebetak@seznam.cz");
        tester.setTextField("password", "stebjan");
        tester.clickButtonWithText(getProperty("action.login"));
        tester.assertTextPresent(getProperty("action.logout"));
    }

    @Test(groups = "web")
    public void testCreateResearchGroupValidation() throws InterruptedException, IOException {

        tester.clickLinkWithText(getProperty("menuItem.groups"));
        tester.assertTextPresent(getProperty("menuItem.createGroup"));
        tester.clickLinkWithText(getProperty("menuItem.createGroup"));

        tester.setTextField("title", "");
        tester.setTextField("description", "");
        tester.clickButtonWithText(getProperty("button.save"));
        Thread.sleep(waitForAjax);
        tester.assertTextPresent("Field 'Group title' is required.");
        tester.assertTextPresent("Field 'Group description' is required.");

        tester.setTextField("title", "new group");
        tester.setTextField("description", "");
        tester.clickButtonWithText(getProperty("button.save"));
        Thread.sleep(waitForAjax);
        tester.assertTextPresent("Field 'Group description' is required.");

        tester.setTextField("title", "");
        tester.setTextField("description", "description");
        tester.clickButtonWithText(getProperty("button.save"));
        Thread.sleep(waitForAjax);
        tester.assertTextPresent("Field 'Group title' is required.");
        tester.assertTextPresent(getProperty("action.logout"));


    }
    @Test(groups = "web")
    public void testCreateResearchGroup() throws InterruptedException, IOException {
//        if (!groupExists("new group")) {
        tester.clickLinkWithText(getProperty("menuItem.groups"));
        tester.assertTextPresent(getProperty("menuItem.createGroup"));
        tester.clickLinkWithText(getProperty("menuItem.createGroup"));

        tester.setTextField("title", "new group");
        tester.setTextField("description", "description");
        tester.clickButtonWithText(getProperty("button.save"));
        Thread.sleep(waitForAjax);
//        }

        tester.assertTextPresent("My groups");
        tester.clickLinkWithText("My groups");

        tester.assertTextPresent("new group");

        tester.clickLinkWithText(getProperty("link.detail"));
        tester.assertLinkPresentWithText(getProperty("button.listOfMembers"));
        tester.clickLinkWithText(getProperty("button.listOfMembers"));
        tester.assertTextPresent("jan.stebetak@seznam.cz");

        tester.assertTextPresent(getProperty("action.logout"));

    }
}
