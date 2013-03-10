package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import cz.zcu.kiv.eegdatabase.data.dao.ArticleDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Article;
import org.jfree.data.time.TimeSeriesTableModel;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jiri Vlasimsky (vlasimsky.jiri@gmail.com)
 * Date: 6.3.11
 * Time: 16:57
 */
public class ArticleDaoTest extends AbstractDataAccessTest {

    @Autowired
    private PersonDao personDao;

    @Autowired
    private ArticleDao articleDao;

    @Test
    @Transactional
    public void testGetAllArticle() {

        Article article = new Article();
        article.setTitle("prvni");
        article.setText("text");
        Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
        article.setTime(currentTimestamp);
        article.setPerson(personDao.getPerson("kaby"));

        List<Article> initialArticleList = articleDao.getAllArticles();
        articleDao.create(article);
        //should contain one more article
        List<Article> articleListAfterCreation = articleDao.getAllArticles();
        assertEquals(initialArticleList.size()+1, articleListAfterCreation.size());
    }

    public ArticleDao getArticleDao() {
        return articleDao;
    }

    public void setArticleDao(ArticleDao articleDao) {
        this.articleDao = articleDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }
}
