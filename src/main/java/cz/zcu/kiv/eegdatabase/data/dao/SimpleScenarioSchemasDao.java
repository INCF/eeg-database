/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   SimpleScenarioSchemasDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ScenarioSchemas;
import org.hibernate.Hibernate;

import java.sql.Clob;
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
    
    /**
     * This list isn't what method name say. Depraceted. Instead of this use getListOfScenarioSchemas().
     */
    @Deprecated
    public List<ScenarioSchemas> getSchemaNames() {
        String hqlQuery = "from ScenarioSchemas scenarioSchemas where scenarioSchemas.approved='y'";
        List<ScenarioSchemas> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).list();
        return list;
    }
    
    /**
     * This list isn't what method name say. Depraceted. Instead of this use getListOfScenarioSchemas().
     */
    @Deprecated
    public List<ScenarioSchemas> getSchemaDescriptions() {
        String hqlQuery = "from ScenarioSchemas scenarioSchemas where scenarioSchemas.approved='y'";
        List<ScenarioSchemas> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).list();
        return list;
    }

    public int getNextSchemaId() {
        String hqlQuery= "select max(schema.schemaId) from ScenarioSchemas schema";
        List<Integer> idList = getSessionFactory().getCurrentSession().createQuery(hqlQuery).list();
        int id = idList.get(0) + 1;
        return id;
    }

    @Override
    public List<ScenarioSchemas> getListOfScenarioSchemas() {
        String hqlQuery = "from ScenarioSchemas scenarioSchemas where scenarioSchemas.approved='y'";
        List<ScenarioSchemas> list = getSessionFactory().getCurrentSession().createQuery(hqlQuery).list();
        return list;
    }

    @Override
    public Clob createClob(String content) {
        return Hibernate.getLobCreator(this.getSessionFactory().getCurrentSession()).createClob(content);
    }

}
