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
 *   SimpleArticleDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Article;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiri Vlasimsky
 */
public class SimpleArticleDao extends SimpleGenericDao<Article, Integer> implements ArticleDao {
    public SimpleArticleDao() {
        super(Article.class);
    }

    @Override
    public List<Article> getAllArticles() {
        String HQLSelect = "from Article order by time desc";
        List<Article> articles = getHibernateTemplate().find(HQLSelect);
        return articles;
    }

    @Override
    public List<Article> getArticlesForUser(Person person) {
        String query;
        List articles = null;

        if (person.getAuthority().equals("ROLE_ADMIN")) {
            // We can simply load the newest articles
            query = "from Article a left join fetch a.researchGroup r " +
                    "order by a.time desc";
            articles = getHibernateTemplate().find(query);
        } else {
            // We need to load only articles which can be viewed by the logged user.
            // That is, we need to load only public articles or articles from the groups the logged user is member of.
            query = "from Article a left join fetch a.researchGroup r " +
                    "where " +
                    "a.researchGroup.researchGroupId is null or " +
                    "a.researchGroup.researchGroupId in " +
                    "(select rm.id.researchGroupId from ResearchGroupMembership rm where rm.id.personId = :personId) " +
                    "order by a.time desc";
            articles = getHibernateTemplate().findByNamedParam(query, "personId", person.getPersonId());
        }

        return articles;
    }

    @Override
    public List<Article> getArticlesForUser(Person person, int limit) {
        getHibernateTemplate().setMaxResults(limit);
        List<Article> articles = getArticlesForUser(person);
        getHibernateTemplate().setMaxResults(0);
        return articles;
    }

    @Override
    public List<Article> getArticlesForList(Person person, int min, int count) {
        String query;
        List articles = null;

        if (person.getAuthority().equals("ROLE_ADMIN")) {
            // We can simply load the newest articles
            query = "from Article a left join fetch a.researchGroup r join fetch a.person p " +
                    "order by a.time desc";
            articles = getSession().createQuery(query).setFirstResult(min).setMaxResults(count).list();
        } else {
            // We need to load only articles which can be viewed by the logged user.
            // That is, we need to load only public articles or articles from the groups the logged user is member of.
            query = "from Article a left join fetch a.researchGroup r join fetch a.person p " +
                    "where " +
                    "a.researchGroup.researchGroupId is null or " +
                    "a.researchGroup.researchGroupId in " +
                    "(select rm.id.researchGroupId from ResearchGroupMembership rm where rm.id.personId = :personId) " +
                    "order by a.time desc";
            articles = getSession().createQuery(query).setFirstResult(min).setMaxResults(count).setParameter("personId", person.getPersonId()).list();
        }

        return articles;
    }

    @Override
    public int getArticleCountForPerson(Person person) {
        if (person.getAuthority().equals("ROLE_ADMIN")) {
            return ((Long) getSession().createQuery("select count(*) from Article").uniqueResult()).intValue();
        }
        String query = "select count(*) from Article a left join a.person p where " +
                "a.researchGroup.researchGroupId is null or " +
                "a.researchGroup.researchGroupId in " +
                "(select rm.id.researchGroupId from ResearchGroupMembership rm where rm.id.personId = :personId)";
        return ((Long) getSession().createQuery(query).setParameter("personId", person.getPersonId()).uniqueResult()).intValue();
    }

    /**
     * Gets article detail information for article detail page. Check the correct permission of the user to view
     * requested article.
     *
     * @param id           Id of the requested article
     * @param loggedPerson User whose permission is checked - should be logged user
     * @return If the user is permitted to view the article specified by id the Article object is returned. Otherwise, null is returned.
     */
    @Override
    public Article getArticleDetail(int id, Person loggedPerson) {

        if (loggedPerson.getAuthority().equals("ROLE_ADMIN")) {
            String query = "from Article a join fetch a.person left join fetch a.researchGroup " +
                    "where " +
                    "a.articleId = :id";
            return (Article) getSession().createQuery(query).setParameter("id", id).uniqueResult();
        } else {
            String query = "from Article a join fetch a.person left join fetch a.researchGroup " +
                    "where " +
                    "a.articleId = :id and (" +
                    "a.researchGroup.researchGroupId is null or " +
                    "a.researchGroup.researchGroupId in " +
                    "(select rm.id.researchGroupId from ResearchGroupMembership rm where rm.id.personId = :personId))";
            return (Article) getSession().createQuery(query).setParameter("id", id).setParameter("personId", loggedPerson.getPersonId()).uniqueResult();
        }
    }

    public List<Article> getAllUserArticles() {
        String HQLSelect = "from Article order by time desc";
        List<Article> articles = getHibernateTemplate().find(HQLSelect);
        return articles;
    }
}
