/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.controller.search;

import cz.zcu.kiv.eegdatabase.logic.wrapper.IWrapper;
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
  public Set<FulltextResult> createSection(Queries queries, String[] fields, IWrapper type) {
    List<Object> list = super.getFulltextResults(queries);
    Set<FulltextResult> results = new HashSet<FulltextResult>();
    if (!list.isEmpty()) {
      RelationshipType relType = getObjectType(list.get(0), type);
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

  private RelationshipType getObjectType(Object o, IWrapper type) {
    Field[] fields = o.getClass().getDeclaredFields();
    if (o.getClass().getName().endsWith(type.className())) {
      return RelationshipType.SECTION_OBJ;
    }
    for (int i = 0; i < fields.length; i++) {
      fields[i].setAccessible(true);
      if (fields[i].getName().equals(type.className().toLowerCase())) {
        return RelationshipType.SIMPLE_REL;
      }
      if (fields[i].getName().equals(type.getSetName())) {
        return RelationshipType.SET;
      }
    }
    return RelationshipType.NO_DEF;
  }

  private void highlight(Object o, Object main, Set<FulltextResult> results, String[] fields, IWrapper type)
          throws Exception {
    String title = type.getTitle();
    int id = getId(o, type);
    results.add(new FulltextResult
            (id, super.getHighlightedText(fields, main), type.className(), type.getPath(), title));
  }


  private int getId(Object o, IWrapper type) throws NoSuchFieldException,
          IllegalArgumentException, IllegalAccessException {
    Field f = o.getClass().getDeclaredField(type.className().toLowerCase() + "Id");
    f.setAccessible(true);
    return (Integer) f.get(o);
  }
}
