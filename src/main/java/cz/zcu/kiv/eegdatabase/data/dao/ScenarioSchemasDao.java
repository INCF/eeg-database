/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ScenarioSchemas;
import java.util.List;

/**
 *
 * @author Jan Koren
 */
public interface ScenarioSchemasDao extends GenericDao<ScenarioSchemas, Integer> {

  public List<ScenarioSchemas> getSchemaNames();
  public List<ScenarioSchemas> getSchemaDescriptions();
  public int getNextSchemaId();
  
  List<ScenarioSchemas> getListOfScenarioSchemas();
}
