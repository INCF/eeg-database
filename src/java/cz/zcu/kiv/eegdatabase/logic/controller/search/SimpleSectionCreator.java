/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.controller.search;

import cz.zcu.kiv.eegdatabase.logic.wrapper.Wrapper;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Honza
 */
public class SimpleSectionCreator extends SectionCreator {

  @Override
  public Set<FulltextResult> createSection(Queries queries, String[] fields, Wrapper type,
          RelationshipType relType) {
    List<Object> list = super.getFulltextResults(queries);
    Set<FulltextResult> results = new HashSet<FulltextResult>();
    if (!list.isEmpty()) {
      for (Object o : list) {
        try {
          switch (relType) {
            case SECTION_OBJ:
              type.setObject(o);
             highlight(o, o, results, fields, type);
              break;
            case SIMPLE_REL:
              Field f = o.getClass().getDeclaredField(type.className().toLowerCase());
              f.setAccessible(true);
              Object obj = f.get(o);
              type.setObject(obj);
              highlight(obj, o, results, fields, type);
              break;
            case SET:
              f = o.getClass().getDeclaredField(type.getSetName());
              f.setAccessible(true);
              Set set = (Set) f.get(o);
              for (Object object : set) {
                type.setObject(object);
                highlight(object, o, results, fields, type);
              }
              break;
          }
        } catch (Exception e) {
          continue;
        }
      }
    }

    return results;
  }


  private void highlight(Object o, Object main, Set<FulltextResult> results, String[] fields, Wrapper type)
          throws Exception {
    String title = type.getTitle();
    Field f = o.getClass().getDeclaredField(type.className().toLowerCase() + "Id");
    f.setAccessible(true);
    int id = (Integer) f.get(o);
    results.add(new FulltextResult
            (id, super.getHighlightedText(fields, main), type.className(), type.getPath(), title));
  }
}
