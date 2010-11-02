/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.controller.search;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.lucene.search.Query;

/**
 *
 * @author Honza
 */
public class PersonSection extends SectionCreator {

  @Override
  public Set<FulltextResult> createSection(Query query, Class type, String[] fields) {
    List<Object> list = super.getFulltextResults(query, type);
    Set<FulltextResult> results = new HashSet<FulltextResult>();
    String path = "/people/detail.html?personId=";
    if (!list.isEmpty()) {
int objectType = -1;
      if (list.get(0) instanceof Person) {
        objectType = super.MAIN_OBJECT;
      } else {
        Field[] field = list.get(0).getClass().getDeclaredFields();
        for (int i = 0; i < field.length; i++) {
          field[i].setAccessible(true);
          if (field[i].getName().equals("person")) {
            objectType = super.ONE_REL;
            break;
          } else if (field[i].getName().equals("persons")){
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
            title = ((Person) t).getNote();
            id = ((Person) t).getPersonId();
            results.add(new FulltextResult
                  (id, super.getHighlightedText(fields, t), "Person", path, title));

          } else if (objectType == super.ONE_REL){
            Field field = t.getClass().getDeclaredField("person");
            field.setAccessible(true);
            Person p = (Person) field.get(t);
            title = p.getNote();
            id = p.getPersonId();
            results.add(new FulltextResult
                  (id, super.getHighlightedText(fields, t), "Person", path, title));

          } else {
            Field field = t.getClass().getDeclaredField("persons");
            field.setAccessible(true);
            Set<Person> set = (Set) field.get(t);
            for (Person p : set) {
              title = p.getNote();
              id = p.getPersonId();
              results.add(new FulltextResult
                  (id, super.getHighlightedText(fields, t), "Person", path, title));
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
