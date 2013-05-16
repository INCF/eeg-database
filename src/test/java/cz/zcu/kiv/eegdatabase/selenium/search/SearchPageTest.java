package cz.zcu.kiv.eegdatabase.selenium.search;

import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import org.junit.*;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created with IntelliJ IDEA.
 * User: Jan Koren
 * Date: 9.5.13
 * Time: 15:50
 * To change this template use File | Settings | File Templates.
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test-context.xml"})
public class SearchPageTest {

    private static WebDriver driver;
    private String baseUrl = "http://localhost:8080";
    private final String TEST_LOGIN = "testaccountforeeg2@seznam.cz";
    private final String TEST_PASSWORD = "123456";
    private static final int DRIVER_IMPLICIT_WAITING_TIME = 300;

    @BeforeClass
    public static void setUp() throws Exception {
        System.out.println("HERE I AM SETUP");
        if (driver == null) {
            System.out.println("Here here");
            //FirefoxBinary binary = new FirefoxBinary(new File("c:/Program Files (x86)/Mozilla Firefox/firefox.exe"));
            FirefoxProfile profile = new FirefoxProfile();

            //driver = new FirefoxDriver(binary, profile);
            driver = new FirefoxDriver();
            driver.manage().timeouts().implicitlyWait(
                    DRIVER_IMPLICIT_WAITING_TIME, TimeUnit.MILLISECONDS);
        }

        System.out.println("DRIVER CREATED");
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    /**
     *
     */
    public void login() {
        System.out.println("GETTING TO THE HOME PAGE...");
        driver.get(baseUrl + "/home-page?0");

        driver.findElement(By.name("userName")).clear();
        driver.findElement(By.name("userName")).sendKeys(TEST_LOGIN);
        driver.findElement(By.name("password")).clear();
        driver.findElement(By.name("password")).sendKeys(TEST_PASSWORD);
        driver.findElement(By.name(":submit")).click();
    }

    /**
     *
     */
    @Test
    public void goToSearchPage() {
        login();
        WebElement element = driver.findElement(By.linkText(ResourceUtils.getString("title.page.search.menu")));
        element.click();

        WebElement header = driver.findElement(By.cssSelector(".header"));
        header.findElement(By.name("autocomplete"));
        header.findElement(By.name("searchButton"));

        WebElement content = driver.findElement(By.cssSelector(".mainContent"));
        content.findElement(By.name("autocomplete"));
        content.findElement(By.name("searchButton"));

        driver.findElement(By.name("autocomplete"));
        driver.findElement(By.name("searchButton"));
    }

    @Test
    public void testAutocomplete() throws InterruptedException {
        login();
        driver.findElement(By.linkText(ResourceUtils.getString("title.page.search.menu"))).click();

        WebElement content = driver.findElement(By.cssSelector(".mainContent"));
        content.findElement(By.name("autocomplete")).sendKeys("te");

        driver.wait(500);

        WebElement searchField = driver.findElement(By.className("wicket-aa"));
        assertTrue(searchField.isDisplayed());
        List<WebElement> suggestions = searchField.findElements(By.cssSelector("ul li"));
        assertTrue(suggestions.size() > 0);
    }

    @Test
    public void searchPhraseWithResults() {
        login();

        WebElement header = driver.findElement(By.cssSelector(".header"));
        header.findElement(By.name("autocomplete")).sendKeys("test");
        header.findElement(By.name("searchButton")).click();

        //check presence of searched text in textboxes
        String searchTextHeader = header.findElement(By.name("autocomplete")).getAttribute("value");
        assertEquals(searchTextHeader, "test");
        String searchTextContent = header.findElement(By.name("autocomplete")).getAttribute("value");
        assertEquals(searchTextContent, "test");

        // check displayed facet categories and search results
        List<WebElement> facetCategoryPanel = driver.findElements(By.cssSelector(".facetCategories li"));
        assertTrue(facetCategoryPanel.size() == 6);
        List<WebElement> searchResults = driver.findElements(By.className("searchResult"));
        assertTrue(searchResults.size() > 0);

        // check presence of the page navigator
        WebElement pageNavigator = driver.findElement(By.className("searchNavigator"));
        assertTrue(pageNavigator.isDisplayed());
    }


    @Test
    public void searchPhraseNoResults() {
        login();

        driver.findElement(By.linkText(ResourceUtils.getString("title.page.search.menu"))).click();
        WebElement content = driver.findElement(By.cssSelector(".mainContent"));
        content.findElement(By.name("autocomplete")).sendKeys("xkapdosdasdljksa");
        content.findElement(By.name("searchButton")).click();

        WebElement noResultsLabel = content.findElement(By.cssSelector("label"));
        assertEquals(noResultsLabel.getText(), ResourceUtils.getString("text.search.noResults"));
    }

    @After
    public void logout() {
        driver.findElement(By.linkText(ResourceUtils.getString("action.logout"))).click();
    }

    @AfterClass
    public static void tearDown() throws Exception {
         driver.quit();
    }
}
