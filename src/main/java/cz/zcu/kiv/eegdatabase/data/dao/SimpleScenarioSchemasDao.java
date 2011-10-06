/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ScenarioSchemas;

import java.util.List;

/**
 * @author Jan Koren
 */
public class SimpleScenarioSchemasDao
        extends SimpleGenericDao<ScenarioSchemas, Integer>
        implements ScenarioSchemasDao {

    public SimpleScenarioSchemasDao() {
        super(ScenarioSchemas.class);
    }

    public List<ScenarioSchemas> getSchemaNames() {
        String hqlSelect = "from ScenarioSchemas scenarioSchemas where scenarioSchemas.approved='y'";
        List<ScenarioSchemas> list = getHibernateTemplate().find(hqlSelect);
        return list;
    }

    public List<ScenarioSchemas> getSchemaDescriptions() {
        String hqlSelect = "from ScenarioSchemas scenarioSchemas where scenarioSchemas.approved='y'";
        List<ScenarioSchemas> list = getHibernateTemplate().find(hqlSelect);
        return list;
    }

    public int getNextSchemaId() {
        String hqlSelect = "select max(schema.schemaId) from ScenarioSchemas schema";
        List<Integer> idList = getHibernateTemplate().find(hqlSelect);
        int id = idList.get(0) + 1;
        return id;
    }

}
