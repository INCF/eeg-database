/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Article;
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

    public List<ArticleComment> getAllWithNoParent(Article article) {
        String query = "from ArticleComment as comment where comment.article.id = :id and comment.parent is null order by time desc";
        List<ArticleComment> comments = getSessionFactory().getCurrentSession().createQuery(query).setParameter("id", article.getArticleId()).list();
        return comments;
    }
}
