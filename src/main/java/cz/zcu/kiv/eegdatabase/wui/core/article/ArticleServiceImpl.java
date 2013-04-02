package cz.zcu.kiv.eegdatabase.wui.core.article;

import cz.zcu.kiv.eegdatabase.data.dao.ArticleCommentDao;
import cz.zcu.kiv.eegdatabase.data.dao.ArticleDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Article;
import cz.zcu.kiv.eegdatabase.data.pojo.ArticleComment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class ArticleServiceImpl implements ArticleService {

    protected Log log = LogFactory.getLog(getClass());

    ArticleDao<Article, Integer> dao;
    ArticleCommentDao<ArticleComment, Integer> commentDao;

    @Required
    public void setDao(ArticleDao<Article, Integer> dao) {
        this.dao = dao;
    }

    @Required
    public void setCommentDao(ArticleCommentDao<ArticleComment, Integer> commentDao) {
        this.commentDao = commentDao;
    }

    @Override
    @Transactional
    public Integer create(Article newInstance) {
        return dao.create(newInstance);
    }

    @Override
    @Transactional(readOnly = true)
    public Article read(Integer id) {
        return dao.read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Article> readByParameter(String parameterName, Object parameterValue) {
        return dao.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional
    public void update(Article transientObject) {
        dao.update(transientObject);
    }

    @Override
    @Transactional
    public void delete(Article persistentObject) {
        dao.delete(persistentObject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Article> getAllRecords() {
        return dao.getAllRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Article> getRecordsAtSides(int first, int max) {
        return dao.getRecordsAtSides(first, max);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountRecords() {
        return dao.getCountRecords();
    }

    @Override
    public List<Article> getUnique(Article example) {
        return dao.findByExample(example);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Article> getAllArticles() {
        return dao.getAllArticles();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Article> getArticlesForUser(Person person) {
        return dao.getArticlesForUser(person);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Article> getArticlesForUser(Person person, int limit) {
        return dao.getArticlesForUser(person, limit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Article> getArticlesForList(Person person, int min, int count) {
        return dao.getArticlesForList(person, min, count);
    }

    @Override
    @Transactional(readOnly = true)
    public int getArticleCountForPerson(Person person) {
        return dao.getArticleCountForPerson(person);
    }

    @Override
    @Transactional(readOnly = true)
    public Article getArticleDetail(int id, Person loggedPerson) {
        return dao.getArticleDetail(id, loggedPerson);
    }

    @Override
    @Transactional
    public Integer create(ArticleComment newInstance) {
        return commentDao.create(newInstance);
    }

    @Override
    @Transactional(readOnly = true)
    public ArticleComment readComment(Integer id) {
        return commentDao.read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleComment> readCommentByParameter(String parameterName, int parameterValue) {
        return commentDao.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleComment> readCommentByParameter(String parameterName, String parameterValue) {
        return commentDao.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional
    public void updateComment(ArticleComment transientObject) {
        commentDao.update(transientObject);
    }

    @Override
    @Transactional
    public void deleteComment(ArticleComment persistentObject) {
        commentDao.delete(persistentObject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleComment> getAllCommentRecords() {
        return commentDao.getAllRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleComment> getCommentRecordsAtSides(int first, int max) {
        return commentDao.getRecordsAtSides(first, max);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountCommentRecords() {
        return commentDao.getCountRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleComment> getCommentsForArticle(int articleId) {
        return commentDao.getCommentsForArticle(articleId);
    }

}
