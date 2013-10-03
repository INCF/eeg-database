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
 *   ArticleFacadeImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.article;

import cz.zcu.kiv.eegdatabase.data.pojo.Article;
import cz.zcu.kiv.eegdatabase.data.pojo.ArticleComment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class ArticleFacadeImpl implements ArticleFacade {
    
    protected Log log = LogFactory.getLog(getClass());
    
    ArticleService service;
    
    @Required
    public void setService(ArticleService service) {
        this.service = service;
    }
    
    @Override
    public Integer create(Article newInstance) {
        return service.create(newInstance);
    }

    @Override
    public Article read(Integer id) {
        return service.read(id);
    }

    @Override
    public List<Article> readByParameter(String parameterName, Object parameterValue) {
        return service.readByParameter(parameterName, parameterValue);
    }

    @Override
    public void update(Article transientObject) {
        service.update(transientObject);
    }

    @Override
    public void delete(Article persistentObject) {
        service.delete(persistentObject);
    }

    @Override
    public List<Article> getAllRecords() {
        return service.getAllRecords();
    }

    @Override
    public List<Article> getRecordsAtSides(int first, int max) {
        return service.getRecordsAtSides(first, max);
    }

    @Override
    public int getCountRecords() {
        return service.getCountRecords();
    }

    @Override
    public List<Article> getUnique(Article example) {
        return service.getUnique(example);
    }

    @Override
    public List<Article> getAllArticles() {
        return service.getAllArticles();
    }

    @Override
    public List<Article> getArticlesForUser(Person person) {
        return service.getArticlesForUser(person);
    }

    @Override
    public List<Article> getArticlesForUser(Person person, int limit) {
        return service.getArticlesForUser(person, limit);
    }

    @Override
    public List<Article> getArticlesForList(Person person, int min, int count) {
        return service.getArticlesForList(person, min, count);
    }

    @Override
    public int getArticleCountForPerson(Person person) {
        return service.getArticleCountForPerson(person);
    }

    @Override
    public Article getArticleDetail(int id, Person loggedPerson) {
        return service.getArticleDetail(id, loggedPerson);
    }

    @Override
    public Integer create(ArticleComment newInstance) {
        return service.create(newInstance);
    }

    @Override
    public ArticleComment readComment(Integer id) {
        return service.readComment(id);
    }

    @Override
    public List<ArticleComment> readCommentByParameter(String parameterName, int parameterValue) {
        return service.readCommentByParameter(parameterName, parameterValue);
    }

    @Override
    public List<ArticleComment> readCommentByParameter(String parameterName, String parameterValue) {
        return service.readCommentByParameter(parameterName, parameterValue);
    }

    @Override
    public void updateComment(ArticleComment transientObject) {
        service.updateComment(transientObject);
    }

    @Override
    public void deleteComment(ArticleComment persistentObject) {
        service.deleteComment(persistentObject);
    }

    @Override
    public List<ArticleComment> getAllCommentRecords() {
        return service.getAllCommentRecords();
    }

    @Override
    public List<ArticleComment> getCommentRecordsAtSides(int first, int max) {
        return service.getCommentRecordsAtSides(first, max);
    }

    @Override
    public int getCountCommentRecords() {
        return service.getCountCommentRecords();
    }

    @Override
    public List<ArticleComment> getCommentsForArticle(int articleId) {
        return service.getCommentsForArticle(articleId);
    }

}
