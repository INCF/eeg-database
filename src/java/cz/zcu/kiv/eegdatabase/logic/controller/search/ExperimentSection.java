/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.logic.controller.search;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.lucene.search.Query;

/**
 *
 * @author Honza
 */
public class ExperimentSection extends SectionCreator {

  @Override
  public Set<FulltextResult> createSection(Query query, Class type, String[] fields) {
    List<Object> list = super.getFulltextResults(query, type);
    Set<FulltextResult> results = new HashSet<FulltextResult>();
    String path = "/experiments/detail.html?experimentId=";
    if (!list.isEmpty()) {
      int objectType = -1;
      if (list.get(0) instanceof Experiment) {
        objectType = super.MAIN_OBJECT;
      } else {
        Field[] field = list.get(0).getClass().getDeclaredFields();
        for (int i = 0; i < field.length; i++) {
          field[i].setAccessible(true);
          if (field[i].getName().equals("experiment")) {
            objectType = super.ONE_REL;
            break;
          } else if (field[i].getName().equals("experiments")){
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
            title = ((Experiment) t).getScenario().getTitle();
            id = ((Experiment) t).getExperimentId();
            results.add(new FulltextResult
                  (id, super.getHighlightedText(fields, t), "Experiment", path, title));

          } else if (objectType == super.ONE_REL){
            Field field = t.getClass().getDeclaredField("scenario");
            field.setAccessible(true);
            Experiment e = (Experiment) field.get(t);
            title = e.getScenario().getTitle();
            id = e.getExperimentId();
            results.add(new FulltextResult
                  (id, super.getHighlightedText(fields, t), "Experiment", path, title));

          } else {
            Field field = t.getClass().getDeclaredField("experiments");
            field.setAccessible(true);
            Set<Experiment> set = (Set) field.get(t);
            for (Experiment e : set) {
              title = e.getScenario().getTitle();
              id = e.getExperimentId();
              results.add(new FulltextResult
                  (id, super.getHighlightedText(fields, t), "Experiment", path, title));
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
