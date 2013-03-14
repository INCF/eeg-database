package cz.zcu.kiv.eegdatabase.indexing;

import cz.zcu.kiv.eegdatabase.data.annotation.SolrField;
import cz.zcu.kiv.eegdatabase.data.annotation.SolrId;
import cz.zcu.kiv.eegdatabase.data.indexing.IndexingUtils;
import cz.zcu.kiv.eegdatabase.data.indexing.PojoIndexer;
import cz.zcu.kiv.eegdatabase.data.pojo.Article;
import cz.zcu.kiv.eegdatabase.data.pojo.ArticleComment;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

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
    @Qualifier("solrServer")
    private SolrServer solrServer;

    private static List<ArticleComment> articleCommentList;
    private static List<Article> articleList;
    private static List<Experiment> experimentList;
    private Log log = LogFactory.getLog("cz.zcu.kiv.eegdatabase.Tests");

    @BeforeClass
    public static void setUpTestSuite() {
        createTestPojos();
    }

    @AfterClass
    public static void tearDownTestSuite() {
    }

    //@Ignore
    @Test
    public void indexArticleComments() throws Exception {
        log.info("Indexing sample article comments...");
        PojoIndexer<ArticleComment> articleCommentIndexer = new PojoIndexer<ArticleComment>();
        articleCommentIndexer.setSolrServer(solrServer);

        for (ArticleComment articleComment : articleCommentList) {
            log.info(articleComment.getText());
        }

        for (ArticleComment articleComment : articleCommentList) {
            articleCommentIndexer.index(articleComment);
        }
    }

    //@Ignore
    @Test
    public void indexArticles() throws Exception {
        log.info("Indexing sample articles...");
        PojoIndexer<Article> articleIndexer = new PojoIndexer<Article>();
        articleIndexer.setSolrServer(solrServer);
        for (Article article : articleList) {
            articleIndexer.index(article);
        }
    }

    //@Ignore
    @Test
    public void indexExperiments() throws Exception {
        log.info("Indexing sample experiments...");
        PojoIndexer<Experiment> experimentIndexer = new PojoIndexer<Experiment>();
        experimentIndexer.setSolrServer(solrServer);
        for (Experiment experiment : experimentList) {
            experimentIndexer.index(experiment);
        }
    }

    //@Ignore
    @Test
    public void deleteExperimentsFromIndex() throws SolrServerException, IllegalAccessException, IOException {
        log.info("Adding sample experiments to the index...");
        PojoIndexer<Experiment> experimentIndexer = new PojoIndexer<Experiment>();
        experimentIndexer.setSolrServer(solrServer);
        // add new experiment data to the index
        Experiment experiment1 = createTestExperiment(99, 15, "note notes notepad");
        Experiment experiment2 = createTestExperiment(100, 25, "parsley, sage, rosemary and thyme");
        Experiment experiment3 = createTestExperiment(101, 30, "eegdatabase eeg database laborarory brain waves");
        experimentIndexer.index(experiment1);
        experimentIndexer.index(experiment2);
        experimentIndexer.index(experiment3);

        int documentsFound = 0;

        QueryResponse response = createSimpleQuery("rosemary");
        SolrDocumentList documents = response.getResults();
        documentsFound = documents.size();

        log.info("Documents found: " + documentsFound);
        assertEquals(1, documentsFound);

        response = createSimpleQuery("notepad");
        documentsFound = response.getResults().size();
        log.info("Documents found: " + documentsFound);
        assertEquals(1, documentsFound);

        response = createSimpleQuery("brain waves");;
        documentsFound = response.getResults().size();
        log.info("Documents found: " + documentsFound);
        assertEquals(1, documentsFound);

        // delete documents from the solr index
        experimentIndexer.unindex(experiment1);
        experimentIndexer.unindex(experiment2);
        experimentIndexer.unindex(experiment3);

        response = createSimpleQuery("rosemary");
        documentsFound = response.getResults().size();
        log.info("Documents found: " + documentsFound);
        assertEquals(0, documentsFound);

        createSimpleQuery("notepad");
        documentsFound = response.getResults().size();
        log.info("Documents found: " + documentsFound);
        assertEquals(0, documentsFound);

        createSimpleQuery("brain waves");
        documentsFound = response.getResults().size();
        log.info("Documents found: " + documentsFound);
        assertEquals(0, documentsFound);
    }

    /**
     * Tests the getIndexableClasses method from the IndexingUtils class.
     * The method should return the assumed number of classes to be indexed by Solr.
     * (i.e. classes containing appropriate annotations)
     */
    //@Ignore
    @Test
    public void getIndexableClassesTest() {
        log.info("Getting all indexable classes...");
        List<Class<?>> solrIndexableClasses = IndexingUtils.getIndexableClasses();
        int indexedClasses = solrIndexableClasses.size();
        log.info("Classes found: " + indexedClasses);
        assertEquals(30, indexedClasses);
    }

    /**
     * Indexes one instance of each class having the Solr indexing annotations
     * and performs a query that returns all documents representing these instances.
     * Test succeeds if all created documents are returned by the query.
     */
    //@Ignore
    @Test
    public void indexSampleIndexablePojos() throws SolrServerException, Exception {

        List<Class<?>> solrIndexableClasses = IndexingUtils.getIndexableClasses();

        assertEquals(30, solrIndexableClasses.size());

        log.info("Creating one instance of each indexable class...");
        int i = 0;
        for (Class<?> clazz : solrIndexableClasses) {

            Object instance = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            log.info(++i + " " + clazz.getName());

            // sets id and other annotated fields if the class to constant values
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(SolrId.class)) {
                    field.set(instance, 100);
                    log.info(field.getName() + ": " + field.get(instance));
                } else if (field.isAnnotationPresent(SolrField.class)) {
                    if (field.getType().equals(String.class)) {
                        field.set(instance, "Mam to nejlepsi turbodmychadlo.");
                    } else {
                        field.set(instance, 99);
                    }

                    log.info(field.getName() + ": " + field.get(instance));
                }
            }

            PojoIndexer<Object> indexer = new PojoIndexer<Object>();
            indexer.setSolrServer(solrServer);
            indexer.index(instance);
        }

        SolrQuery query = new SolrQuery();
        query.set("df", "text_all");
        query.setQuery("turbodmychadlo");
        query.setIncludeScore(true);
        query.setRows(50); // max number of rows to be returned
        //query.setSortField("class", SolrQuery.ORDER.asc);

        int documentsFound = 0;

        QueryResponse response = solrServer.query(query);
        SolrDocumentList documents = response.getResults();
        documentsFound = documents.size();

        log.info("found documents: " + documentsFound);

        for (SolrDocument document : documents) {
            for (String fieldName : document.getFieldNames()) {
                log.info(fieldName + ": " + document.getFieldValue(fieldName));
            }
        }
        assertEquals(30, documentsFound);
    }


    /**
     * Creates a simple query.
     * @param phrase The input query phrase.
     * @return The query response from the Solr server.
     * @throws SolrServerException
     */
    private QueryResponse createSimpleQuery(String phrase) throws SolrServerException {
        log.info("searching \"" + phrase + "\"...");
        SolrQuery query = new SolrQuery();
        query.set("df", "text_all");
        query.setQuery(phrase);
        query.setRows(10);

        return solrServer.query(query);
    }


    /**
     * Creates lists of test POJOs.
     */
    private static void createTestPojos() {
        articleCommentList = createTestArticleComments();
        articleList = createTestArticles();
        experimentList = createTestExperiments();
    }

    /**
     * Creates a list of sample ArticleComments objects.
     *
     * @return The list of sample ArticleComments objects.
     */
    private static List<ArticleComment> createTestArticleComments() {
        List<ArticleComment> articleCommentList = new ArrayList<ArticleComment>();

        articleCommentList.add(createTestArticleComment(1, "No comment"));
        articleCommentList.add(createTestArticleComment(2, "No cement"));
        articleCommentList.add(createTestArticleComment(3, "No dement"));
        articleCommentList.add(createTestArticleComment(4, "Yes popleta"));
        articleCommentList.add(createTestArticleComment(5, "Jez, Popelka"));

        return articleCommentList;
    }

    /**
     * Creates a test article comment.
     *
     * @param id   article comment id
     * @param text article comment text
     * @return The created article comment.
     */
    private static ArticleComment createTestArticleComment(int id, String text) {
        ArticleComment articleComment = new ArticleComment();
        articleComment.setCommentId(id);
        articleComment.setText(text);
        return articleComment;
    }

    /**
     * Creates a list of sample Article objects.
     *
     * @return The list of sample Article objects.
     */
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

    /**
     * Creates a test article.
     *
     * @param id    article id
     * @param text  article text
     * @param title article title
     * @return The created article.
     */
    private static Article createTestArticle(int id, String text, String title) {
        Article article = new Article();
        article.setArticleId(id);
        article.setText(text);
        article.setTitle(title);
        return article;
    }

    /**
     * Creates a list of sample Experiment objects.
     *
     * @return The list of sample Article objects.
     */
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

    /**
     * Creates a test experiment.
     *
     * @param id          experiment id
     * @param temperature experiment temperature
     * @param note        ecperiment note
     * @return The created experiment.
     */
    private static Experiment createTestExperiment(int id, int temperature, String note) {
        Experiment experiment = new Experiment();
        experiment.setExperimentId(id);
        experiment.setTemperature(temperature);
        experiment.setEnvironmentNote(note);
        return experiment;
    }
}
