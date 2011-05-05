/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ScenarioSchemas;
import java.util.List;

/**
 *
 * @author Jan Koreň
 */
public class SimpleScenarioSchemasDao
        extends SimpleGenericDao<ScenarioSchemas, Integer>
        implements ScenarioSchemasDao {

  public SimpleScenarioSchemasDao() {
    super(ScenarioSchemas.class);
  }

  public List<ScenarioSchemas> getScenarioSchemaNames() {
    String hqlSelect = "from ScenarioSchemas scenarioSchemas";
    List<ScenarioSchemas> list = getHibernateTemplate().find(hqlSelect);
    return list;
  }

}
