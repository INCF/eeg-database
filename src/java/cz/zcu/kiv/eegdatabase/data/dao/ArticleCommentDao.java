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
 * @author Jiriik
 */
public interface ArticleCommentDao<T, PK extends Serializable> extends GenericDao<T, PK> {
  public List<ArticleComment> getAll(Article article);
}

