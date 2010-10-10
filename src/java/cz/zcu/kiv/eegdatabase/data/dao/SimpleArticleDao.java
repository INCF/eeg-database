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
public class SimpleArticleDao<T, PK extends Serializable>
  extends SimpleGenericDao<T, PK> implements ArticleDao<T, PK>
{ 
  public SimpleArticleDao(Class<T> type) {
    super(type);
  }

}
