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

  public ArticleWrapper(Object a) {
    this.a = (Article) a;
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
  public String getPath() {
    return "/articles/detail.html?articleId=";
  }
}
