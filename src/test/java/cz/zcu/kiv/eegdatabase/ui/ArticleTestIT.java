package cz.zcu.kiv.eegdatabase.ui;

import cz.zcu.kiv.eegdatabase.data.TestUtils;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.Util;
import net.sourceforge.jwebunit.junit.WebTester;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Created by Honza on 16.11.14.
 */
public class ArticleTestIT extends AbstractUITest {

    @Autowired
    private PersonDao personDao;

    @BeforeMethod(groups = "web")
    public void setUp() {
        if (!personDao.usernameExists("jan.stebetak@seznam.cz")) {
            Person person = TestUtils.createPersonForTesting("jan.stebetak@seznam.cz", Util.ROLE_USER);
            person.setConfirmed(true);
            personDao.create(person);
        }

        tester = new WebTester();
        tester.setBaseUrl(url);
        tester.beginAt("/home-page");
        tester.setTextField("userName", "jan.stebetak@seznam.cz");
        tester.setTextField("password", "stebjan");
        tester.clickButtonWithText("Log in");
        tester.assertTextPresent("Log out");

    }

    @Test(groups = "web")
    public void testArticleValidation() throws InterruptedException {

        createGroupIfNotExists();

        tester.clickLinkWithText("Articles");
        tester.assertLinkPresentWithText("Add a new article");
        tester.clickLinkWithText("Add a new article");

        tester.setTextField("title", "");
        tester.setTextField("text", "");
        tester.clickButtonWithText("Save");
        Thread.sleep(waitForAjax);
        tester.assertTextPresent("Field 'Research group' is required.");
        tester.assertTextPresent("Field 'Title' is required.");
        tester.assertTextPresent("Field 'Text' is required.");
        tester.clickLinkWithText("Log out");

    }

    @Test(groups = "web")
    public void testAddArticle() throws InterruptedException {

        createGroupIfNotExists();

        tester.clickLinkWithText("Articles");
        tester.assertLinkPresentWithText("Add a new article");
        tester.clickLinkWithText("Add a new article");
        tester.selectOption("researchGroup", "new group");
        tester.setTextField("title", "Test title");
        tester.setTextField("text", "some text");
        tester.clickButtonWithText("Save");
        Thread.sleep(waitForAjax);

        tester.assertTextPresent("Test title");

        tester.clickLinkWithText("Log out");

    }

    @Test(groups = "web")
    public void testAddArticleCommentWithValidation() throws InterruptedException {

        createGroupIfNotExists();

        tester.clickLinkWithText("Articles");
        tester.assertLinkPresentWithText("Add a new article");
        tester.clickLinkWithText("Add a new article");
        tester.selectOption("researchGroup", "new group");
        tester.setTextField("title", "Test title2");
        tester.setTextField("text", "some text");
        tester.clickButtonWithText("Save");
        Thread.sleep(waitForAjax);

        tester.clickLinkWithText("All articles");

        tester.assertTextPresent("Test title2");
        tester.clickLinkWithText("Test title2");
        tester.assertTextPresent("Post new comment");
        tester.setTextField("text", "");
        tester.clickButtonWithText("Save");
        Thread.sleep(waitForAjax);
        tester.assertTextPresent("Field 'Text' is required.");

        tester.setTextField("text", "new comment");
        tester.clickButtonWithText("Save");
        Thread.sleep(waitForAjax);

        tester.assertTextPresent("new comment");

        tester.clickLinkWithText("Log out");

    }
    @Test(groups = "web")
    public void testArticleVisibility() throws InterruptedException {

        createGroupIfNotExists();

        // Creating article with user "jan.stebetak@seznam.cz" which has a group
        tester.clickLinkWithText("Articles");
        tester.assertLinkPresentWithText("Add a new article");
        tester.clickLinkWithText("Add a new article");
        tester.selectOption("researchGroup", "new group");
        tester.setTextField("title", "Test title3");
        tester.setTextField("text", "some text");
        tester.clickButtonWithText("Save");
        Thread.sleep(waitForAjax);

        tester.assertTextPresent("Test title3");

        tester.clickLinkWithText("Log out");

        //Creating ADMIN which has no group and log-in
        if (!personDao.usernameExists("jan.stebetak2@seznam.cz")) {
            Person person = TestUtils.createPersonForTesting("jan.stebetak2@seznam.cz", Util.ROLE_ADMIN);
            person.setConfirmed(true);
            personDao.create(person);
        }
        tester.setTextField("userName", "jan.stebetak2@seznam.cz");
        tester.setTextField("password", "stebjan");
        tester.clickButtonWithText("Log in");

        tester.assertTextPresent("Articles");
        tester.clickLinkWithText("Articles");
        tester.assertTextPresent("Test title3"); //ADMIN should be able to see a previously created article.

        tester.clickLinkWithText("Log out");

        //Creating USER which has no group and log-in
        if (!personDao.usernameExists("jan.stebetak3@seznam.cz")) {
            Person person = TestUtils.createPersonForTesting("jan.stebetak3@seznam.cz", Util.ROLE_USER);
            person.setConfirmed(true);
            personDao.create(person);
        }
        tester.setTextField("userName", "jan.stebetak3@seznam.cz");
        tester.setTextField("password", "stebjan");
        tester.clickButtonWithText("Log in");

        tester.assertTextPresent("Articles");
        tester.clickLinkWithText("Articles");
        tester.assertTextNotPresent("Test title3"); //USER should not be able to see a previously created article.

        tester.clickLinkWithText("Log out");

    }



}