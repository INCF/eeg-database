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
    public List getArticlesForUser(Person person, int limit) {
        getHibernateTemplate().setMaxResults(limit);
        List articles = getArticlesForUser(person);
        getHibernateTemplate().setMaxResults(0);
        return articles;
    }

    @Override
    public List getArticlesForList(Person person, int min, int count) {
        String query;
        List articles = null;

        if (person.getAuthority().equals("ROLE_ADMIN")) {
            // We can simply load the newest articles
            query = "select new map(a.articleId as articleId, a.title as title, a.time as time, " +
                    "r.researchGroupId as researchGroupId, r.title as researchGroupTitle, " +
                    "a.articleComments.size as commentCount, p.givenname||' '||p.surname as authorName, " +
                    "p.personId as ownerId, substring(a.text, 1, 500) as textPreview) " +
                    "from Article a left join a.researchGroup r left join a.person p " +
                    "order by a.time desc";
            articles = getSession().createQuery(query).setFirstResult(min).setMaxResults(count).list();
        } else {
            // We need to load only articles which can be viewed by the logged user.
            // That is, we need to load only public articles or articles from the groups the logged user is member of.
            query = "select new map(a.articleId as articleId, a.title as title, a.time as time, " +
                    "r.researchGroupId as researchGroupId, r.title as researchGroupTitle, " +
                    "a.articleComments.size as commentCount, p.givenname||' '||p.surname as authorName, " +
                    "p.personId as ownerId, substring(a.text, 1, 500) as textPreview) " +
                    "from Article a left join a.researchGroup r left join a.person p where " +
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


    public List<Article> getAllUserArticles() {
        String HQLSelect = "from Article order by time desc";
        List<Article> articles = getHibernateTemplate().find(HQLSelect);
        return articles;
    }

}
