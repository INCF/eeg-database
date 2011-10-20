/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Article;

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

    public List<Article> getAllUserArticles() {
        String HQLSelect = "from Article order by time desc";
        List<Article> articles = getHibernateTemplate().find(HQLSelect);
        return articles;
    }

}
