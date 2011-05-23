package cz.zcu.kiv.eegdatabase.test.perf.persistentLayer;

import cz.zcu.kiv.eegdatabase.data.dao.ArticleCommentDao;
import cz.zcu.kiv.eegdatabase.data.dao.ArticleDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Article;
import cz.zcu.kiv.eegdatabase.data.pojo.ArticleComment;
import org.databene.contiperf.PerfTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Kabourek
 * Date: 16.5.11
 * Time: 17:37
 * To change this template use File | Settings | File Templates.
 * Identificator of test  /PPT_A_1_WorWitArt_L/. Contains document Testovaci scenare.docx.
 */

   public class ArticleServicePerformanceTest  extends PerformanceTest {

    @Autowired
    ArticleDao articleDao;

    @Autowired
    PersonDao personeDao;

    @Autowired
    ArticleCommentDao articleCommnetDao;

    private Article article;
    private ArticleComment articleComment;


/**
 * Method test create article.
 * User: Kabourek
 * Identificator of test / PPT_A_2_AddArt_F /. Contains document Testovaci scenare.docx.
 */
    @Test
    @PerfTest(invocations = 2, threads = 2)
    public void createArticleTest(){
        article = new Article();
        article.setTitle("prvni");
        article.setText("aaa");
        article.setPerson(personeDao.getPerson("kaby"));
        articleDao.create(article);

    }

/**
 * Method test edit article.
 * User: Kabourek
 * Identificator of test /PPT_A_3_EdiArt_F/. Contains document Testovaci scenare.docx.
 */
    @Test
    @PerfTest(invocations = 2, threads = 2)
    public void editArticleTest(){
        article = new Article();
        article.setTitle("prvni_edit");
        article.setText("vvv");
        article.setPerson(personeDao.getPerson("kaby"));
        articleDao.update(article);

    }


/**
 * Method test get all article.
 * User: Kabourek
 * Identificator of test. Contains document Testovaci scenare.docx.
 */
    @Test
    @PerfTest(invocations = 2, threads = 2)
    public void getAllArticleTest(){
        articleDao.getAllArticles();

    }


/**
 * Method test create commons.
 * User: Kabourek
 * Identificator of test /PPT_A_4_AddComArt_F/. Contains document Testovaci scenare.docx.
 */
    @Test
    @PerfTest(invocations = 2, threads = 2)
    public void createCommonsTest(){
         articleComment = new ArticleComment();
         articleComment.setArticle(article);
         articleComment.setText("komentar");
         article.setArticleComments((Set<ArticleComment>) articleComment);
         articleCommnetDao.create(article);

    }


/**
 * Method test delete article.
 * User: Kabourek
 * Identificator of test /PPT_A_5_DelArt_F/. Contains document Testovaci scenare.docx.
 */
   //@Test
   // @PerfTest(invocations = 5, threads = 2)
   //@Required(max = 1200, average = 250)
    public void deleteArticle() throws Exception {
      articleCommnetDao.delete(article);
    }

    //setter
    public void setArticleDao(ArticleDao articleDao) {
    this.articleDao = articleDao;
    }
}

