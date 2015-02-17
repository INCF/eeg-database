/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   ArticleServiceImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.article;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.transaction.annotation.Transactional;

import cz.zcu.kiv.eegdatabase.data.dao.ArticleCommentDao;
import cz.zcu.kiv.eegdatabase.data.dao.ArticleDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Article;
import cz.zcu.kiv.eegdatabase.data.pojo.ArticleComment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.service.MailService;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;

public class ArticleServiceImpl implements ArticleService {
    
    protected Log log = LogFactory.getLog(getClass());

    private ArticleDao dao;
    private ArticleCommentDao commentDao;
    private MailService mailService;
    private ResearchGroupDao groupDao;

    @Required
    public void setDao(ArticleDao dao) {
        this.dao = dao;
    }

    @Required
    public void setCommentDao(ArticleCommentDao commentDao) {
        this.commentDao = commentDao;
    }

    @Required
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }
    
    @Required
    public void setGroupDao(ResearchGroupDao groupDao) {
        this.groupDao = groupDao;
    }

    @Override
    @Transactional
    public Integer create(Article article) {

        Integer id = dao.create(article);

        if (article.getResearchGroup() != null) {
            ResearchGroup group = groupDao.read(article.getResearchGroup().getResearchGroupId());
            for (Person subscriber : group.getArticlesSubscribers()) {
                if (!subscriber.equals(article.getPerson())) {
                    mailService.sendNotification(subscriber.getEmail(), article, LocaleContextHolder.getLocale());
                }
            }
        }

        return id;
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

        List<ArticleComment> comments = commentDao.getCommentsForArticle(persistentObject.getArticleId());

        for (ArticleComment comment : comments) {
            commentDao.delete(comment);
        }

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

        Article detail = dao.getArticleDetail(id, loggedPerson);
        List<ArticleComment> comments = commentDao.getCommentsForArticle(detail.getArticleId());
        
        detail.setArticleComments(new LinkedHashSet<ArticleComment>(comments));
        
        return detail;
    }

    @Override
    @Transactional
    public Integer create(ArticleComment comment) {

        Integer id = commentDao.create(comment);
        // logged user is that person who own this comment.
        Person loggedUser = comment.getPerson();

        Article article = dao.read(comment.getArticle().getArticleId());
        for (Person subscriber : article.getSubscribers()) {
            if (!loggedUser.equals(subscriber)) {
                mailService.sendNotification(subscriber.getEmail(), comment, LocaleContextHolder.getLocale());
            }
        }
        return id;
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
