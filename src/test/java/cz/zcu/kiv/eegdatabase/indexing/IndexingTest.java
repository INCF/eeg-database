package cz.zcu.kiv.eegdatabase.indexing;

import cz.zcu.kiv.eegdatabase.data.indexing.Indexer;
import cz.zcu.kiv.eegdatabase.data.indexing.PojoIndexer;
import cz.zcu.kiv.eegdatabase.data.pojo.Article;
import cz.zcu.kiv.eegdatabase.data.pojo.ArticleComment;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServer;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Jan Koren
 * Date: 19.2.13
 * Time: 0:21
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test-context.xml"})
public class IndexingTest {

    @Autowired
    SolrServer solrServer;

    private static List<ArticleComment> articleCommentList;
    private static List<Article> articleList;
    private static List<Experiment> experimentList;
    Logger logger = Logger.getLogger(this.getClass());

    public IndexingTest()
    {
        changeParserImplementationToXerces();//Important!
    }

    private void changeParserImplementationToXerces() {
        System.setProperty("javax.xml.parsers.SAXParserFactory","org.apache.xerces.jaxp.SAXParserFactoryImpl");
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory","org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
    }

    @BeforeClass
    public static void setUp() {
        createTestPojos();
    }

    @AfterClass
    public static void tearDown() {

    }

    //@Ignore
    @Test
    public void indexArticleComments() throws Exception {
        PojoIndexer<ArticleComment> articleCommentIndexer = new PojoIndexer<ArticleComment>();
        articleCommentIndexer.setSolrServer(solrServer);

        for (ArticleComment articleComment : articleCommentList) {
            System.out.print("TEXT JE: ");
            System.out.println(articleComment.getText());
        }

        for (ArticleComment articleComment : articleCommentList) {
            articleCommentIndexer.index(articleComment);
        }
    }

    //@Ignore
    @Test
    public void indexArticles() throws Exception {
        PojoIndexer<Article> articleIndexer = new PojoIndexer<Article>();
        articleIndexer.setSolrServer(solrServer);
        for (Article article : articleList) {
            articleIndexer.index(article);
        }
    }

    //@Ignore
    @Test
    public void indexExperiments() throws Exception {
        PojoIndexer<Experiment> experimentIndexer = new PojoIndexer<Experiment>();
        experimentIndexer.setSolrServer(solrServer);
        for (Experiment experiment : experimentList) {
            experimentIndexer.index(experiment);
        }
    }

    private static void createTestPojos() {
        articleCommentList = createTestArticleComments();
        articleList = createTestArticles();
        experimentList = createTestExperiments();
    }

    private static List<ArticleComment> createTestArticleComments() {
        List<ArticleComment> articleCommentList = new ArrayList<ArticleComment>();

        articleCommentList.add(createTestArticleComment(1, "No comment"));
        articleCommentList.add(createTestArticleComment(2, "No cement"));
        articleCommentList.add(createTestArticleComment(3, "No dement"));
        articleCommentList.add(createTestArticleComment(4, "Yes popleta"));
        articleCommentList.add(createTestArticleComment(5, "Jez, Popelka"));

        return  articleCommentList;
    }

    private static ArticleComment createTestArticleComment(int id, String text) {
        ArticleComment articleComment= new ArticleComment();
        articleComment.setCommentId(id);
        articleComment.setText(text);
        return articleComment;
    }

    private static List<Article> createTestArticles() {
        List<Article> articleList = new ArrayList<Article>();

        articleList.add(createTestArticle(1, "Great article about nothing.", "Nothing"));
        articleList.add(createTestArticle(2, "In alteration insipidity impression " +
                "by travelling reasonable up motionless. Of regard warmth by unable sudden garden ladies.", "Music leave"));
        articleList.add(createTestArticle(3, "One morning, when Gregor Samsa woke from troubled dreams", "kafka"));
        articleList.add(createTestArticle(4, "proper human room although a little too small, lay peacefully " +
                "between its four familiar walls.", "A wonderful serenity"));
        articleList.add(createTestArticle(5, "She packed her seven versalia, put her initial into the belt" +
                " and made herself on the way.", "World of Grammar"));
        articleList.add(createTestArticle(6, "To an English person, it will seem like simplified English, " +
                "as a skeptical Cambridge friend of mine told me what Occidental is.", "One day"));

        return articleList;
    }

    private static Article createTestArticle(int id, String text, String title) {
        Article article = new Article();
        article.setArticleId(id);
        article.setText(text);
        article.setTitle(title);
        return article;
    }

    private static List<Experiment> createTestExperiments() {
        List<Experiment> experimentList = new ArrayList<Experiment>();

        experimentList.add(createTestExperiment(1, 20, "this is note"));
        experimentList.add(createTestExperiment(2, 28, "Another fairy tale with a happy ending."));
        experimentList.add(createTestExperiment(3, 19, "Wrong experiment, do not take its results too seriously."));
        experimentList.add(createTestExperiment(4, 35, "Man with a goat"));
        experimentList.add(createTestExperiment(5, 31, "Sunny day, no clouds whatsoever."));
        experimentList.add(createTestExperiment(6, 50, "The warmest night in history"));
        experimentList.add(createTestExperiment(7, 22, "Great success!"));

        return experimentList;
    }

    private static Experiment createTestExperiment(int id, int temperature, String note) {
        Experiment experiment = new Experiment();
        experiment.setExperimentId(id);
        experiment.setTemperature(temperature);
        experiment.setEnvironmentNote(note);
        return experiment;
    }
}
