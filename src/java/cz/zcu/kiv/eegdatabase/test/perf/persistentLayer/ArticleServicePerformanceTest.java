package cz.zcu.kiv.eegdatabase.test.perf.persistentLayer;

import cz.zcu.kiv.eegdatabase.data.dao.ArticleCommentDao;
import cz.zcu.kiv.eegdatabase.data.dao.ArticleDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Article;
import cz.zcu.kiv.eegdatabase.data.pojo.ArticleComment;
import cz.zcu.kiv.eegdatabase.test.perf.persistentLayer.helper.DateFormater;
import org.junit.Test;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Kabourek
 * Date: 16.5.11
 * Time: 17:37
 * To change this template use File | Settings | File Templates.
 * Identificator of test  /PPT_A_1_WorWitArt_L/. Contains document Testovaci scenare.docx.
 */
   public class ArticleServicePerformanceTest  extends PerformanceTest {

    /**
     * Constant for atribute of test data.
     */
    public static final String ARTICLE_TITLE = "testovaci clanek";
    public static final String ARTICLE_TEXT = "text clanku";
    public static final String ARTICLE_UPDATE_TEXT = "update text clanku";
    public static final String ARTICLE_COMMENT = "komentar k clanku";
    public static final String TEST_PERSON = "kaby";

    private ArticleDao articleDao;
    private PersonDao personDao;
    private ArticleCommentDao articleCommnetDao;
    private Article article;
    private ArticleComment articleComment;

/**
* Method test create article for next test.
*
*/
    public void createTestArticle(){
        article = new Article();
        article.setTitle(DateFormater.createTestDate(ARTICLE_TITLE));
        article.setText(DateFormater.createTestDate(ARTICLE_TEXT));
        article.setTime(DateFormater.getTimestamp());
        System.out.println("time stamp je "+DateFormater.getTimestamp());
        article.setPerson(personDao.getPerson(TEST_PERSON));
    }

    public void createArticleCommens(){
        createTestArticle();
        articleDao.create(article);
        articleComment = new ArticleComment();
        articleComment.setArticle(article);
        articleComment.setPerson(personDao.getPerson(TEST_PERSON));
        articleComment.setTime(DateFormater.getTimestamp());
        articleComment.setText(ARTICLE_COMMENT);
        articleComment.setUserIsOwnerOrAdmin(true);
        articleComment.setUserMemberOfGroup(true);

    }

/**
* Method test create article.
* Identificator of test / PPT_A_2_AddArt_F /. Contains document Testovaci scenare.docx.
*/
    @Test
    public void testCreateArticle(){
        createTestArticle();
        articleDao.create(article);
        assertEquals(DateFormater.createTestDate(ARTICLE_TEXT), article.getText());
        assertEquals(DateFormater.createTestDate(ARTICLE_TITLE), article.getTitle());
        articleDao.delete(article);

    }

/**
* Method test edit article.
* User: Kabourek
* Identificator of test /PPT_A_3_EdiArt_F/. Contains document Testovaci scenare.docx.
*/
    @Test
    public void testEditArticle(){
        createTestArticle();
        String pom = DateFormater.createTestDate(ARTICLE_UPDATE_TEXT);
        article.setText(pom);
        article.setTitle("update_title");
        articleDao.update(article);
        assertEquals(pom, article.getText());
        assertEquals("update_title", article.getTitle());
        articleDao.delete(article);
    }


/**
 * Method test get all article.
 * User: Kabourek
 * Identificator of test. Contains document Testovaci scenare.docx.
 */
    @Test
    public void testGetAllArticle(){
        List<Article> articleList = articleDao.getAllArticles() ;
        assertTrue(articleList.size() > 0);
        System.out.println("pocet clanku "+articleList.size());
    }

/**
* Method test delete article.
* User: Kabourek
* Identificator of test /PPT_A_5_DelArt_F/. Contains document Testovaci scenare.docx.
*/
    @Test
    public void testDeleteArticle() throws Exception {
     createTestArticle();
     articleDao.create(article);
     articleDao.delete(article);
    }
/**
* Method test create commons.
* User: Kabourek
* Identificator of test /PPT_A_4_AddComArt_F/. Contains document Testovaci scenare.docx.
*/
    @Test
    public void createCommons(){
         createArticleCommens();
         articleCommnetDao.create(articleComment);
         assertEquals(ARTICLE_COMMENT,articleComment.getText());
         articleCommnetDao.delete(articleComment);
    }


    /**
     * Setter for DAO object.
     */
    public void setArticleDao(ArticleDao articleDao) {
    this.articleDao = articleDao;
    }

    public void setPersonDao(PersonDao personDao) {
    this.personDao = personDao;
    }

    public void setArticleCommnetDao(ArticleCommentDao articleCommnetDao) {
    this.articleCommnetDao = articleCommnetDao;
    }

//    public ArticleDao getArticleDao() {
//    return articleDao;
//    }
}

