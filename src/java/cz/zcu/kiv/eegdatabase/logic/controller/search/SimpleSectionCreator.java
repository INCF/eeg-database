/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.controller.search;

import cz.zcu.kiv.eegdatabase.logic.wrapper.Wrapper;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Honza
 */
public class SimpleSectionCreator extends SectionCreator {

  @Override
  public Set<FulltextResult> createSection(Queries queries, String[] fields, SectionType wType,
          RelationshipType relType) {
    List<Object> list = super.getFulltextResults(queries);
    Set<FulltextResult> results = new HashSet<FulltextResult>();
    Constructor<Wrapper> con = null;
    try {
      con = wType.getWrapperClass().getConstructor(Object.class);
    } catch (Exception ex) {
      Logger.getLogger(SimpleSectionCreator.class.getName()).log(Level.SEVERE, null, ex);
    } 
    if (!list.isEmpty()) {
      for (Object o : list) {
        try {
          
          Wrapper wrapper = null;
          switch (relType) {
            case SECTION_OBJ:
              wrapper = (Wrapper) con.newInstance(o);
             highlight(o, o, results, fields, wrapper);
              break;
            case SIMPLE_REL:
              Field f = o.getClass().getDeclaredField(wType.getSimpleRelName());
              f.setAccessible(true);
              Object obj = f.get(o);
              wrapper = (Wrapper) con.newInstance(obj);
              highlight(obj, o, results, fields, wrapper);
              break;
            case SET:
              f = o.getClass().getDeclaredField(wType.getSetName());
              f.setAccessible(true);
              Set set = (Set) f.get(o);
              for (Object object : set) {
                wrapper = (Wrapper) con.newInstance(object);
                highlight(object, o, results, fields, wrapper);
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
