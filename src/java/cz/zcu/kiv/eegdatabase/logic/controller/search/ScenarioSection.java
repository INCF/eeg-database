/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.logic.controller.search;

import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import java.util.List;
import java.util.Set;
import java.lang.reflect.Field;
import java.util.HashSet;
import org.apache.lucene.search.Query;

/**
 *
 * @author Honza
 */
public class ScenarioSection extends SectionCreator {

  @Override
  public Set<FulltextResult> createSection(Query query, Class type, String[] fields) {
    List<Object> list = super.getFulltextResults(query, type);
    Set<FulltextResult> results = new HashSet<FulltextResult>();
    String path = "/scenarios/detail.html?scenarioId=";
    if (!list.isEmpty()) {
      int objectType = -1;
      if (list.get(0) instanceof Scenario) {
        objectType = super.MAIN_OBJECT;
      } else {
        Field[] field = list.get(0).getClass().getDeclaredFields();
        for (int i = 0; i < field.length; i++) {
          field[i].setAccessible(true);
          if (field[i].getName().equals("scenario")) {
            objectType = super.ONE_REL;
            break;
          } else if(field[i].getName().equals("scenarios")){
            objectType = super.SET;
            break;
          }
        }
      }

      for (Object t : list) {
        String title = "";

        int id = 0;
        try {
          String text = super.getHighlightedText(fields, t);
          if (objectType == super.MAIN_OBJECT) {
            title = ((Scenario) t).getTitle();
            id = ((Scenario) t).getScenarioId();
            results.add(new FulltextResult
                  (id, text + "length="+((Scenario) t).getScenarioLength(), "Scenario", path, title));

          } else if (objectType == super.ONE_REL){
            Field field = t.getClass().getDeclaredField("scenario");
            field.setAccessible(true);
            Scenario s = (Scenario) field.get(t);
            title = s.getTitle();
            id = s.getScenarioId();
            results.add(new FulltextResult
                  (id, text + "length=" + s.getScenarioLength(), "Scenario", path, title));
            
          } else {
            Field field = t.getClass().getDeclaredField("scenarios");
            field.setAccessible(true);
            Set<Scenario> set = (Set) field.get(t);
            for (Scenario scenario : set) {
              title = scenario.getTitle();
              id = scenario.getScenarioId();
              results.add(new FulltextResult
                  (id, text + "length=" + scenario.getScenarioLength(), "Scenario", path, title));
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
