package cz.zcu.kiv.eegdatabase.test.perf;

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

    @Test
    @PerfTest(invocations = 2, threads = 2)
    public void createArticleTest(){
        article = new Article();
        article.setTitle("prvni");
        article.setText("aaa");
        article.setPerson(personeDao.getPerson("kaby"));
        articleDao.create(article);

    }
    @Test
    @PerfTest(invocations = 2, threads = 2)
    public void editArticleTest(){
        article = new Article();
        article.setTitle("prvni_edit");
        article.setText("vvv");
        article.setPerson(personeDao.getPerson("kaby"));
        articleDao.update(article);

    }

    @Test
    @PerfTest(invocations = 2, threads = 2)
    public void getAllArticleTest(){
        articleDao.getAllArticles();

    }
    @Test
    @PerfTest(invocations = 2, threads = 2)
    public void createCommonsTest(){
         articleComment = new ArticleComment();
         articleComment.setArticle(article);
         articleComment.setText("komentar");
         article.setArticleComments((Set<ArticleComment>) articleComment);
         articleCommnetDao.create(article);

    }

    //@Test
   // @PerfTest(invocations = 5, threads = 2)
    //@Required(max = 1200, average = 250)
    public void test1() throws Exception {
        Thread.sleep(200);
        System.out.println("helloWord");

    }

    //setter
    public void setArticleDao(ArticleDao articleDao) {
    this.articleDao = articleDao;
    }
}

