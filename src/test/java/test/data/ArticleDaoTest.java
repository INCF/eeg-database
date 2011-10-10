package test.data;

import cz.zcu.kiv.eegdatabase.data.dao.ArticleDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Article;
import org.junit.Test;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jiri Vlasimsky (vlasimsky.jiri@gmail.com)
 * Date: 6.3.11
 * Time: 16:57
 */
public class ArticleDaoTest extends AbstractDataAccessTest {
    private ArticleDao articleDao;

    public void setPersoneDao(PersonDao personeDao) {
        this.personeDao = personeDao;
    }

    private PersonDao personeDao;

    @Test
    public void testGetAllArticle() {
        Article article = new Article();
        article.setTitle("prvni");
        article.setPerson(personeDao.getPerson("kaby"));

        articleDao.create(article);

        List<Article> articleList = articleDao.getAllArticles();
        // simple success test if article count is equal to 18
        assertEquals(18, articleList.size());
       // Article article = new Article();

    }

    public ArticleDao getArticleDao() {
        return articleDao;
    }

    public void setArticleDao(ArticleDao articleDao) {
        this.articleDao = articleDao;
    }
}
