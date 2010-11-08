/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.controller.search;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Honza
 */
public class SimpleSectionCreator extends SectionCreator {

  @Override
  public Set<FulltextResult> createSection(Queries queries, String[] fields, SectionType type) {
    List<Object> list = super.getFulltextResults(queries);
    Set<FulltextResult> results = new HashSet<FulltextResult>();
    if (!list.isEmpty()) {
      RelationshipType relType = getObjectType(list.get(0), type);
      for (Object o : list) {
        try {
          switch (relType) {
            case SECTION_OBJ:
             highlight(o, o, results, fields, type);
              break;
            case SIMPLE_REL:
              Field f = o.getClass().getDeclaredField(type.className().toLowerCase());
              f.setAccessible(true);
              Object obj = f.get(o);
              highlight(obj, o, results, fields, type);
              break;
            case SET:
              f = o.getClass().getDeclaredField(type.getSetName());
              f.setAccessible(true);
              Set set = (Set) f.get(o);
              for (Object object : set) {
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

  private RelationshipType getObjectType(Object o, SectionType type) {
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

  private void highlight(Object o, Object main, Set<FulltextResult> results, String[] fields, SectionType type)
          throws Exception {
    String title = getTitle(o, type);
    int id = getId(o, type);
    results.add(new FulltextResult
            (id, super.getHighlightedText(fields, main), type.className(), type.getPath(), title));
  }

  private String getTitle(Object o, SectionType type) throws NoSuchMethodException,
          IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    switch (type) {
      case ARTICLE:
      case SCENARIO:
        Method method = o.getClass().getMethod("getTitle", new Class[0]);
        return (String) method.invoke(o, new Object[0]);
      case PERSON:
        method = o.getClass().getMethod("getNote", new Class[0]);
        return (String) method.invoke(o, new Object[0]);
      case EXPERIMENT:
        method = o.getClass().getMethod("getScenario", new Class[0]);
        Object scen = method.invoke(o, new Object[0]);
        method = scen.getClass().getMethod("getTitle", new Class[0]);
        return (String) method.invoke(scen, new Object[0]);
      default:
        return "No title";
    }
  }

  private int getId(Object o, SectionType type) throws NoSuchFieldException,
          IllegalArgumentException, IllegalAccessException {
    Field f = o.getClass().getDeclaredField(type.className().toLowerCase() + "Id");
    f.setAccessible(true);
    return (Integer) f.get(o);
  }
}
