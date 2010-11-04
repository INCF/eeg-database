/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.controller.search;

import cz.zcu.kiv.eegdatabase.data.pojo.Article;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.lucene.search.Query;

/**
 *
 * @author Honza
 */
public class ArticleSection extends SectionCreator {

  @Override
  public Set<FulltextResult> createSection(Queries queries, Class type, String[] fields) {
    List<Object> list = super.getFulltextResults(queries, type);
    Set<FulltextResult> results = new HashSet<FulltextResult>();
    String path = "/articles/detail.html?articleId=";
    if (!list.isEmpty()) {
      int objectType = -1;
      if (list.get(0) instanceof Article) {
        objectType = super.MAIN_OBJECT;
      } else {
        Field[] field = list.get(0).getClass().getDeclaredFields();
        for (int i = 0; i < field.length; i++) {
          field[i].setAccessible(true);
          if (field[i].getName().equals("article")) {
            objectType = super.ONE_REL;
            break;
          } else if (field[i].getName().equals("articles")){
            objectType = super.SET;
            break;
          }
        }
      }

      for (Object t : list) {
        String title = "";
        int id = 0;
        try {
          if (objectType == super.MAIN_OBJECT) {
            title = ((Article) t).getTitle();
            id = ((Article) t).getArticleId();
            results.add(new FulltextResult
                  (id, super.getHighlightedText(fields, t), "Article", path, title));

          } else if (objectType == super.ONE_REL){
            Field field = t.getClass().getDeclaredField("article");
            field.setAccessible(true);
            Article a = (Article) field.get(t);
            title = a.getTitle();
            id = a.getArticleId();
            results.add(new FulltextResult
                  (id, super.getHighlightedText(fields, t), "Article", path, title));

          } else {
            Field field = t.getClass().getDeclaredField("articles");
            field.setAccessible(true);
            Set<Article> set = (Set) field.get(t);
            for (Article a : set) {
              title = a.getTitle();
              id = a.getArticleId();
              results.add(new FulltextResult
                  (id, super.getHighlightedText(fields, t), "Article", path, title));
            }
          }
        } catch (Exception ex) {
          continue;
        }
      }
    }

    return results;
  }
}
