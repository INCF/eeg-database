package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.data.pojo.ScenarioType;
import cz.zcu.kiv.eegdatabase.data.pojo.ScenarioTypeNonSchema;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Koren
 * Date: 22.4.11
 * Time: 17:12
 * To change this template use File | Settings | File Templates.
 */
public class SimpleScenarioTypeDao extends SimpleGenericDao<ScenarioType, Integer>
                                   implements ScenarioTypeDao {

    public SimpleScenarioTypeDao() {
        super(ScenarioType.class);
    }
}
