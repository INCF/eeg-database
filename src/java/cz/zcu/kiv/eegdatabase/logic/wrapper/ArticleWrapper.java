/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.wrapper;

import cz.zcu.kiv.eegdatabase.data.pojo.Article;

/**
 *
 * @author Honza
 */
public class ArticleWrapper implements Wrapper {

  private Article a;

  @Override
  public void setObject(Object o) {
    this.a = (Article) o;
  }

  @Override
  public String getTitle() {
    return a.getTitle();
  }

  @Override
  public String className() {
    return "Article";
  }

  @Override
  public String getSetName() {
    return "articles";
  }

  @Override
  public String getPath() {
    return "/articles/detail.html?articleId=";
  }
}
