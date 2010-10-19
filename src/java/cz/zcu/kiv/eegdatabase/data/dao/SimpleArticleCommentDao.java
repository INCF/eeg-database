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
 *
 * @author Jiri Vlasimsky
 */
public class SimpleArticleCommentDao<T, PK extends Serializable>
        extends SimpleGenericDao<T, PK> implements ArticleCommentDao<T, PK> {

  public SimpleArticleCommentDao(Class<T> type) {
    super(type);
  }

  public List<ArticleComment> getAll(Article article) {
    int id = article.getArticleId();
    String HQLselect = "from ArticleComment as comment where comment.article.id = "+id+" order by time desc";

    List<ArticleComment> comments = getHibernateTemplate().find(HQLselect);
    return comments;
  }
}
