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

    public static final String ARTICLE_TITLE="testovaci clanek";
    public static final String ARTICLE_TEXT="text clanku";



    private ArticleDao articleDao;

    private PersonDao personDao;

    private ArticleCommentDao articleCommnetDao;

    private Article article;
    private ArticleComment articleComment;


    public ArticleServicePerformanceTest(){
         article = new Article();
    }

    public void createTestArticle(){
        article.setTitle(DateFormater.createTestDate(ARTICLE_TITLE));
        article.setText(DateFormater.createTestDate(ARTICLE_TEXT));
        article.setTime(DateFormater.getTimestamp());
        System.out.println("time stamp je "+DateFormater.getTimestamp());
        article.setPerson(personDao.getPerson("kaby"));
    }

/**
* Method test create article.
* User: Kabourek
* Identificator of test / PPT_A_2_AddArt_F /. Contains document Testovaci scenare.docx.
*/
    @Test
    public void testCreateArticle(){
        createTestArticle();
        articleDao.create(article);
        articleDao.delete(article);

    }

///**
// * Method test edit article.
// * User: Kabourek
// * Identificator of test /PPT_A_3_EdiArt_F/. Contains document Testovaci scenare.docx.
// */
//    @Test
//    @PerfTest(invocations = 2, threads = 2)
//    public void editArticleTest(){
//        article = new Article();
//        article.setTitle("prvni_edit");
//        article.setText("vvv");
//        article.setPerson(personDao.getPerson("kaby"));
//        articleDao.update(article);
//
//    }


/**
 * Method test get all article.
 * User: Kabourek
 * Identificator of test. Contains document Testovaci scenare.docx.
 */
    @Test
    public void testGetAllArticle(){
        List<Article> articleList = articleDao.getAllArticles() ;

        System.out.println("pocet clanku "+articleList.size());
    }
///**
// * Method test create commons.
// * User: Kabourek
// * Identificator of test /PPT_A_4_AddComArt_F/. Contains document Testovaci scenare.docx.
// */
//    //@Test
//    @PerfTest(invocations = 2, threads = 2)
//    public void createCommonsTest(){
//         articleComment = new ArticleComment();
//         articleComment.setArticle(article);
//         articleComment.setText("komentar");
//         article.setArticleComments((Set<ArticleComment>) articleComment);
//         articleCommnetDao.create(article);
//
//    }


/**
* Method test delete article.
* User: Kabourek
* Identificator of test /PPT_A_5_DelArt_F/. Contains document Testovaci scenare.docx.
*/
//    @Test
//    public void testDeleteArticle() throws Exception {
//      System.out.println("tohle je article pro smazani"+article.getTitle());
//      articleCommnetDao.delete(article);
//    }

    //setter  and getter
    public void setArticleDao(ArticleDao articleDao) {
    this.articleDao = articleDao;
    }

    public void setPersonDao(PersonDao personDao) {
    this.personDao = personDao;
    }

    public ArticleDao getArticleDao() {
    return articleDao;
    }
}

