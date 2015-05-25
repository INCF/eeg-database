package cz.zcu.kiv.eegdatabase.ui;

import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.logic.Util;
import net.sourceforge.jwebunit.junit.WebTester;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

import static org.testng.FileAssert.fail;

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
        tester.clickLinkWithText(getProperty("action.logout"));


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

        tester.assertTextPresent(getProperty("menuItem.myGroups"));
        tester.clickLinkWithText(getProperty("menuItem.myGroups"));

        tester.assertTextPresent("new group");

        tester.clickLinkWithText(getProperty("link.detail"));
        tester.assertLinkPresentWithText(getProperty("button.listOfMembers"));
        tester.clickLinkWithText(getProperty("button.listOfMembers"));
        tester.assertTextPresent("jan.stebetak@seznam.cz");

        tester.clickLinkWithText(getProperty("action.logout"));

    }

    @Test(groups = "web", dependsOnMethods = {"testCreateResearchGroup"})
    public void testAddMemberValidation() throws InterruptedException, IOException {
        tester.clickLinkWithText(getProperty("menuItem.groups"));
        tester.assertTextPresent(getProperty("menuItem.myGroups"));
        tester.clickLinkWithText(getProperty("menuItem.myGroups"));

        tester.assertTextPresent("new group");
        tester.clickLinkWithText(getProperty("link.detail"));

        tester.assertLinkPresentWithText(getProperty("button.listOfMembers"));
        tester.clickLinkWithText(getProperty("button.listOfMembers"));
        tester.assertTextPresent("jan.stebetak@seznam.cz");

        tester.assertLinkPresentWithText(getProperty("button.addMemberToGroup"));
        tester.clickLinkWithText(getProperty("button.addMemberToGroup"));
        tester.setTextField("username", "");

        tester.clickButtonWithText(getProperty("button.addMemberToGroup"));
        Thread.sleep(waitForAjax);

        tester.assertTextPresent("Field 'E-mail' is required.");
        tester.assertTextPresent("Field 'User role' is required.");

        tester.setTextField("username", "xxx@xxx.com");
        tester.selectOption("roles", getProperty("select.option.groupAdmin"));
        tester.clickButtonWithText(getProperty("button.addMemberToGroup"));
        Thread.sleep(waitForAjax);

        tester.assertTextPresent(getProperty("invalid.userNameDoesNotExist"));

        tester.clickLinkWithText(getProperty("action.logout"));

    }

    @Test(groups = "web", dependsOnMethods = {"testCreateResearchGroup"})
    public void testAddMemberDuplicity() throws InterruptedException, IOException {

        tester.clickLinkWithText(getProperty("menuItem.groups"));
        tester.assertTextPresent(getProperty("menuItem.myGroups"));
        tester.clickLinkWithText(getProperty("menuItem.myGroups"));

        tester.assertTextPresent("new group");
        tester.clickLinkWithText(getProperty("link.detail"));

        tester.assertLinkPresentWithText(getProperty("button.listOfMembers"));
        tester.clickLinkWithText(getProperty("button.listOfMembers"));
        tester.assertTextPresent("jan.stebetak@seznam.cz");

        tester.assertLinkPresentWithText(getProperty("button.addMemberToGroup"));
        tester.clickLinkWithText(getProperty("button.addMemberToGroup"));

        tester.setTextField("username", "jan.stebetak@seznam.cz");
        tester.selectOption("roles", getProperty("select.option.groupAdmin"));
        tester.clickButtonWithText(getProperty("button.addMemberToGroup"));
        Thread.sleep(waitForAjax);

        tester.assertTextPresent(getProperty("invalid.userNameAlreadyInGroup"));

        tester.clickLinkWithText(getProperty("action.logout"));

    }
    @Test(groups = "web", dependsOnMethods = {"testCreateResearchGroup"})
    public void testGroupPermission() throws InterruptedException, IOException {

        tester.clickLinkWithText(getProperty("menuItem.groups"));
        tester.assertTextPresent("new group");
        tester.clickLinkWithText(getProperty("link.detail"));

        tester.assertTextPresent("jan.stebetak@seznam.cz"); //Is logged user a member of group
        tester.assertLinkPresentWithText(getProperty("button.addMemberToGroup"));
        tester.clickLinkWithText(getProperty("action.logout"));

        if (!personDao.usernameExists("jan.stebetak2@seznam.cz")) {
            Person person = TestUtils.createPersonForTesting("jan.stebetak2@seznam.cz", Util.ROLE_ADMIN);
            person.setConfirmed(true);
            personDao.create(person);
        }
        tester.setTextField("userName", "jan.stebetak2@seznam.cz");
        tester.setTextField("password", "stebjan");
        tester.clickButtonWithText(getProperty("action.login"));

        tester.clickLinkWithText(getProperty("menuItem.groups"));
        tester.assertTextPresent("new group");
        tester.clickLinkWithText(getProperty("link.detail"));

        tester.assertLinkPresentWithText(getProperty("button.membershipRequest"));

        tester.clickLinkWithText(getProperty("action.logout"));
    }

    @Test(groups = "web", dependsOnMethods = {"testCreateResearchGroup"})
    public void testAddMemberToGroup() throws InterruptedException, IOException {

        tester.clickLinkWithText(getProperty("menuItem.groups"));
        tester.assertTextPresent("new group");
        tester.clickLinkWithText(getProperty("link.detail"));

        if (!personDao.usernameExists("jan.stebetak3@seznam.cz")) {
            Person person = TestUtils.createPersonForTesting("jan.stebetak3@seznam.cz", Util.ROLE_USER);
            person.setConfirmed(true);
            personDao.create(person);
        }
        tester.assertLinkPresentWithText(getProperty("button.listOfMembers"));
        tester.clickLinkWithText(getProperty("button.listOfMembers"));
        tester.assertTextPresent("jan.stebetak@seznam.cz"); //Is a member of group

        tester.assertLinkPresentWithText(getProperty("button.addMemberToGroup"));
        tester.clickLinkWithText(getProperty("button.addMemberToGroup"));

        tester.setTextField("username", "jan.stebetak3@seznam.cz");
        tester.selectOption("roles", getProperty("select.option.groupAdmin"));
        tester.clickButtonWithText(getProperty("button.addMemberToGroup"));
        Thread.sleep(waitForAjax);

        tester.assertTextPresent("jan.stebetak3@seznam.cz"); //new member

        tester.clickLinkWithText(getProperty("action.logout"));
    }

    @Test(groups = "web", dependsOnMethods = {"testAddMemberToGroup", "testTransferOwnershipValidation"})
    public void testTransferOwnership() throws InterruptedException, IOException {

        tester.clickLinkWithText(getProperty("menuItem.groups"));
        tester.assertTextPresent("new group");
        tester.clickLinkWithText(getProperty("link.detail"));

        tester.assertLinkPresentWithText(getProperty("button.addMemberToGroup"));

        tester.assertLinkPresentWithText(getProperty("button.listOfMembers"));
        tester.clickLinkWithText(getProperty("button.listOfMembers"));
        tester.assertTextPresent("jan.stebetak3@seznam.cz"); //Is added in the previous test

        tester.assertLinkPresentWithText(getProperty("button.transferOwnership"));
        tester.clickLinkWithText(getProperty("button.transferOwnership"));

        tester.selectOption("members", "jan.stebetak3@seznam.cz");
        tester.clickButtonWithText(getProperty("button.transferOwnership"));
        Thread.sleep(waitForAjax);

        tester.assertLinkNotPresentWithText(getProperty("button.transferOwnership")); //the logged user is no more owner

        tester.clickLinkWithText(getProperty("action.logout"));
    }

    @Test(groups = "web", dependsOnMethods = {"testCreateResearchGroup"})
    public void testTransferOwnershipValidation() throws InterruptedException, IOException {

        tester.clickLinkWithText(getProperty("menuItem.groups"));
        tester.assertTextPresent("new group");
        tester.clickLinkWithText(getProperty("link.detail"));
        tester.assertLinkPresentWithText(getProperty("button.listOfMembers"));
        tester.clickLinkWithText(getProperty("button.listOfMembers"));

        tester.assertTextPresent("jan.stebetak@seznam.cz"); //Is a logged user
        tester.assertLinkPresentWithText(getProperty("button.transferOwnership"));
        tester.clickLinkWithText(getProperty("button.transferOwnership"));

        tester.clickButtonWithText(getProperty("button.transferOwnership"));
        Thread.sleep(waitForAjax);
        tester.assertTextPresent("Field 'User name' is required.");
        try {

            tester.selectOption("members", "jan.stebetak3@seznam.cz");
            tester.clickButtonWithText(getProperty("button.transferOwnership"));
            Thread.sleep(waitForAjax);
            fail("User is not member of the group.");
        } finally {

            tester.clickLinkWithText(getProperty("action.logout"));
        }
    }
}
