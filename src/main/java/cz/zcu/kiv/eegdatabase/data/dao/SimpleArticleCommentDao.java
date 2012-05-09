/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ArticleComment;

import java.io.Serializable;
import java.util.List;

/**
 * @author Jiri Vlasimsky
 */
public class SimpleArticleCommentDao<T, PK extends Serializable>
        extends SimpleGenericDao<T, PK> implements ArticleCommentDao<T, PK> {

    public SimpleArticleCommentDao(Class<T> type) {
        super(type);
    }

    @Override
    public List<ArticleComment> getCommentsForArticle(int articleId) {
        String query = "select distinct c from ArticleComment c left join fetch c.children join fetch c.person " +
                "where " +
                "c.article.id = :id " +
                "order by c.time desc";
        return getSessionFactory().getCurrentSession().createQuery(query).setParameter("id", articleId).list();
    }
}
