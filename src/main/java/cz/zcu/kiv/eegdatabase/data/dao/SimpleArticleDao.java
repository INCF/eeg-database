/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Article;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;

import java.io.Serializable;
import java.util.List;

/**
 * @author Jiri Vlasimsky
 */
public class SimpleArticleDao<T, PK extends Serializable>
        extends SimpleGenericDao<T, PK> implements ArticleDao<T, PK> {
    public SimpleArticleDao(Class<T> type) {
        super(type);
    }

    public List<Article> getAllArticles() {
        String HQLSelect = "from Article order by time desc";
        List<Article> articles = getHibernateTemplate().find(HQLSelect);
        return articles;
    }

    @Override
    public List getArticlesForUser(Person person) {
        String query;
        List articles = null;

        if (person.getAuthority().equals("ROLE_ADMIN")) {
            // We can simply load the newest articles
            query = "select new map(a.articleId as articleId, a.title as title, a.time as time, " +
                    "r.researchGroupId as researchGroupId, r.title as researchGroupTitle, " +
                    "a.articleComments.size as commentCount)" +
                    "from Article a left join a.researchGroup r " +
                    "order by a.time desc";
            articles = getHibernateTemplate().find(query);
        } else {
            // We need to load only articles which can be viewed by the logged user.
            // That is, we need to load only public articles or articles from the groups the logged user is member of.
            query = "select new map(a.articleId as articleId, a.title as title, a.time as time, " +
                    "r.researchGroupId as researchGroupId, r.title as researchGroupTitle, " +
                    "a.articleComments.size as commentCount)" +
                    "from Article a left join a.researchGroup r where " +
                    "a.researchGroup.researchGroupId is null or " +
                    "a.researchGroup.researchGroupId in " +
                    "(select rm.id.researchGroupId from ResearchGroupMembership rm where rm.id.personId = :personId) " +
                    "order by a.time desc";
            articles = getHibernateTemplate().findByNamedParam(query, "personId", person.getPersonId());
        }

        return articles;
    }

    @Override
    public List getArticlesForUser(Person person, int limit) {
        getHibernateTemplate().setMaxResults(limit);
        List articles = getArticlesForUser(person);
        getHibernateTemplate().setMaxResults(0);
        return articles;
    }

    public List<Article> getAllUserArticles() {
        String HQLSelect = "from Article order by time desc";
        List<Article> articles = getHibernateTemplate().find(HQLSelect);
        return articles;
    }

}
