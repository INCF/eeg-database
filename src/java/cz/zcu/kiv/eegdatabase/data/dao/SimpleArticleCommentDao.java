/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.data.dao;

import java.io.Serializable;

/**
 *
 * @author Jiri Vlasimsky
 */
public class SimpleArticleCommentDao<T, PK extends Serializable>
  extends SimpleGenericDao<T, PK> implements ArticleCommentDao<T, PK>
{
  public SimpleArticleCommentDao(Class<T> type) {
    super(type);
  }

}
